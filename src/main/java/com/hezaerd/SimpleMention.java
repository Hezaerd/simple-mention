package com.hezaerd;

import com.hezaerd.registry.ModPackets;
import com.hezaerd.utils.ModLib;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleMention implements ModInitializer {
	@Override
	public void onInitialize() {
		ServerMessageEvents.CHAT_MESSAGE.register((message, sender, params) -> {
			if (!(sender instanceof ServerPlayerEntity)) return;
			if (sender.getServer() == null) return;
			MinecraftServer server = sender.getServer();
			PlayerManager playerManager = server.getPlayerManager();
			
			String rawMsg = message.getContent().getString();
			Matcher matcher = Pattern.compile("@(\\w{3,16})").matcher(rawMsg);
			if (!matcher.find()) return;
			
			String targetName = matcher.group(1);
			ServerPlayerEntity target = playerManager.getPlayer(targetName);
			if (target == null) return;

			PacketByteBuf buf = PacketByteBufs.create();
			buf.writeUuid(target.getUuid());
			ServerPlayNetworking.send(target, ModPackets.MENTION_PACKET, buf);

			MutableText newText = Text.literal("<" + sender.getName().getString() + "> ")
					.setStyle(Style.EMPTY.withColor(Formatting.GRAY))
					.append(Text.literal("@" + targetName)
							.setStyle(Style.EMPTY.withColor(Formatting.DARK_PURPLE).withBold(true)))
					.append(Text.literal(rawMsg.substring(matcher.end())));
			
			ModLib.LOGGER.info("Player {} mentioned {}", sender.getName().getString(), targetName);
			
			playerManager.broadcast(newText, false);
		});

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayNetworking.registerReceiver(handler, ModPackets.MENTION_PACKET,
					(server1, player, handler1, buf, responseSender) -> {});
		});

		// Register content
		ModPackets.register();
	}
}