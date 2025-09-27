package org.daylight.numismaticscalculator.fabric.config;

import org.daylight.numismaticscalculator.fabric.replacements.FabricBooleanConfigValue;
import org.daylight.numismaticscalculator.fabric.replacements.FabricIntegerConfigValue;
import org.daylight.numismaticscalculator.replacements.IBooleanConfigValue;
import org.daylight.numismaticscalculator.replacements.IIntConfigValue;

public class ConfigHandler {
    public static final SimpleConfig CONFIG = new SimpleConfig();

    public static IBooleanConfigValue requireShiftForTotalTooltip;
    public static IBooleanConfigValue showControlPanel;
    public static IBooleanConfigValue overlayAnimationEnabled;
    public static IIntConfigValue overlayAnimationDuration;

    public static void init() {
        CONFIG.load();

        requireShiftForTotalTooltip = new FabricBooleanConfigValue(CONFIG, "requireShiftForTotalTooltip", true);
        showControlPanel = new FabricBooleanConfigValue(CONFIG, "showControlPanel", true);
        overlayAnimationEnabled = new FabricBooleanConfigValue(CONFIG, "overlayAnimationEnabled", true);
        overlayAnimationDuration = new FabricIntegerConfigValue(CONFIG, "overlayAnimationDuration", 150);
    }
}
