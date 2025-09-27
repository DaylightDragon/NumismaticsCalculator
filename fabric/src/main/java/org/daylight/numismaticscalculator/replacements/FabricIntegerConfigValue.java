package org.daylight.numismaticscalculator.replacements;

import org.daylight.numismaticscalculator.config.SimpleConfig;

public class FabricIntegerConfigValue extends FabricConfigValue<Integer> implements IIntConfigValue {
    public FabricIntegerConfigValue(SimpleConfig config, String key, Integer defaultValue) {
        super(config, key, defaultValue);
    }
}
