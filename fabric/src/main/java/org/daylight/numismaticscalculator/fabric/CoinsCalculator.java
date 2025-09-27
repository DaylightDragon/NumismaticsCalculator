package org.daylight.numismaticscalculator.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import org.daylight.numismaticscalculator.fabric.config.ConfigHandler;
import org.daylight.numismaticscalculator.fabric.event.FabricInventoryChangeEvents;
import org.daylight.numismaticscalculator.fabric.event.FabricScreenEvents;
import org.daylight.numismaticscalculator.fabric.event.FabricTooltipEvents;
import org.daylight.numismaticscalculator.fabric.replacements.*;
import org.daylight.numismaticscalculator.fabric.replacements.api.FabricRenderSystem;
import org.daylight.numismaticscalculator.fabric.ui.overlays.FabricCalculatorOverlay;
import org.daylight.numismaticscalculator.fabric.ui.overlays.FabricGuiManagerOverlay;
import org.daylight.numismaticscalculator.fabric.ui.overlays.FabricModSettingsOverlay;
import org.daylight.numismaticscalculator.replacements.SingletonInstances;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoinsCalculator implements ClientModInitializer {
	public static final String MOD_ID = "numismaticscalculator";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		registerCommonSingletonInstances();
		ConfigHandler.init();
		ModKeyBindings.register();
		ModelLoadingPlugin.register(context -> FabricCoinValues.init());

		FabricScreenEvents.initializeScreenEvents();
		FabricInventoryChangeEvents.register();
		FabricTooltipEvents.register();
	}

	private void registerCommonSingletonInstances() {
		SingletonInstances.DRAWING_UTILS = new FabricDrawingUtils();
		SingletonInstances.RENDER_SYSTEM = new FabricRenderSystem();
		SingletonInstances.MOD_RESOURCES = new FabricResources();
		SingletonInstances.EDITBOXES = new FabricEditBoxFactory();
		SingletonInstances.COMPONENTS = new FabricComponentFactory();
		SingletonInstances.COIN_VALUES = new FabricCoinValues();
		SingletonInstances.MINECRAFT_UTILS = new FabricMinecraftUtilities();
		SingletonInstances.INPUT_UTILS = new FabricInputUtils();

		SingletonInstances.CALCULATOR_OVERLAY = new FabricCalculatorOverlay();
		SingletonInstances.GUI_MANAGER_OVERLAY = new FabricGuiManagerOverlay();
		SingletonInstances.MOD_SETTINGS_OVERLAY = new FabricModSettingsOverlay();
	}
}