package org.daylight.coinscalculator.replacements.api;

import net.minecraft.client.texture.Sprite;
import org.daylight.coinscalculator.replacements.ITextureAtlasSprite;

public class FabricTextureAtlasSprite implements ITextureAtlasSprite {
    private Sprite delegate;
    public FabricTextureAtlasSprite(Sprite delegate) {
        this.delegate = delegate;
    }

    public Sprite getDelegate() {
        return delegate;
    }

    @Override
    public String asAtlasLocation() {
        return delegate.getAtlasId().toString();
    }
}
