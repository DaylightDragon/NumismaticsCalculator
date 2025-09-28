package org.daylight.numismaticscalculator.neoforge.replacements.api;

import net.minecraft.resources.ResourceLocation;
import org.daylight.numismaticscalculator.replacements.IResourceLocation;

public class NeoForgeResourceLocation implements IResourceLocation {
    private final ResourceLocation delegate;

    public NeoForgeResourceLocation(ResourceLocation delegate) {
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
