package org.daylight.coinscalculator.ui.elements;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public class UiSpace extends UIElement {
    private int xSpace;
    private int ySpace;

    public UiSpace(int xSpace, int ySpace) {
        this.xSpace = xSpace;
        this.ySpace = ySpace;
    }

    @Override
    public int getPreferredWidth() {
        return xSpace;
    }

    @Override
    public int getPreferredHeight() {
        return ySpace;
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        super.render(g, mouseX, mouseY, partialTick);
    }
}
