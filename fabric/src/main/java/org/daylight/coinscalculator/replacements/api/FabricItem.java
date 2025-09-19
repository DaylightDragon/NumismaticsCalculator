package org.daylight.coinscalculator.replacements.api;

import net.minecraft.client.texture.Sprite;
import net.minecraft.item.Item;
import org.daylight.coinscalculator.replacements.IItem;
import org.daylight.coinscalculator.replacements.ITextureAtlasSprite;

public class FabricItem implements IItem {
    private Item delegate;
    public FabricItem(Item delegate) {
        this.delegate = delegate;
    }

    public Item getDelegate() {
        return delegate;
    }
}
