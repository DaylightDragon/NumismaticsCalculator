package org.daylight.coinscalculator.replacements.api;

import net.minecraft.client.gui.screen.Screen;
import org.daylight.coinscalculator.replacements.IAbstractContainerScreen;
import org.daylight.coinscalculator.replacements.IModSettingsScreen;
import org.daylight.coinscalculator.replacements.IScreen;
import org.daylight.coinscalculator.ui.FabricModSettingsScreenImpl;
import org.daylight.coinscalculator.ui.overlays.FabricModSettingsOverlay;

public class FabricScreen implements IScreen {
    private Screen delegate;

    public FabricScreen(Screen delegate) {
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
        return (IAbstractContainerScreen<?>) delegate;
    }

    @Override
    public boolean isModSettingsScreen() {
        return delegate instanceof FabricModSettingsScreenImpl;
    }

    @Override
    public IModSettingsScreen getAsModSettingsScreen() {
        return new FabricModSettingsScreen((FabricModSettingsScreenImpl) delegate);
    }
}
