package org.daylight.coinscalculator.ui.elements;

import net.minecraft.client.gui.GuiGraphics;
import org.daylight.coinscalculator.CoinsCalculator;

public abstract class UIElement {
    private int x, y, width, height; // change to protected later, DO NOT SET FROM OUTSIDE
    protected int minWidth = 0, maxWidth = Integer.MAX_VALUE;
    protected int minHeight = 0, maxHeight = Integer.MAX_VALUE;
    protected boolean isVisible;
    protected boolean isEnabled;

    public abstract int getPreferredWidth();
    public abstract int getPreferredHeight();

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
//        CoinsCalculator.LOGGER.info(getClass().getSimpleName() +
//                " setPosition -> x: " + x + ", y: " + y);
    }

    public void setBounds(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = Math.max(minWidth, Math.min(width, maxWidth));
        this.height = Math.max(minHeight, Math.min(height, maxHeight));
//        CoinsCalculator.LOGGER.info(getClass().getSimpleName() +
//                " setBounds -> x: " + x + ", y: " + y + ", width: " + width + ", height: " + height);
    }

    public abstract void render(GuiGraphics g, int mouseX, int mouseY, float partialTick);

    public boolean onClick(double mouseX, double mouseY) {
        // true to stop checks
        return false;
    }

    protected boolean isMouseOver(double mx, double my) {
        return mx >= x && mx <= x + width && my >= y && my <= y + height;
    }
}
