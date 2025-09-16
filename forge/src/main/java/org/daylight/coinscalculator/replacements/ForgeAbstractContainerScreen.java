package org.daylight.coinscalculator.replacements;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

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
}
