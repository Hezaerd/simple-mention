package com.hezaerd.mixin;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.command.CommandSource;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(ChatInputSuggestor.class)
public abstract class ChatInputSuggestorMixin {
    @Unique
    private static final Pattern AT_PATTERN = Pattern.compile("@");
    
    @Shadow @Final
    TextFieldWidget textField;

    @Shadow @Final private boolean slashOptional;

    @Shadow @Final
    MinecraftClient client;

    @Shadow @Nullable
    private CompletableFuture<Suggestions> pendingSuggestions;

    @Shadow
    public abstract void show(boolean narrateFirstSuggestion);

    @Inject(
            method = "refresh",
            at = @At("TAIL"),
            cancellable = true
    )
    public void onRefresh(CallbackInfo ci) {
        String message = this.textField.getText();
        StringReader reader = new StringReader(message);
        boolean hasSlash = reader.canRead() && reader.peek() == '/';
        if (hasSlash) reader.skip();

        boolean isCommand = this.slashOptional || hasSlash;
        int cursor = this.textField.getCursor();

        if (!isCommand) {
            String textUptoCursor = message.substring(0, cursor);
            int atPosition = findLastAtPosition(textUptoCursor);

            if (atPosition >= 0 && atPosition < textUptoCursor.length()) {
                // Check if we're currently typing a mention
                String afterAt = textUptoCursor.substring(atPosition + 1);
                if (!afterAt.contains(" ")) { // No space after @ means we're typing a username
                    suggestPlayerNames(textUptoCursor, atPosition, ci);
                }
            }
        }
    }

    @Unique
    private int findLastAtPosition(String text) {
        Matcher matcher = AT_PATTERN.matcher(text);
        int lastPosition = -1;
        while (matcher.find()) {
            lastPosition = matcher.start();
        }
        return lastPosition;
    }

    @Unique
    private void suggestPlayerNames(String textUptoCursor, int atPosition, CallbackInfo ci) {
        List<String> playerNames = new ArrayList<>();
        ClientPlayNetworkHandler networkHandler = this.client.getNetworkHandler();

        if (networkHandler != null) {
            networkHandler.getPlayerList().forEach(entry ->
                    playerNames.add("@" + entry.getProfile().getName())
            );

            this.pendingSuggestions = CommandSource.suggestMatching(playerNames, new SuggestionsBuilder(textUptoCursor, atPosition));
            this.pendingSuggestions.thenRun(() -> {
                if (this.pendingSuggestions.isDone()) return;
                this.show(false);
            });
            ci.cancel();
        }
    }
}
