package org.daylight.numismaticscalculator.fabric.ui.overlays;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
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

        return (screen instanceof HandledScreen<?>);
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

