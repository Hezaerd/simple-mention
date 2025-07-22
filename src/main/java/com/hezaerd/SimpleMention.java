package com.hezaerd;

import com.hezaerd.events.ServerChatEvent;
import com.hezaerd.registry.ModPackets;
import net.fabricmc.api.ModInitializer;

public class SimpleMention implements ModInitializer {
	@Override
	public void onInitialize() {
		ModPackets.register();
		ServerChatEvent.register();
	}
}