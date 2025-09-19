package org.daylight.coinscalculator.replacements;

import org.daylight.coinscalculator.config.SimpleConfig;

public class FabricConfigValue<T> implements IConfigValue<T> {
    private final SimpleConfig config;
    private final String key;
    private final T defaultValue;

    public FabricConfigValue(SimpleConfig config, String key, T defaultValue) {
        this.config = config;
        this.key = key;
        this.defaultValue = defaultValue;
    }

    @Override
    public T get() {
        return config.get(key, defaultValue);
    }

    @Override
    public void set(T value) {
        config.set(key, value);
    }

    public void save() {
        config.save();
    }
}
