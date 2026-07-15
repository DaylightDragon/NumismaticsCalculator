package org.daylight.numismaticscalculator.fabric.replacements.api;

import net.minecraft.resources.ResourceLocation;
import org.daylight.numismaticscalculator.replacements.IResourceLocation;
import org.jetbrains.annotations.NotNull;

public class FabricResourceLocation implements IResourceLocation {
    private ResourceLocation delegate;
    public FabricResourceLocation(ResourceLocation delegate) {
        this.delegate = delegate;
    }

    public ResourceLocation getDelegate() {
        return delegate;
    }

    @Override
    public @NotNull String getPath() {
        return delegate.getPath();
    }
}
