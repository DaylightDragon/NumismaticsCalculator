package org.daylight.coinscalculator.ui.elements;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public class UIText extends UIElement {
    private String text;
    private final Font font;

    public UIText(String text, Font font) {
        this.text = text;
        this.font = font;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int getPreferredWidth() {
        return font.width(text);
    }

    @Override
    public int getPreferredHeight() {
//        System.out.println(getId() + " getPreferredHeight: " + font.lineHeight);
        return font.lineHeight;
    }

    @Override
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        if(!shouldBeRendered()) return;
        graphics.drawString(font, text, x, y, 0xFFFFFF);
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
