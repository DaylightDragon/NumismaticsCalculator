package org.daylight.numismaticscalculator.ui.overlays;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import org.daylight.numismaticscalculator.config.ConfigData;
import org.daylight.numismaticscalculator.replacements.api.ForgeScreen;
import org.daylight.numismaticscalculator.replacements.IScreen;
import org.daylight.numismaticscalculator.ui.screens.ForgeModSettingsScreenImpl;

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
