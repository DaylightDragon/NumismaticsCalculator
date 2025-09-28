package org.daylight.numismaticscalculator.neoforge.replacements.api;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.daylight.numismaticscalculator.replacements.IBooleanConfigValue;

public class NeoForgeBooleanConfigValue implements IBooleanConfigValue {
    private final ModConfigSpec.BooleanValue delegate;

    public NeoForgeBooleanConfigValue(ModConfigSpec.BooleanValue delegate) {
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
