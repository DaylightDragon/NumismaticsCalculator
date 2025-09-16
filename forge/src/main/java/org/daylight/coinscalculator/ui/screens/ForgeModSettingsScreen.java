package org.daylight.coinscalculator.ui.screens;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import org.daylight.coinscalculator.replacements.ForgeAbstractContainerScreen;
import org.daylight.coinscalculator.replacements.IAbstractContainerScreen;
import org.daylight.coinscalculator.replacements.IModSettingsScreen;

public class ForgeModSettingsScreen implements IModSettingsScreen {
    private final ModSettingsScreen delegate;

    public ForgeModSettingsScreen(ModSettingsScreen delegate) {
        this.delegate = delegate;
    }

    public ModSettingsScreen getDelegate() {
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
        throw new UnsupportedOperationException("This screen isn't a AbstractContainerScreen");
    }
}
