package com.shimaper.enchanteverything;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnchantEverything implements ModInitializer {
	public static final String MOD_ID = "enchant-everything";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static int enchantabilityValue = 10;

	@Override
	public void onInitialize() {
        enchantabilityValue = 10;
	}
}