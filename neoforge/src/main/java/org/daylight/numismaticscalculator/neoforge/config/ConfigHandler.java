package org.daylight.numismaticscalculator.neoforge.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ConfigHandler {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

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
