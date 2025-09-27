package org.daylight.numismaticscalculator.fabric.replacements.api;

import net.minecraft.screen.slot.Slot;
import org.daylight.numismaticscalculator.replacements.IItem;
import org.daylight.numismaticscalculator.replacements.IItemStack;
import org.daylight.numismaticscalculator.replacements.ISlot;

public class FabricSlot implements ISlot {
    private Slot delegate;
    public FabricSlot(Slot delegate) {
        this.delegate = delegate;
    }

    public Slot getDelegate() {
        return delegate;
    }

    @Override
    public IItem getItemActual() {
        return new FabricItem(delegate.getStack().getItem());
    }

    @Override
    public IItemStack getItemStack() {
        return new FabricItemStack(delegate.getStack());
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
        return delegate.inventory.getClass();
    }
}
