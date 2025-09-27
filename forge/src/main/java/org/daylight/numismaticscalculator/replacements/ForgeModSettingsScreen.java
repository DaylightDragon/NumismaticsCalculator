package org.daylight.numismaticscalculator.replacements;

import org.daylight.numismaticscalculator.ui.screens.ForgeModSettingsScreenImpl;

public class ForgeModSettingsScreen implements IModSettingsScreen {
    private final ForgeModSettingsScreenImpl delegate;

    public ForgeModSettingsScreen(ForgeModSettingsScreenImpl delegate) {
        this.delegate = delegate;
    }

    public ForgeModSettingsScreenImpl getDelegate() {
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
        throw new UnsupportedOperationException("This screen isn't an AbstractContainerScreen");
    }

    @Override
    public IModSettingsScreen getAsModSettingsScreen() {
        return new ForgeModSettingsScreen(delegate);
    }

    @Override
    public boolean isAbstractContainerScreen() {
        return false;
    }

    @Override
    public boolean isModSettingsScreen() {
        return true;
    }
}
