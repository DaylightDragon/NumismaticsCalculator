package org.daylight.numismaticscalculator.fabric.replacements.api;

import net.minecraft.world.inventory.AbstractContainerMenu;
import org.daylight.numismaticscalculator.replacements.IAbstractContainerMenu;
import org.daylight.numismaticscalculator.replacements.ISlot;

import java.util.ArrayList;
import java.util.List;

public class FabricAbstractContainerMenu implements IAbstractContainerMenu {
    private AbstractContainerMenu delegate;

    public FabricAbstractContainerMenu(AbstractContainerMenu delegate) {
        this.delegate = delegate;
    }

    private AbstractContainerMenu getDelegate() {
        return delegate;
    }

    @Override
    public List<ISlot> getSlots() {
        List<ISlot> slots = new ArrayList<>();
        delegate.slots.forEach(slot -> slots.add(new FabricSlot(slot)));
        return slots;
    }
}
