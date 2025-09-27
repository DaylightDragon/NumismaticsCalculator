package org.daylight.numismaticscalculator.ui.overlays;

import org.daylight.numismaticscalculator.config.ConfigHandler;
import org.daylight.numismaticscalculator.replacements.IBooleanConfigValue;
import org.daylight.numismaticscalculator.replacements.IIntConfigValue;

public class FabricModSettingsOverlay extends IModSettingsOverlay {
    @Override
    protected IBooleanConfigValue getConfigRequireShiftForTotalTooltip() {
        return ConfigHandler.requireShiftForTotalTooltip;
    }

    @Override
    protected IBooleanConfigValue getConfigShowControlPanel() {
        return ConfigHandler.showControlPanel;
    }

    @Override
    protected IBooleanConfigValue getConfigOverlayAnimationEnabled() {
        return ConfigHandler.overlayAnimationEnabled;
    }

    @Override
    protected IIntConfigValue getConfigOverlayAnimationDuration() {
        return ConfigHandler.overlayAnimationDuration;
    }
}

