package com.hezaerd.events;

import com.hezaerd.registry.ModPackets;
import com.hezaerd.utils.MentionFormatter;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ServerChatEvent {
    public static final Pattern MENTION_PATTERN = Pattern.compile("@(\\w{3,16})");
    
    public static void register() {
        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register((signedMessage, sender, params) -> {
            if (sender.getServer() == null) return true;
            
            String raw = signedMessage.getContent().getString();
            Matcher m = MENTION_PATTERN.matcher(raw);
            if (!m.find()) return true;

            String targetName = m.group(1);
            ServerPlayerEntity target = sender.getServer().getPlayerManager().getPlayer(targetName);
            if (target == null) return true;

            MutableText formatted = MentionFormatter.format(sender, raw, m.start(), m.group().length());

            for (ServerPlayerEntity p : sender.getServer().getPlayerManager().getPlayerList()) {
                if (p == target) {
                    p.sendMessage(formatted, false);
                } else {
                    p.sendMessage(signedMessage.unsignedContent(), false);
                }
            }

            // Ping packet
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeUuid(target.getUuid());
            ServerPlayNetworking.send(target, ModPackets.MENTION, buf);
            
            return false;
        });
    }
    
}
