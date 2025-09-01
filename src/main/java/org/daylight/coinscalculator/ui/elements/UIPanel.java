package org.daylight.coinscalculator.ui.elements;

import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayList;
import java.util.List;

public class UIPanel implements UIElement {
    private int x, y, width, height;
    private final List<UIElement> children = new ArrayList<>();
    private final int padding = 5;
    private final int spacing = 4;

    public void addElement(UIElement el) {
        children.add(el);
    }

    public void layout() {
        int maxWidth = 0;
        int totalHeight = padding;

        for (UIElement el : children) {
            maxWidth = Math.max(maxWidth, el.getPreferredWidth());
            totalHeight += el.getPreferredHeight() + spacing;
        }
        totalHeight += padding;

        this.width = maxWidth + padding * 2;
        this.height = totalHeight;

        int currentY = y + padding;
        for (UIElement el : children) {
            el.setPosition(x + padding, currentY);
            currentY += el.getPreferredHeight() + spacing;
        }
    }

    @Override
    public int getPreferredWidth() {
        return width; // после layout()
    }

    @Override
    public int getPreferredHeight() {
        return height;
    }

    @Override
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.fill(x, y, x + width, y + height, 0xAA000000);
        for (UIElement el : children) {
            el.render(graphics, mouseX, mouseY, partialTick);
        }
    }

    @Override
    public boolean onClick(double mouseX, double mouseY) {
        for (UIElement el : children) {
            if(el.onClick(mouseX, mouseY)) return true;
        }
        return false;
    }
}
