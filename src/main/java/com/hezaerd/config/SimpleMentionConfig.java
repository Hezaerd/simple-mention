package com.hezaerd.config;

import com.hezaerd.utils.ModLib;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

@Config(name = ModLib.MOD_ID)
public class SimpleMentionConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip
    public boolean playSound = true;

    @ConfigEntry.Gui.Tooltip
    public SoundEvent sound = SoundEvents.BLOCK_NOTE_BLOCK_PLING.value();

    @ConfigEntry.Gui.Tooltip
    public boolean desktopNotification = true;

    public static void register() {
        AutoConfig.register(SimpleMentionConfig.class, Toml4jConfigSerializer::new);
    }

    public static SimpleMentionConfig get() {
        return AutoConfig.getConfigHolder(SimpleMentionConfig.class).getConfig();
    }
}
