package org.daylight.numismaticscalculator.fabric.replacements;

import org.daylight.numismaticscalculator.fabric.config.SimpleConfig;
import org.daylight.numismaticscalculator.replacements.IIntConfigValue;

public class FabricIntegerConfigValue extends FabricConfigValue<Integer> implements IIntConfigValue {
    public FabricIntegerConfigValue(SimpleConfig config, String key, Integer defaultValue) {
        super(config, key, defaultValue);
    }
}
