package org.daylight.coinscalculator.replacements;

import net.minecraftforge.common.ForgeConfigSpec;

public class ForgeBooleanConfigValue implements IBooleanConfigValue {
    private final ForgeConfigSpec.BooleanValue delegate;

    public ForgeBooleanConfigValue(ForgeConfigSpec.BooleanValue delegate) {
        this.delegate = delegate;
    }

    @Override
    public Boolean get() {
        return delegate.get();
    }

    @Override
    public void set(Boolean value) {
        delegate.set(value);
    }
}
