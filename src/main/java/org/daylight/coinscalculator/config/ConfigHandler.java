package org.daylight.coinscalculator.config;

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

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
