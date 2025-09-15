package org.daylight.coinscalculator.replacements;

import org.jetbrains.annotations.NotNull;

public interface IGuiGraphics {
    void drawString(@NotNull IFont font, @NotNull String text, int x, int y, int color);
    void drawTexture(@NotNull ITexture texture, int x, int y, int width, int height);
}
