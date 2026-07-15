package org.daylight.numismaticscalculator.fabric.replacements.api;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.daylight.numismaticscalculator.replacements.ITextureAtlasSprite;

public class FabricTextureAtlasSprite implements ITextureAtlasSprite {
    private TextureAtlasSprite delegate;
    public FabricTextureAtlasSprite(TextureAtlasSprite delegate) {
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
