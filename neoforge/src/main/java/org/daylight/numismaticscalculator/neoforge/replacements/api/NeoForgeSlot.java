package org.daylight.numismaticscalculator.neoforge.replacements.api;

import net.minecraft.world.inventory.Slot;
import org.daylight.numismaticscalculator.replacements.IItem;
import org.daylight.numismaticscalculator.replacements.IItemStack;
import org.daylight.numismaticscalculator.replacements.ISlot;

public class NeoForgeSlot implements ISlot {
    private final Slot delegate;

    public NeoForgeSlot(Slot delegate) {
        this.delegate = delegate;
    }

    public Slot getDelegate() {
        return delegate;
    }

    @Override
    public IItem getItemActual() {
        return new NeoForgeItem(delegate.getItem().getItem());
    }

    @Override
    public IItemStack getItemStack() {
        return new NeoForgeItemStack(delegate.getItem());
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
