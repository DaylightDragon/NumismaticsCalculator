package org.daylight.numismaticscalculator.fabric.replacements.api;

import net.minecraft.item.Item;
import org.daylight.numismaticscalculator.replacements.IItem;

public class FabricItem implements IItem {
    private Item delegate;
    public FabricItem(Item delegate) {
        this.delegate = delegate;
    }

    public Item getDelegate() {
        return delegate;
    }
}
