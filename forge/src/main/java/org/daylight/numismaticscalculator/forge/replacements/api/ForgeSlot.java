package org.daylight.numismaticscalculator.forge.replacements.api;

import net.minecraft.world.inventory.Slot;
import org.daylight.numismaticscalculator.replacements.IItem;
import org.daylight.numismaticscalculator.replacements.IItemStack;
import org.daylight.numismaticscalculator.replacements.ISlot;

public class ForgeSlot implements ISlot {
    private final Slot delegate;

    public ForgeSlot(Slot delegate) {
        this.delegate = delegate;
    }

    public Slot getDelegate() {
        return delegate;
    }

    @Override
    public IItem getItemActual() {
        return new ForgeItem(delegate.getItem().getItem());
    }

    @Override
    public IItemStack getItemStack() {
        return new ForgeItemStack(delegate.getItem());
    }

    @Override
    public int x() {
        return delegate.x;
    }

    @Override
    public int y() {
        return delegate.y;
    }

    @Override
    public Class<?> getContainerClass() {
        return delegate.container.getClass();
    }
}
