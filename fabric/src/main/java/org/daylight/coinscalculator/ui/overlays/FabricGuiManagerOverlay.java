package org.daylight.coinscalculator.ui.overlays;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.daylight.coinscalculator.config.ConfigData;
import org.daylight.coinscalculator.replacements.IScreen;
import org.daylight.coinscalculator.replacements.api.FabricScreen;
import org.daylight.coinscalculator.ui.FabricModSettingsScreenImpl;

public class FabricGuiManagerOverlay extends IGuiManagerOverlay {
    @Override
    public boolean shouldRenderOnScreen(IScreen screenOriginal) {
        if(!(screenOriginal instanceof FabricScreen forgeScreen)) throw new IllegalArgumentException();
        Screen screen = forgeScreen.getDelegate();

        return (screen instanceof HandledScreen<?>);
    }

    @Override
    protected boolean shouldShowControlPanel() {
        return ConfigData.showControlPanel.get();
    }

    @Override
    protected void onOpenSettingsBtnClicked() {
        FabricModSettingsScreenImpl.setAsScreen();
    }
}

