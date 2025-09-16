package org.daylight.coinscalculator.replacements;

import net.minecraft.resources.ResourceLocation;

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
