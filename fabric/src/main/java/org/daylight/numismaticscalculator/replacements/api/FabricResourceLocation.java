package org.daylight.numismaticscalculator.replacements.api;

import net.minecraft.util.Identifier;
import org.daylight.numismaticscalculator.replacements.IResourceLocation;
import org.jetbrains.annotations.NotNull;

public class FabricResourceLocation implements IResourceLocation {
    private Identifier delegate;
    public FabricResourceLocation(Identifier delegate) {
        this.delegate = delegate;
    }

    public Identifier getDelegate() {
        return delegate;
    }

    @Override
    public @NotNull String getPath() {
        return delegate.getPath();
    }
}
