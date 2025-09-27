package org.daylight.numismaticscalculator.ui.overlays;

import org.daylight.numismaticscalculator.config.ConfigData;
import org.daylight.numismaticscalculator.replacements.IBooleanConfigValue;
import org.daylight.numismaticscalculator.replacements.IIntConfigValue;
import org.daylight.numismaticscalculator.replacements.api.ForgeBooleanConfigValue;
import org.daylight.numismaticscalculator.replacements.api.ForgeIntConfigValue;

public class ForgeModSettingsOverlay extends IModSettingsOverlay {
    @Override
    protected IBooleanConfigValue getConfigRequireShiftForTotalTooltip() {
        return new ForgeBooleanConfigValue(ConfigData.requireShiftForTotalTooltip);
    }

    @Override
    protected IBooleanConfigValue getConfigShowControlPanel() {
        return new ForgeBooleanConfigValue(ConfigData.showControlPanel);
    }

    @Override
    protected IBooleanConfigValue getConfigOverlayAnimationEnabled() {
        return new ForgeBooleanConfigValue(ConfigData.overlayAnimationEnabled);
    }

    @Override
    protected IIntConfigValue getConfigOverlayAnimationDuration() {
        return new ForgeIntConfigValue(ConfigData.overlayAnimationDuration);
    }
}
