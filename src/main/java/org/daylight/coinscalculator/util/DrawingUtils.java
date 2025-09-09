package org.daylight.coinscalculator.util;

import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;

public class DrawingUtils {
    public static void fill(@NotNull GuiGraphics g, int minX, int minY, int maxX, int maxY, int pColor, int outlineWidth, int outlineColor) {
        if(outlineWidth <= 0) {
            g.fill(minX, minY, maxX, maxY, pColor);
        } else {
            int left = minX + outlineWidth;
            int top = minY + outlineWidth;
            int right = maxX - outlineWidth;
            int bottom = maxY - outlineWidth;
            if(left < right && top < bottom) {
                g.fill(left, top, maxX, maxY, pColor);
            }

            int x1 = minX + outlineWidth;
            int x2 = maxX - outlineWidth;

            int y1 = minY + outlineWidth;
            int y2 = maxY - outlineWidth;

            g.fill(minX, minY, x2, y1, outlineColor); // top, left to right
            g.fill(x2, minY, maxX, y2, outlineColor); // right, top to bottom
            g.fill(x1, y2, maxX, maxY, outlineColor);
            g.fill(minX, y1, x1, maxY, outlineColor);
        }
    }
}
