package org.daylight.coinscalculator;

import net.fabricmc.api.ModInitializer;

import org.daylight.coinscalculator.replacements.ITextureAtlasSprite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoinsCalculator implements ModInitializer {
	public static final String MOD_ID = "numismaticscalculator";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");
		ModKeyBindings.register();
		ITextureAtlasSprite sprite = new ITextureAtlasSprite() {
			@Override
			public String asAtlasLocation() {
				return "";
			}
		};
	}
}