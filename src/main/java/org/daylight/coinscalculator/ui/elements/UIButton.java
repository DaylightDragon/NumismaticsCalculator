package org.daylight.coinscalculator.ui.elements;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public class UIButton extends UIElement {
    private final String label;
    private final Font font;
    private final Runnable onClick;

    public UIButton(String label, Font font, Runnable onClick) {
        this.label = label;
        this.font = font;
        this.onClick = onClick;
        this.width = font.width(label) + 10;
        this.height = font.lineHeight + 6;
    }

    @Override
    public int getPreferredWidth() {
        return label.length() * 6 + 10; // width;
    }

    @Override
    public int getPreferredHeight() {
        return height;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        int bgColor = isMouseOver(mouseX, mouseY) ? 0xFF666666 : 0xFF444444;
        graphics.fill(x, y, x + width, y + height, bgColor);
        graphics.drawString(font, label, x + 5, y + (height - font.lineHeight) / 2, 0xFFFFFF);
    }

    @Override
    public boolean onClick(double mouseX, double mouseY) {
        if (isMouseOver(mouseX, mouseY)) {
            onClick.run();
            return true;
        }
        return false;
    }
}
