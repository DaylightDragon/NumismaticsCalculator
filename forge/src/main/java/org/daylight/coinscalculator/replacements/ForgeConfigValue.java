package org.daylight.coinscalculator.replacements;

import net.minecraftforge.common.ForgeConfigSpec;

public class ForgeConfigValue<T> implements IConfigValue<T> {

    private final ForgeConfigSpec.ConfigValue<T> delegate;

    public ForgeConfigValue(ForgeConfigSpec.ConfigValue<T> delegate) {
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

    public ForgeConfigSpec.ConfigValue<T> getDelegate() {
        return delegate;
    }
}
