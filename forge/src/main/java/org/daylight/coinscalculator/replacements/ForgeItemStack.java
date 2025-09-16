package org.daylight.coinscalculator.replacements;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

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
