package com.hezaerd.utils;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public final class MentionFormatter {
    public static MutableText format(ServerPlayerEntity sender,
                                     String rawMessage,
                                     int mentionPos,
                                     int mentionLen) {

        // "<" in white
        MutableText prefix = Text.literal("<");

        // sender name in team colour
        MutableText name = Text.literal(sender.getName().getString())
                .setStyle(Style.EMPTY
                        .withColor(sender.getTeamColorValue())
                        .withInsertion(sender.getName().getString()));

        // ">" in white
        MutableText suffixBracket = Text.literal("> ");

        // golden mention
        MutableText mention = Text.literal(rawMessage.substring(mentionPos, mentionPos + mentionLen))
                .setStyle(Style.EMPTY.withColor(Formatting.GOLD));

        // rest of the message in white
        MutableText before = Text.literal(rawMessage.substring(0, mentionPos));
        MutableText after = Text.literal(rawMessage.substring(mentionPos + mentionLen));

        return prefix
                .append(name)
                .append(suffixBracket)
                .append(before)
                .append(mention)
                .append(after);
    }
}
