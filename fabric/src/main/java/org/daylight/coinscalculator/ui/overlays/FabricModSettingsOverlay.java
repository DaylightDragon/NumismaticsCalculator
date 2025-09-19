package org.daylight.coinscalculator.ui.overlays;

import org.daylight.coinscalculator.config.ConfigHandler;
import org.daylight.coinscalculator.replacements.FabricBooleanConfigValue;
import org.daylight.coinscalculator.replacements.IBooleanConfigValue;
import org.daylight.coinscalculator.replacements.IIntConfigValue;

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

