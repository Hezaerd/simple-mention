package com.hezaerd.utils;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for processing mentions in chat messages efficiently.
 */
public final class MentionProcessor {
    // Pattern to match @username mentions
    private static final Pattern MENTION_PATTERN = Pattern.compile("@([A-Za-z0-9_.-]+)");

    // Pattern for highlighting mentions in chat
    private static final Pattern HIGHLIGHT_PATTERN = Pattern.compile("@[A-Za-z0-9_.-]+");

    private MentionProcessor() {
        // Utility class, prevent instantiation
    }

    /**
     * Extracts all mentions from a message string.
     *
     * @param message The message to extract mentions from
     * @return List of mentioned usernames (without @ symbol)
     */
    public static List<String> extractMentions(String message) {
        List<String> mentions = new ArrayList<>();
        Matcher matcher = MENTION_PATTERN.matcher(message);

        while (matcher.find()) {
            mentions.add(matcher.group(1)); // group(1) contains the username without @
        }

        return mentions;
    }

    /**
     * Processes mentions in a message and notifies mentioned players.
     *
     * @param message The message content
     * @param sender The player who sent the message
     * @param server The Minecraft server instance
     */
    public static void processMentions(String message, ServerPlayerEntity sender, MinecraftServer server) {
        if (message == null || message.isEmpty()) {
            return;
        }

        List<String> mentions = extractMentions(message);

        for (String username : mentions) {
            ServerPlayerEntity mentionedPlayer = server.getPlayerManager().getPlayer(username);
            if (mentionedPlayer != null && !mentionedPlayer.equals(sender)) {
                notifyPlayer(mentionedPlayer, sender);
            }
        }
    }

    /**
     * Notifies a player that they have been mentioned.
     *
     * @param mentionedPlayer The player being mentioned
     * @param mentioner The player who mentioned them
     */
    private static void notifyPlayer(ServerPlayerEntity mentionedPlayer, ServerPlayerEntity mentioner) {
        // This will be implemented in the mixin to avoid circular dependencies
        // The actual notification logic remains in the mixin
    }

    /**
     * Gets the pattern used for highlighting mentions in chat.
     *
     * @return The highlight pattern
     */
    public static Pattern getHighlightPattern() {
        return HIGHLIGHT_PATTERN;
    }

    /**
     * Checks if a message contains any mentions.
     *
     * @param message The message to check
     * @return true if the message contains mentions, false otherwise
     */
    public static boolean containsMentions(String message) {
        return message != null && MENTION_PATTERN.matcher(message).find();
    }
}