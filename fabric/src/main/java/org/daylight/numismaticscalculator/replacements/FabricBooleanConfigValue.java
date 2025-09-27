package org.daylight.numismaticscalculator.replacements;

import org.daylight.numismaticscalculator.config.SimpleConfig;

public class FabricBooleanConfigValue extends FabricConfigValue<Boolean> implements IBooleanConfigValue {
    public FabricBooleanConfigValue(SimpleConfig config, String key, Boolean defaultValue) {
        super(config, key, defaultValue);
    }
}
