package com.hezaerd.utils;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModLib {
    public static final String MOD_ID = "simple-mention";
    public static final String MOD_NAME = "Simple Mention";
    
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }
}
