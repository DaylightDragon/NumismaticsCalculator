package org.daylight.coinscalculator.ui.elements;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public class UIText implements UIElement {
    private final String text;
    private final Font font;
    private int x, y;

    public UIText(String text, Font font) {
        this.text = text;
        this.font = font;
    }

    @Override
    public int getPreferredWidth() {
        return font.width(text);
    }

    @Override
    public int getPreferredHeight() {
        return font.lineHeight;
    }

    @Override
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.drawString(font, text, x, y, 0xFFFFFF);
    }

    @Override
    public boolean onClick(double mouseX, double mouseY) {
        return false;
    }
}
