package org.daylight.numismaticscalculator.replacements;

import org.jetbrains.annotations.NotNull;

public interface IFont {
    int width(@NotNull String text);
    int lineHeight();
}
