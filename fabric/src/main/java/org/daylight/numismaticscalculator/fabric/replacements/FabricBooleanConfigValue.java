package org.daylight.numismaticscalculator.fabric.replacements;

import org.daylight.numismaticscalculator.fabric.config.SimpleConfig;
import org.daylight.numismaticscalculator.replacements.IBooleanConfigValue;

public class FabricBooleanConfigValue extends FabricConfigValue<Boolean> implements IBooleanConfigValue {
    public FabricBooleanConfigValue(SimpleConfig config, String key, Boolean defaultValue) {
        super(config, key, defaultValue);
    }
}
