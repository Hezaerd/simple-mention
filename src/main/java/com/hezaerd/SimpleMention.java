package com.hezaerd;

import com.hezaerd.utils.ModLib;
import net.fabricmc.api.ModInitializer;

public class SimpleMention implements ModInitializer {
	@Override
	public void onInitialize() {
		try {
			ModLib.LOGGER.info("Simple Mention mod initialized successfully");
		} catch (Exception e) {
			ModLib.LOGGER.error("Failed to initialize Simple Mention mod", e);
		}
	}
}