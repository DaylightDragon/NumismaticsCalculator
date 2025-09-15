package org.daylight.coinscalculator.replacements;

import org.jetbrains.annotations.NotNull;

public interface IDrawingUtils {
    void fill(IGuiGraphics graphics, int xMin, int yMin, int xMax, int yMax, int bgColor, int outlineWidth, int outlineColor);
    void drawScaledText(@NotNull IGuiGraphics graphics, String text, float x, float y, int color, float scale, boolean shadow);
}
