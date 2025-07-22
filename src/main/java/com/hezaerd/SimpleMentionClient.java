package com.hezaerd;

import com.hezaerd.config.SimpleMentionConfig;
import com.hezaerd.events.ClientNotificationEvents;
import net.fabricmc.api.ClientModInitializer;

public class SimpleMentionClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SimpleMentionConfig.register();
        ClientNotificationEvents.register();
    }
}
