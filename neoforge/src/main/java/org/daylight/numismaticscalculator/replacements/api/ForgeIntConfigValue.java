package org.daylight.numismaticscalculator.replacements.api;

import net.minecraftforge.common.ForgeConfigSpec;
import org.daylight.numismaticscalculator.replacements.IIntConfigValue;

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
