package org.daylight.numismaticscalculator.neoforge.replacements.api;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.daylight.numismaticscalculator.neoforge.replacements.NeoForgeModSettingsScreen;
import org.daylight.numismaticscalculator.neoforge.ui.screens.NeoForgeModSettingsScreenImpl;
import org.daylight.numismaticscalculator.replacements.IAbstractContainerScreen;
import org.daylight.numismaticscalculator.replacements.IModSettingsScreen;
import org.daylight.numismaticscalculator.replacements.IScreen;

public class NeoForgeScreen implements IScreen {
    private final Screen delegate;

    public NeoForgeScreen(Screen delegate) {
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
        return new NeoForgeAbstractContainerScreen<>((AbstractContainerScreen<?>) delegate);
    }

    @Override
    public IModSettingsScreen getAsModSettingsScreen() {
        return new NeoForgeModSettingsScreen((NeoForgeModSettingsScreenImpl) delegate);
    }

    @Override
    public boolean isAbstractContainerScreen() {
        return delegate instanceof AbstractContainerScreen<?>;
    }

    @Override
    public boolean isModSettingsScreen() {
        return delegate instanceof NeoForgeModSettingsScreenImpl;
    }
}
