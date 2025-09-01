package org.daylight.coinscalculator.ui.elements;

import net.minecraft.client.gui.GuiGraphics;

public interface UIElement {
    int getPreferredWidth();
    int getPreferredHeight();
    void setPosition(int x, int y);
    void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick);
    boolean onClick(double mouseX, double mouseY); // true to stop checks
}
