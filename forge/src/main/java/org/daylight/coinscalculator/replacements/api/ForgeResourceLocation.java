package org.daylight.coinscalculator.replacements.api;

import net.minecraft.resources.ResourceLocation;
import org.daylight.coinscalculator.replacements.IResourceLocation;

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
