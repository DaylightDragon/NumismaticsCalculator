package org.daylight.numismaticscalculator.replacements.api;

import net.minecraft.world.item.ItemStack;
import org.daylight.numismaticscalculator.replacements.IItem;
import org.daylight.numismaticscalculator.replacements.IItemStack;

public class ForgeItemStack implements IItemStack {
    private final ItemStack delegate;

    public ForgeItemStack(ItemStack delegate) {
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
        return new ForgeItem(delegate.getItem());
    }
}
