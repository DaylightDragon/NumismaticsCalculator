package org.daylight.coinscalculator.replacements;

import net.minecraft.client.gui.Font;
import net.minecraft.world.item.Item;

public class ForgeItem implements IItem {
    private final Item delegate;

    public ForgeItem(Item delegate) {
        this.delegate = delegate;
    }

    public Item getDelegate() {
        return delegate;
    }
}
