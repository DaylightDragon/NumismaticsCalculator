package org.daylight.numismaticscalculator.neoforge.replacements.api;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.daylight.numismaticscalculator.replacements.ITextureAtlasSprite;

public class NeoForgeTextureAtlasSprite implements ITextureAtlasSprite {
    private final TextureAtlasSprite delegate;

    public NeoForgeTextureAtlasSprite(TextureAtlasSprite delegate) {
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
