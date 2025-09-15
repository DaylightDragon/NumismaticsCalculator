package org.daylight.coinscalculator.replacements;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.daylight.coinscalculator.replacements.ITextureAtlasSprite;

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
