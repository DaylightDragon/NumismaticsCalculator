package org.daylight.numismaticscalculator.forge.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    static {
        BUILDER.comment("Numismatics Calculator Config").push("general");

        ConfigData.requireShiftForTotalTooltip = BUILDER.comment("Require Holding Down SHIFT For Total Value Tooltip")
                .define("requireShiftForTotalTooltip", true);

        ConfigData.showControlPanel = BUILDER.comment("Show Control Panel Buttons")
                .define("showControlPanel", true);

        ConfigData.overlayAnimationEnabled = BUILDER.comment("Main Overlay Animation Enabled")
                .define("mainOverlayPositionAnimationEnabled", true);

        ConfigData.overlayAnimationDuration = BUILDER.comment("Main Overlay Animation Duration")
                        .defineInRange("mainOverlayPositionAnimationDuration", 150, 0, 10000);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
