package org.daylight.coinscalculator.replacements.api;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.daylight.coinscalculator.replacements.IItem;
import org.daylight.coinscalculator.replacements.IItemStack;

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
