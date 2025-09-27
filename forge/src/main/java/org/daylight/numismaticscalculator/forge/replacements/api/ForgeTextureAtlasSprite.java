package org.daylight.numismaticscalculator.forge.replacements.api;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.daylight.numismaticscalculator.replacements.ITextureAtlasSprite;

public class ForgeTextureAtlasSprite implements ITextureAtlasSprite {
    private final TextureAtlasSprite delegate;

    public ForgeTextureAtlasSprite(TextureAtlasSprite delegate) {
        this.delegate = delegate;
    }

    public TextureAtlasSprite getDelegate() {
        return delegate;
    }

    @Override
    public String asAtlasLocation() {
        return delegate.atlasLocation().toString();
    }
}
