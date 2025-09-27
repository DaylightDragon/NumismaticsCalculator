package org.daylight.numismaticscalculator.replacements;

public interface IBooleanConfigValue extends IConfigValue<Boolean> {
    default void toggle() {
        set(!get());
    }
}

