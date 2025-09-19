package org.daylight.coinscalculator.replacements;

import org.daylight.coinscalculator.config.SimpleConfig;

public class FabricIntegerConfigValue extends FabricConfigValue<Integer> implements IIntConfigValue {
    public FabricIntegerConfigValue(SimpleConfig config, String key, Integer defaultValue) {
        super(config, key, defaultValue);
    }
}
