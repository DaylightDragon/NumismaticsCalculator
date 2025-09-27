package org.daylight.numismaticscalculator.fabric.replacements.api;

import net.minecraft.screen.ScreenHandler;
import org.daylight.numismaticscalculator.replacements.IAbstractContainerMenu;
import org.daylight.numismaticscalculator.replacements.ISlot;

import java.util.ArrayList;
import java.util.List;

public class FabricAbstractContainerMenu implements IAbstractContainerMenu {
    private ScreenHandler delegate;

    public FabricAbstractContainerMenu(ScreenHandler delegate) {
        this.delegate = delegate;
    }

    private ScreenHandler getDelegate() {
        return delegate;
    }

    @Override
    public List<ISlot> getSlots() {
        List<ISlot> slots = new ArrayList<>();
        delegate.slots.forEach(slot -> slots.add(new FabricSlot(slot)));
        return slots;
    }
}
