package org.daylight.numismaticscalculator.forge.ui.overlays;

import org.daylight.numismaticscalculator.forge.config.ConfigData;
import org.daylight.numismaticscalculator.forge.replacements.api.ForgeBooleanConfigValue;
import org.daylight.numismaticscalculator.forge.replacements.api.ForgeIntConfigValue;
import org.daylight.numismaticscalculator.replacements.IBooleanConfigValue;
import org.daylight.numismaticscalculator.replacements.IIntConfigValue;
import org.daylight.numismaticscalculator.ui.overlays.IModSettingsOverlay;

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
