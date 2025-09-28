package org.daylight.numismaticscalculator.neoforge.replacements.api;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.daylight.numismaticscalculator.replacements.IConfigValue;

public class NeoForgeConfigValue<T> implements IConfigValue<T> {

    private final ModConfigSpec.ConfigValue<T> delegate;

    public NeoForgeConfigValue(ModConfigSpec.ConfigValue<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public T get() {
        return delegate.get();
    }

    @Override
    public void set(T value) {
        delegate.set(value);
    }

    public ModConfigSpec.ConfigValue<T> getDelegate() {
        return delegate;
    }
}
