package org.daylight.numismaticscalculator.fabric.replacements.api;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.daylight.numismaticscalculator.fabric.ui.FabricModSettingsScreenImpl;
import org.daylight.numismaticscalculator.replacements.IAbstractContainerScreen;
import org.daylight.numismaticscalculator.replacements.IModSettingsScreen;
import org.daylight.numismaticscalculator.replacements.IScreen;

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
        return new FabricAbstractContainerScreen<>((HandledScreen<?>) delegate);
    }

    @Override
    public boolean isModSettingsScreen() {
        return delegate instanceof FabricModSettingsScreenImpl;
    }

    @Override
    public IModSettingsScreen getAsModSettingsScreen() {
        return new FabricModSettingsScreen((FabricModSettingsScreenImpl) delegate);
    }

    @Override
    public boolean isAbstractContainerScreen() {
        return delegate instanceof HandledScreen<?>;
    }
}
