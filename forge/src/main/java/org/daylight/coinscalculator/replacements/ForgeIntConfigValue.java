package org.daylight.coinscalculator.replacements;

import net.minecraftforge.common.ForgeConfigSpec;

public class ForgeIntConfigValue implements IIntConfigValue {
    private final ForgeConfigSpec.IntValue delegate;

    public ForgeIntConfigValue(ForgeConfigSpec.IntValue delegate) {
        this.delegate = delegate;
    }

    @Override
    public Integer get() {
        return delegate.get();
    }

    @Override
    public void set(Integer value) {
        delegate.set(value);
    }
}
