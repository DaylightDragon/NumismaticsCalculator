package org.daylight.coinscalculator.replacements;

import org.daylight.coinscalculator.config.SimpleConfig;

public class FabricBooleanConfigValue extends FabricConfigValue<Boolean> implements IBooleanConfigValue {
    public FabricBooleanConfigValue(SimpleConfig config, String key, Boolean defaultValue) {
        super(config, key, defaultValue);
    }
}
