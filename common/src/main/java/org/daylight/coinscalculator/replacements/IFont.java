package org.daylight.coinscalculator.replacements;

import org.jetbrains.annotations.NotNull;

public interface IFont {
    int width(@NotNull String text);
    int lineHeight();
}
