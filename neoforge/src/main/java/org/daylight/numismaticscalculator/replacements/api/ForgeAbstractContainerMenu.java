package org.daylight.numismaticscalculator.replacements.api;

import net.minecraft.world.inventory.AbstractContainerMenu;
import org.daylight.numismaticscalculator.replacements.IAbstractContainerMenu;
import org.daylight.numismaticscalculator.replacements.ISlot;

import java.util.ArrayList;
import java.util.List;

public class ForgeAbstractContainerMenu implements IAbstractContainerMenu {
    private final AbstractContainerMenu delegate;

    public ForgeAbstractContainerMenu(AbstractContainerMenu delegate) {
        this.delegate = delegate;
    }

    public AbstractContainerMenu getDelegate() {
        return delegate;
    }

    @Override
    public List<ISlot> getSlots() {
        List<ISlot> slots = new ArrayList<>();
        delegate.slots.forEach(slot -> slots.add(new ForgeSlot(slot)));
        return slots;
    }
}
