package com.hezaerd;

import com.hezaerd.config.SimpleMentionConfig;
import com.hezaerd.utils.DesktopNotifier;
import com.hezaerd.utils.ModLib;
import net.fabricmc.api.ClientModInitializer;

public class SimpleMentionClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        try {
            SimpleMentionConfig.register();
            ModLib.LOGGER.info("Simple Mention client initialized successfully");
        } catch (Exception e) {
            ModLib.LOGGER.error("Failed to initialize Simple Mention client", e);
        }
    }

    /**
     * Cleanup method to be called when the mod is being unloaded.
     * This ensures proper resource cleanup.
     */
    public static void cleanup() {
        try {
            DesktopNotifier.cleanup();
            ModLib.LOGGER.info("Simple Mention cleanup completed");
        } catch (Exception e) {
            ModLib.LOGGER.warn("Error during Simple Mention cleanup", e);
        }
    }
}
