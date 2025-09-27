package org.daylight.numismaticscalculator.ui.overlays;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.daylight.numismaticscalculator.config.ConfigHandler;
import org.daylight.numismaticscalculator.replacements.IScreen;
import org.daylight.numismaticscalculator.replacements.api.FabricScreen;
import org.daylight.numismaticscalculator.ui.FabricModSettingsScreenImpl;

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

