package org.daylight.numismaticscalculator.fabric.replacements;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import org.daylight.numismaticscalculator.fabric.replacements.api.FabricGuiGraphics;
import org.daylight.numismaticscalculator.replacements.IDrawingUtils;
import org.daylight.numismaticscalculator.replacements.IGuiGraphics;
import org.jetbrains.annotations.NotNull;

public class FabricDrawingUtils implements IDrawingUtils {
    public static void fillSt(@NotNull IGuiGraphics graphics, int minX, int minY, int maxX, int maxY, int pColor, int outlineWidth, int outlineColor) {
        if(!(graphics instanceof FabricGuiGraphics fabricGuiGraphics)) throw new IllegalArgumentException();
        GuiGraphics g = fabricGuiGraphics.getDelegate();
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

            g.fill(minX, minY, x2, y1, outlineColor); // top
            g.fill(x2, minY, maxX, y2, outlineColor); // right
            g.fill(x1, y2, maxX, maxY, outlineColor); // bottom
            g.fill(minX, y1, x1, maxY, outlineColor); // left
        }
    }

    @Override
    public void fill(IGuiGraphics graphics, int xMin, int yMin, int xMax, int yMax, int bgColor, int outlineWidth, int outlineColor) {
        if(!(graphics instanceof FabricGuiGraphics fabricGuiGraphics)) throw new IllegalArgumentException();
        fillSt(fabricGuiGraphics, xMin, yMin, xMax, yMax, bgColor, outlineWidth, outlineColor);
    }

    public static void drawScaledTextStatic(@NotNull IGuiGraphics graphics, String text, float x, float y, int color, float scale, boolean shadow) {
        if(!(graphics instanceof FabricGuiGraphics fabricGuiGraphics)) throw new IllegalArgumentException();
        GuiGraphics context = fabricGuiGraphics.getDelegate();
        PoseStack matrices = context.pose(); // это MatrixStack
        matrices.pushPose(); // MatrixStack имеет pushPose/popPose

        matrices.translate(x, y, 0);
        matrices.scale(scale, scale, 1f);

        Font textRenderer = Minecraft.getInstance().font;

        textRenderer.drawInBatch(
                text,
                0f, 0f, // локальные координаты после translate
                color,
                shadow,
                matrices.last().pose(), // преобразование в Matrix4f
                Minecraft.getInstance().renderBuffers().bufferSource(),
                Font.DisplayMode.NORMAL,
                0, // background color
                0xF000F0 // light
        );

        matrices.popPose();
    }

    @Override
    public void drawScaledText(IGuiGraphics graphics, String text, float x, float y, int color, float scale, boolean shadow) {
        drawScaledTextStatic(graphics, text, x, y, color, scale, shadow);
    }
}
