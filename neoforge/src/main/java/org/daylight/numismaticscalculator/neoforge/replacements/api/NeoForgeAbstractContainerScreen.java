package org.daylight.numismaticscalculator.neoforge.replacements.api;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.daylight.numismaticscalculator.replacements.IAbstractContainerMenu;
import org.daylight.numismaticscalculator.replacements.IAbstractContainerScreen;
import org.daylight.numismaticscalculator.replacements.IModSettingsScreen;
import org.daylight.numismaticscalculator.replacements.ISlot;

public class NeoForgeAbstractContainerScreen<T> implements IAbstractContainerScreen<T> {
    private AbstractContainerScreen<?> delegate;

    public NeoForgeAbstractContainerScreen(AbstractContainerScreen<?> delegate) {
        this.delegate = delegate;
    }

    public AbstractContainerScreen<?> getDelegate() {
        return delegate;
    }

    @Override
    public int width() {
        return delegate.width;
    }

    @Override
    public int height() {
        return delegate.height;
    }

    @Override
    public IAbstractContainerScreen<?> getAsAbstractContainerScreen() {
        return new NeoForgeAbstractContainerScreen<>((AbstractContainerScreen<?>) delegate);
    }

    @Override
    public IModSettingsScreen getAsModSettingsScreen() {
        throw new UnsupportedOperationException("This screen isn't a ModSettingsScreen");
    }

    @Override
    public boolean isModSettingsScreen() {
        return false;
    }

    public int getGuiLeft() {
        return delegate.getGuiLeft();
    }

    public int getGuiTop() {
        return delegate.getGuiTop();
    }

    @Override
    public int countSlots() {
        return delegate.getMenu().slots.size();
    }

    @Override
    public IAbstractContainerMenu getMenu() {
        return new NeoForgeAbstractContainerMenu(delegate.getMenu());
    }

    @Override
    public ISlot getSlotUnderMouse() {
        return new NeoForgeSlot(delegate.getSlotUnderMouse());
    }

    @Override
    public boolean isAbstractContainerScreen() {
        return delegate instanceof AbstractContainerScreen<?>;
    }
}
