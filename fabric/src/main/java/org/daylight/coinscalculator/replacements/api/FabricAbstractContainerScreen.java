package org.daylight.coinscalculator.replacements.api;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.daylight.coinscalculator.replacements.IAbstractContainerMenu;
import org.daylight.coinscalculator.replacements.IAbstractContainerScreen;
import org.daylight.coinscalculator.replacements.IModSettingsScreen;
import org.daylight.coinscalculator.replacements.ISlot;

public class FabricAbstractContainerScreen implements IAbstractContainerScreen {
    private HandledScreen<?> delegate;

    public FabricAbstractContainerScreen(HandledScreen<?> delegate) {
        this.delegate = delegate;
    }

    public HandledScreen<?> getDelegate() {
        return delegate;
    }

    @Override
    public int getGuiLeft() {
        return delegate.getNavigationFocus().getLeft();
    }

    @Override
    public int getGuiTop() {
        return delegate.getNavigationFocus().getTop();
    }

    @Override
    public int countSlots() {
        return delegate.getScreenHandler().slots.size();
    }

    @Override
    public IAbstractContainerMenu getMenu() {
        return new FabricAbstractContainerMenu(delegate.getScreenHandler());
    }

    @Override
    public ISlot getSlotUnderMouse() {
        return null;
    }

    @Override
    public int width() {
        return 0;
    }

    @Override
    public int height() {
        return 0;
    }

    @Override
    public IAbstractContainerScreen<?> getAsAbstractContainerScreen() {
        return null;
    }

    @Override
    public boolean isModSettingsScreen() {
        return false;
    }

    @Override
    public IModSettingsScreen getAsModSettingsScreen() {
        return null;
    }
}
