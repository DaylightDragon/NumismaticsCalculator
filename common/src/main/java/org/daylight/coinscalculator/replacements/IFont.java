package org.daylight.coinscalculator.replacements;

import org.jetbrains.annotations.NotNull;

public interface IFont {
    int getWidth(@NotNull String text);
    int getHeight();
}
