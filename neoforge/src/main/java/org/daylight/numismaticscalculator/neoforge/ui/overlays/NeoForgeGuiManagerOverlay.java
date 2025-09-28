package org.daylight.numismaticscalculator.neoforge.ui.overlays;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import org.daylight.numismaticscalculator.neoforge.config.ConfigData;
import org.daylight.numismaticscalculator.neoforge.replacements.api.NeoForgeScreen;
import org.daylight.numismaticscalculator.neoforge.ui.screens.NeoForgeModSettingsScreenImpl;
import org.daylight.numismaticscalculator.replacements.IScreen;
import org.daylight.numismaticscalculator.ui.overlays.IGuiManagerOverlay;

public class NeoForgeGuiManagerOverlay extends IGuiManagerOverlay {
    @Override
    public boolean shouldRenderOnScreen(IScreen screenOriginal) {
        if(!(screenOriginal instanceof NeoForgeScreen forgeScreen)) throw new IllegalArgumentException();
        Screen screen = forgeScreen.getDelegate();

        return (screen instanceof AbstractContainerScreen) || (screen instanceof CreativeModeInventoryScreen) || (screen instanceof InventoryScreen);
    }

    @Override
    protected boolean shouldShowControlPanel() {
        return ConfigData.showControlPanel.get();
    }

    @Override
    protected void onOpenSettingsBtnClicked() {
        NeoForgeModSettingsScreenImpl.setAsScreen();
    }
}
