package org.daylight.numismaticscalculator.neoforge.ui.overlays;

import org.daylight.numismaticscalculator.neoforge.config.ConfigData;
import org.daylight.numismaticscalculator.neoforge.replacements.api.NeoForgeBooleanConfigValue;
import org.daylight.numismaticscalculator.neoforge.replacements.api.NeoForgeIntConfigValue;
import org.daylight.numismaticscalculator.replacements.IBooleanConfigValue;
import org.daylight.numismaticscalculator.replacements.IIntConfigValue;
import org.daylight.numismaticscalculator.ui.overlays.IModSettingsOverlay;

public class NeoForgeModSettingsOverlay extends IModSettingsOverlay {
    @Override
    protected IBooleanConfigValue getConfigRequireShiftForTotalTooltip() {
        return new NeoForgeBooleanConfigValue(ConfigData.requireShiftForTotalTooltip);
    }

    @Override
    protected IBooleanConfigValue getConfigShowControlPanel() {
        return new NeoForgeBooleanConfigValue(ConfigData.showControlPanel);
    }

    @Override
    protected IBooleanConfigValue getConfigOverlayAnimationEnabled() {
        return new NeoForgeBooleanConfigValue(ConfigData.overlayAnimationEnabled);
    }

    @Override
    protected IIntConfigValue getConfigOverlayAnimationDuration() {
        return new NeoForgeIntConfigValue(ConfigData.overlayAnimationDuration);
    }
}
