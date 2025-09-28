package org.daylight.numismaticscalculator.neoforge.replacements.api;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.daylight.numismaticscalculator.replacements.IIntConfigValue;

public class NeoForgeIntConfigValue implements IIntConfigValue {
    private final ModConfigSpec.IntValue delegate;

    public NeoForgeIntConfigValue(ModConfigSpec.IntValue delegate) {
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
