package com.hezaerd.mixin;

import com.hezaerd.config.SimpleMentionConfig;
import net.minecraft.client.network.message.MessageHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * Client-side mixin for message handling.
 * Note: Mention formatting is now handled server-side to ensure only mentioned players see formatting.
 */
@Mixin(MessageHandler.class)
public class MessageHandlerMixin {

    @ModifyArg(
            method = "processChatMessageInternal",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/ChatHud;addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V"
            )
    )
    private Text processMessage(Text message) {
        // Client-side formatting is disabled - formatting is now handled server-side
        // This ensures only mentioned players see the formatting
        return message;
    }
}
