package org.daylight.numismaticscalculator.fabric.ui.overlays;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.daylight.numismaticscalculator.fabric.config.ConfigHandler;
import org.daylight.numismaticscalculator.fabric.replacements.api.FabricScreen;
import org.daylight.numismaticscalculator.fabric.ui.FabricModSettingsScreenImpl;
import org.daylight.numismaticscalculator.replacements.IScreen;
import org.daylight.numismaticscalculator.ui.overlays.IGuiManagerOverlay;

public class FabricGuiManagerOverlay extends IGuiManagerOverlay {
    @Override
    public boolean shouldRenderOnScreen(IScreen screenOriginal) {
        if(!(screenOriginal instanceof FabricScreen forgeScreen)) throw new IllegalArgumentException();
        Screen screen = forgeScreen.getDelegate();

        return (screen instanceof AbstractContainerScreen<?>);
    }

    @Override
    protected boolean shouldShowControlPanel() {
        return ConfigHandler.showControlPanel.get();
    }

    @Override
    protected void onOpenSettingsBtnClicked() {
        FabricModSettingsScreenImpl.setAsScreen();
    }
}

