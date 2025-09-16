package org.daylight.coinscalculator.replacements;

public interface IBooleanConfigValue extends IConfigValue<Boolean> {
    default void toggle() {
        set(!get());
    }
}

