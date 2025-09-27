package org.daylight.numismaticscalculator.forge.replacements.api;

import net.minecraftforge.common.ForgeConfigSpec;
import org.daylight.numismaticscalculator.replacements.IBooleanConfigValue;

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
