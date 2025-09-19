package org.daylight.coinscalculator.replacements.api;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.screen.slot.Slot;
import org.daylight.coinscalculator.replacements.IFont;
import org.daylight.coinscalculator.replacements.IItem;
import org.daylight.coinscalculator.replacements.IItemStack;
import org.daylight.coinscalculator.replacements.ISlot;
import org.jetbrains.annotations.NotNull;

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
