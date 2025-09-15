package org.daylight.coinscalculator.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import org.daylight.coinscalculator.replacements.IDrawingUtils;
import org.daylight.coinscalculator.replacements.IGuiGraphics;
import org.jetbrains.annotations.NotNull;

public class DrawingUtils implements IDrawingUtils {
    public static void fill(@NotNull GuiGraphics g, int minX, int minY, int maxX, int maxY, int pColor, int outlineWidth, int outlineColor) {
        if(outlineWidth <= 0) {
            g.fill(minX, minY, maxX, maxY, pColor);
        } else {
            int left = minX + outlineWidth;
            int top = minY + outlineWidth;
            int right = maxX - outlineWidth;
            int bottom = maxY - outlineWidth;
            if(left < right && top < bottom) {
                g.fill(left, top, right, bottom, pColor);
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

    @Override
    public void fill(IGuiGraphics graphics, int xMin, int yMin, int xMax, int yMax, int bgColor, int outlineWidth, int outlineColor) {

    }

    public static void drawScaledTextStatic(@NotNull IGuiGraphics graphics, String text, float x, float y, int color, float scale, boolean shadow) {
        PoseStack poseStack = (PoseStack) graphics.pose();
        poseStack.pushPose();

        // Переносим в точку (x, y)
        poseStack.translate(x, y, 0);
        // Масштабируем
        poseStack.scale(scale, scale, 1f);

        Font font = Minecraft.getInstance().font;

        // drawInBatch должен использовать локальные координаты (0,0) после translate
        font.drawInBatch(
                text,
                0, 0,           // локальные координаты
                color,
                shadow,
                poseStack.last().pose(),
                Minecraft.getInstance().renderBuffers().bufferSource(),
                Font.DisplayMode.NORMAL,
                0,
                0xF000F0
        );

        // Важно: вызвать flush после drawInBatch
        Minecraft.getInstance().renderBuffers().bufferSource().endBatch();

        poseStack.popPose();
    }

    @Override
    public void drawScaledText(IGuiGraphics graphics, String text, float x, float y, int color, float scale, boolean shadow) {
        drawScaledTextStatic(graphics, text, x, y, color, scale, shadow);
    }
}
