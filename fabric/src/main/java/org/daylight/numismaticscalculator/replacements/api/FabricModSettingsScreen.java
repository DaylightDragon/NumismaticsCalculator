package org.daylight.numismaticscalculator.replacements.api;

import org.daylight.numismaticscalculator.replacements.IAbstractContainerScreen;
import org.daylight.numismaticscalculator.replacements.IModSettingsScreen;
import org.daylight.numismaticscalculator.ui.FabricModSettingsScreenImpl;

public class FabricModSettingsScreen implements IModSettingsScreen {
    private FabricModSettingsScreenImpl delegate;

    public FabricModSettingsScreen(FabricModSettingsScreenImpl delegate) {
        this.delegate = delegate;
    }

    public FabricModSettingsScreenImpl getDelegate() {
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
        throw new UnsupportedOperationException("This is not an AbstractContainerScreen");
    }

    @Override
    public boolean isModSettingsScreen() {
        return false;
    }

    @Override
    public IModSettingsScreen getAsModSettingsScreen() {
        return this;
    }

    @Override
    public boolean isAbstractContainerScreen() {
        return false;
    }
}
