package com.hezaerd.utils;

import net.minecraft.text.*;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;
import java.util.function.UnaryOperator;

/**
 * Utility class for formatting mentions in chat messages.
 */
public final class MentionFormatter {
    private static final int MENTION_COLOR = 0xFF52D0;

    private MentionFormatter() {
        // Utility class, prevent instantiation
    }

    /**
     * Formats a message with highlighted mentions for a specific player.
     *
     * @param message The original message
     * @param targetPlayer The player who will see the formatted message
     * @return Formatted message with mentions highlighted only for the target player
     */
    public static MutableText formatMessageForPlayer(String message, ServerPlayerEntity targetPlayer) {
        if (!MentionProcessor.containsMentions(message)) {
            return Text.literal(message);
        }

        List<String> mentions = MentionProcessor.extractMentions(message);
        MutableText result = Text.empty();
        String remainingMessage = message;

        for (String username : mentions) {
            int atIndex = remainingMessage.indexOf("@" + username);
            if (atIndex == -1) continue;

            // Add text before the mention
            if (atIndex > 0) {
                result.append(Text.literal(remainingMessage.substring(0, atIndex)));
            }

            // Check if this mention is for the target player
            if (targetPlayer.getName().getString().equals(username)) {
                // Highlight the mention for the mentioned player
                String mention = "@" + username;
                result.append(Text.literal(mention).styled(createMentionStyle(mention, targetPlayer)));
            } else {
                // Keep the mention plain for other players
                result.append(Text.literal("@" + username));
            }

            // Update remaining message
            remainingMessage = remainingMessage.substring(atIndex + username.length() + 1);
        }

        // Add any remaining text
        if (!remainingMessage.isEmpty()) {
            result.append(Text.literal(remainingMessage));
        }

        return result;
    }

    /**
     * Creates a plain message without any mention formatting.
     *
     * @param message The original message
     * @return Plain message without formatting
     */
    public static MutableText createPlainMessage(String message) {
        return Text.literal(message);
    }

    /**
     * Creates the style for a mention.
     *
     * @param mention The mention string (including @)
     * @param mentionedPlayer The player being mentioned
     * @return Style for the mention
     */
    private static UnaryOperator<Style> createMentionStyle(String mention, ServerPlayerEntity mentionedPlayer) {
        return style -> style
                .withColor(MENTION_COLOR)
                .withItalic(true)
                .withClickEvent(new ClickEvent(
                        ClickEvent.Action.SUGGEST_COMMAND,
                        mention + " "
                ))
                .withHoverEvent(new HoverEvent(
                        HoverEvent.Action.SHOW_TEXT,
                        createPlayerHoverText(mentionedPlayer)
                ));
    }

    /**
     * Creates hover text for a player.
     *
     * @param player The player to create hover text for
     * @return Hover text
     */
    private static MutableText createPlayerHoverText(ServerPlayerEntity player) {
        return Text.empty()
                .append(player.getDisplayName().copy().styled(style -> style.withColor(MENTION_COLOR)))
                .append("\n")
                .append(Text.literal(player.getUuidAsString()));
    }
}