package com.hezaerd.mixin;

import com.hezaerd.config.SimpleMentionConfig;
import com.hezaerd.utils.MentionFormatter;
import com.hezaerd.utils.MentionProcessor;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SentMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {
    @Shadow @Final
    private MinecraftServer server;

    @Shadow @Final
    private ServerPlayerEntity player;

    @Inject(
            method = "sendChatMessage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/message/SentMessage;send(Lnet/minecraft/server/network/ServerPlayerEntity;ZLnet/minecraft/network/message/MessageType$Parameters;)V"
            ),
            cancellable = true
    )
    public void onSendChatMessage(SentMessage message, boolean filterMaskEnabled, MessageType.Parameters params, CallbackInfo ci) {
        if (!SimpleMentionConfig.get().enabled) return;

        String messageContent = message.getContent().getString();
        if (!MentionProcessor.containsMentions(messageContent)) return;

        // Cancel the original message sending
        ci.cancel();

        // Extract mentions
        List<String> mentions = MentionProcessor.extractMentions(messageContent);

                // Send different messages to different players based on configuration
        SimpleMentionConfig cfg = SimpleMentionConfig.get();

        for (ServerPlayerEntity targetPlayer : server.getPlayerManager().getPlayerList()) {
            // Check if this player is mentioned in the message
            boolean isMentioned = mentions.stream()
                    .anyMatch(username -> targetPlayer.getName().getString().equals(username));

            if (cfg.onlyMentionedPlayersSeeFormatting) {
                if (isMentioned) {
                    // Send formatted message to mentioned player
                    Text formattedMessage = MentionFormatter.formatMessageForPlayer(messageContent, targetPlayer);
                    targetPlayer.sendMessage(formattedMessage, false);
                } else {
                    // Send plain message to non-mentioned players
                    Text plainMessage = MentionFormatter.createPlainMessage(messageContent);
                    targetPlayer.sendMessage(plainMessage, false);
                }
            } else {
                // Send formatted message to all players (old behavior)
                Text formattedMessage = MentionFormatter.formatMessageForPlayer(messageContent, targetPlayer);
                targetPlayer.sendMessage(formattedMessage, false);
            }
        }

        // Process notifications for mentioned players
        for (String username : mentions) {
            ServerPlayerEntity mentionedPlayer = server.getPlayerManager().getPlayer(username);
            if (mentionedPlayer != null && !mentionedPlayer.equals(player)) {
                // Check if we should ignore own mentions
                if (!cfg.ignoreOwnMentions || !mentionedPlayer.equals(player)) {
                    notifyMentionedPlayer(mentionedPlayer, player);
                }
            }
        }
    }

    @Unique
    private void notifyMentionedPlayer(ServerPlayerEntity mentionedPlayer, ServerPlayerEntity mentioner) {
        SimpleMentionConfig cfg = SimpleMentionConfig.get();

        // Create ping message
        Text pingMessage = Text.literal(
                mentioner.getName().getString()
                        + Text.translatable("text.action.simple-mention.ping").getString()
        );

        // Send message to mentioned player
        mentionedPlayer.sendMessage(pingMessage, true);

        // Play sound if enabled
        if (cfg.playSound) {
            mentionedPlayer.playSound(cfg.sound, net.minecraft.sound.SoundCategory.MASTER, 1.0f, 1.5f);
        }

        // Show desktop notification if enabled
        if (cfg.desktopNotification) {
            com.hezaerd.utils.DesktopNotifier.notify(
                    "Simple Mention",
                    mentioner.getName().getString()
                            + Text.translatable("text.action.simple-mention.ping").getString());
        }
    }
}