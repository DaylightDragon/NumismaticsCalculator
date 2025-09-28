package org.daylight.numismaticscalculator.neoforge.replacements;

import org.daylight.numismaticscalculator.neoforge.ui.screens.NeoForgeModSettingsScreenImpl;
import org.daylight.numismaticscalculator.replacements.IAbstractContainerScreen;
import org.daylight.numismaticscalculator.replacements.IModSettingsScreen;

public class NeoForgeModSettingsScreen implements IModSettingsScreen {
    private final NeoForgeModSettingsScreenImpl delegate;

    public NeoForgeModSettingsScreen(NeoForgeModSettingsScreenImpl delegate) {
        this.delegate = delegate;
    }

    public NeoForgeModSettingsScreenImpl getDelegate() {
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
        return new NeoForgeModSettingsScreen(delegate);
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
