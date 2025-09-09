package org.daylight.coinscalculator.ui.elements;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;

public class UIText extends UIElement {
    private String text;
    private final Font font;
    private float scale;
    private int color;

    public UIText(String text, Font font, float scale, int color) {
        this.text = text;
        this.font = font;
        this.scale = scale;
        this.color = color;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public int getPreferredWidth() {
        return (int) (font.width(text) * scale + 10);
    }

    @Override
    public int getPreferredHeight() {
//        System.out.println(getId() + " getPreferredHeight: " + font.lineHeight);
        return (int) (font.lineHeight); //  * scale
    }

    @Override
    public int getWidth() {
        return (int) (width); //  * scale
    }

    @Override
    public int getHeight() {
        return (int) (height); //  * scale
    }

    @Override
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        if(!shouldBeRendered()) return;
//        graphics.drawString(font, text, x, y, 0xFFFFFF);
        drawScaledText(graphics, text, x, y, color, scale, true);
    }

    public static void drawScaledText(@NotNull GuiGraphics graphics, String text, float x, float y, int color, float scale, boolean shadow) {
        PoseStack poseStack = graphics.pose();
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
    public void updateInternalValues() {
        super.updateInternalValues();
    }

    @Override
    public boolean onClick(double mouseX, double mouseY) {
        return false;
    }
}
