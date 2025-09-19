package org.daylight.coinscalculator.ui.overlays;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import org.daylight.coinscalculator.config.ConfigData;
import org.daylight.coinscalculator.replacements.api.ForgeScreen;
import org.daylight.coinscalculator.replacements.IScreen;
import org.daylight.coinscalculator.ui.screens.ForgeModSettingsScreenImpl;

public class ForgeGuiManagerOverlay extends IGuiManagerOverlay {
    @Override
    public boolean shouldRenderOnScreen(IScreen screenOriginal) {
        if(!(screenOriginal instanceof ForgeScreen forgeScreen)) throw new IllegalArgumentException();
        Screen screen = forgeScreen.getDelegate();

        return (screen instanceof AbstractContainerScreen) || (screen instanceof CreativeModeInventoryScreen) || (screen instanceof InventoryScreen);
    }

    @Override
    protected boolean shouldShowControlPanel() {
        return ConfigData.showControlPanel.get();
    }

    @Override
    protected void onOpenSettingsBtnClicked() {
        ForgeModSettingsScreenImpl.setAsScreen();
    }
}
