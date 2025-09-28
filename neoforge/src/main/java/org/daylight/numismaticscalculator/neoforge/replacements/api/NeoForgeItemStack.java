package org.daylight.numismaticscalculator.neoforge.replacements.api;

import net.minecraft.world.item.ItemStack;
import org.daylight.numismaticscalculator.neoforge.replacements.api.NeoForgeItem;
import org.daylight.numismaticscalculator.replacements.IItem;
import org.daylight.numismaticscalculator.replacements.IItemStack;

public class NeoForgeItemStack implements IItemStack {
    private final ItemStack delegate;

    public NeoForgeItemStack(ItemStack delegate) {
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
        return new NeoForgeItem(delegate.getItem());
    }
}
