package org.daylight.numismaticscalculator.neoforge.replacements.api;

import net.minecraft.world.item.Item;
import org.daylight.numismaticscalculator.replacements.IItem;

public class NeoForgeItem implements IItem {
    private final Item delegate;

    public NeoForgeItem(Item delegate) {
        this.delegate = delegate;
    }

    public Item getDelegate() {
        return delegate;
    }
}
