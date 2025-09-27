package org.daylight.numismaticscalculator.replacements.api;

import net.minecraft.resources.ResourceLocation;
import org.daylight.numismaticscalculator.replacements.IResourceLocation;

public class ForgeResourceLocation implements IResourceLocation {
    private final ResourceLocation delegate;

    public ForgeResourceLocation(ResourceLocation delegate) {
        this.delegate = delegate;
    }

    public ResourceLocation getDelegate() {
        return delegate;
    }

    @Override
    public String getPath() {
        return delegate.toString();
    }
}
