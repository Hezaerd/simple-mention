package com.hezaerd;

import com.hezaerd.registry.ModPackets;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.sound.SoundEvents;

import java.util.UUID;

public class SimpleMentionClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(ModPackets.MENTION_PACKET,
                (client, handler, buf, responseSender) -> {
                    UUID who = buf.readUuid();
                    client.execute(() -> {
                        System.out.println("You have been mentioned!");
                        if (client.player != null && client.player.getUuid().equals(who)) {
                            client.player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_PLING.value(), 1.0F, 2.0F);
                        }
                    });
                }
        );
    }
}
