package org.daylight.numismaticscalculator.replacements.api;

import net.minecraft.world.item.Item;
import org.daylight.numismaticscalculator.replacements.IItem;

public class ForgeItem implements IItem {
    private final Item delegate;

    public ForgeItem(Item delegate) {
        this.delegate = delegate;
    }

    public Item getDelegate() {
        return delegate;
    }
}
