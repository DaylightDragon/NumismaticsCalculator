package org.daylight.numismaticscalculator.replacements.api;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.daylight.numismaticscalculator.replacements.ForgeModSettingsScreen;
import org.daylight.numismaticscalculator.replacements.IAbstractContainerScreen;
import org.daylight.numismaticscalculator.replacements.IModSettingsScreen;
import org.daylight.numismaticscalculator.replacements.IScreen;
import org.daylight.numismaticscalculator.ui.screens.ForgeModSettingsScreenImpl;

public class ForgeScreen implements IScreen {
    private final Screen delegate;

    public ForgeScreen(Screen delegate) {
        this.delegate = delegate;
    }

    public Screen getDelegate() {
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
        return new ForgeModSettingsScreen((ForgeModSettingsScreenImpl) delegate);
    }

    @Override
    public boolean isAbstractContainerScreen() {
        return delegate instanceof AbstractContainerScreen<?>;
    }

    @Override
    public boolean isModSettingsScreen() {
        return delegate instanceof ForgeModSettingsScreenImpl;
    }
}
