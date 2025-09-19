package org.daylight.coinscalculator.replacements;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;
import org.daylight.coinscalculator.ui.screens.ForgeModSettingsScreen;
import org.daylight.coinscalculator.ui.screens.ModSettingsScreen;

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
        return new ForgeModSettingsScreen((ModSettingsScreen) delegate);
    }

    @Override
    public boolean isModSettingsScreen() {
        return delegate instanceof ModSettingsScreen;
    }
}
