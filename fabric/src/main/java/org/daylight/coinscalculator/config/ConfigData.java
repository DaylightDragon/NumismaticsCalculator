package org.daylight.coinscalculator.config;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ConfigData {
    public static AtomicBoolean requireShiftForTotalTooltip = new AtomicBoolean(false);
    public static AtomicBoolean showControlPanel = new AtomicBoolean(true);
    public static AtomicBoolean overlayAnimationEnabled = new AtomicBoolean(true);
    public static AtomicInteger overlayAnimationDuration = new AtomicInteger(150);
}
