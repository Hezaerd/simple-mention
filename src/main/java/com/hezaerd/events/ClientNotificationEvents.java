package com.hezaerd.events;

import com.hezaerd.config.SimpleMentionConfig;
import com.hezaerd.registry.ModPackets;
import com.hezaerd.utils.DesktopNotifier;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.sound.SoundCategory;

import java.util.UUID;

public final class ClientNotificationEvents {
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(ModPackets.MENTION,
                (client, handler, buf, responseSender) -> {
                    UUID target = buf.readUuid();
                    client.execute(() -> {
                        if (client.player == null || !client.player.getUuid().equals(target)) return;

                        SimpleMentionConfig cfg = SimpleMentionConfig.get();
                        if (cfg.playSound) {
                            client.player.playSound(cfg.sound, SoundCategory.PLAYERS, 1.0F, 1.0F);
                        }
                        if (cfg.desktopNotification && !client.isWindowFocused()) {
                            DesktopNotifier.notify("MentionMod", "You were mentioned in chat!");
                        }
                    });
                });
    }
}
