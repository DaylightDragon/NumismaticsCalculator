package org.daylight.coinscalculator.replacements.api;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.daylight.coinscalculator.replacements.IAbstractContainerMenu;
import org.daylight.coinscalculator.replacements.IAbstractContainerScreen;
import org.daylight.coinscalculator.replacements.IModSettingsScreen;
import org.daylight.coinscalculator.replacements.ISlot;

public class ForgeAbstractContainerScreen<T> implements IAbstractContainerScreen<T> {
    private AbstractContainerScreen<?> delegate;

    public ForgeAbstractContainerScreen(AbstractContainerScreen<?> delegate) {
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
        return new ForgeAbstractContainerScreen<>((AbstractContainerScreen<?>) delegate);
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
        return new ForgeAbstractContainerMenu(delegate.getMenu());
    }

    @Override
    public ISlot getSlotUnderMouse() {
        return new ForgeSlot(delegate.getSlotUnderMouse());
    }

    @Override
    public boolean isAbstractContainerScreen() {
        return delegate instanceof AbstractContainerScreen<?>;
    }
}
