package org.daylight.coinscalculator;

import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import org.daylight.coinscalculator.event.FabricScreenEvents;
import org.daylight.coinscalculator.event.FabricInventoryChangeEvents;
import org.daylight.coinscalculator.event.FabricTooltipEvents;
import org.daylight.coinscalculator.replacements.FabricCoinValues;
import org.daylight.coinscalculator.replacements.FabricDrawingUtils;
import org.daylight.coinscalculator.replacements.FabricInputUtils;
import org.daylight.coinscalculator.replacements.SingletonInstances;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoinsCalculator implements ClientModInitializer {
	public static final String MOD_ID = "numismaticscalculator";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		registerCommonSingletonInstances();
		ModKeyBindings.register();
		ModelLoadingPlugin.register(context -> FabricCoinValues.init());

		FabricScreenEvents.initializeScreenEvents();
		FabricInventoryChangeEvents.register();
		FabricTooltipEvents.register();
	}

	private void registerCommonSingletonInstances() {
		SingletonInstances.DRAWING_UTILS = new FabricDrawingUtils();
//		SingletonInstances.RENDER_SYSTEM = new FabricRenderSystem();
//		SingletonInstances.MOD_RESOURCES = new FabircResources();
//		SingletonInstances.EDITBOXES = new FabricEditBoxFactory();
//		SingletonInstances.COMPONENTS = new FabricComponentFactory();
		SingletonInstances.COIN_VALUES = new FabricCoinValues();
//		SingletonInstances.MINECRAFT_UTILS = new FabricMinecraftUtilities();
		SingletonInstances.INPUT_UTILS = new FabricInputUtils();

//		SingletonInstances.CALCULATOR_OVERLAY = new ForgeCalculatorOverlay();
//		SingletonInstances.GUI_MANAGER_OVERLAY = new ForgeGuiManagerOverlay();
//		SingletonInstances.MOD_SETTINGS_OVERLAY = new ForgeModSettingsOverlay();
	}
}