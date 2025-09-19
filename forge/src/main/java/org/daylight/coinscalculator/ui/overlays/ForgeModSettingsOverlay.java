package org.daylight.coinscalculator.ui.overlays;

import org.daylight.coinscalculator.config.ConfigData;
import org.daylight.coinscalculator.replacements.api.ForgeBooleanConfigValue;
import org.daylight.coinscalculator.replacements.api.ForgeIntConfigValue;
import org.daylight.coinscalculator.replacements.IBooleanConfigValue;
import org.daylight.coinscalculator.replacements.IIntConfigValue;

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
