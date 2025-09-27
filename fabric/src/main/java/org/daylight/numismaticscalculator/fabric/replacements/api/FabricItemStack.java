package org.daylight.numismaticscalculator.fabric.replacements.api;

import net.minecraft.item.ItemStack;
import org.daylight.numismaticscalculator.replacements.IItem;
import org.daylight.numismaticscalculator.replacements.IItemStack;

public class FabricItemStack implements IItemStack {
    private ItemStack delegate;
    public FabricItemStack(ItemStack delegate) {
        this.delegate = delegate;
    }

    public ItemStack getDelegate() {
        return delegate;
    }

    @Override
    public int getCount() {
        return delegate.getCount();
    }

    @Override
    public IItem getItem() {
        return new FabricItem(delegate.getItem());
    }
}
