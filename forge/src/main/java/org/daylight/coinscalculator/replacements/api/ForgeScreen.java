package org.daylight.coinscalculator.replacements.api;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.daylight.coinscalculator.replacements.IAbstractContainerScreen;
import org.daylight.coinscalculator.replacements.IModSettingsScreen;
import org.daylight.coinscalculator.replacements.IScreen;
import org.daylight.coinscalculator.replacements.ForgeModSettingsScreen;
import org.daylight.coinscalculator.ui.screens.ForgeModSettingsScreenImpl;

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
        return new ForgeAbstractContainerScreen((AbstractContainerScreen<?>) delegate);
    }

    @Override
    public IModSettingsScreen getAsModSettingsScreen() {
        return new ForgeModSettingsScreen((ForgeModSettingsScreenImpl) delegate);
    }

    @Override
    public boolean isModSettingsScreen() {
        return delegate instanceof ForgeModSettingsScreenImpl;
    }
}
