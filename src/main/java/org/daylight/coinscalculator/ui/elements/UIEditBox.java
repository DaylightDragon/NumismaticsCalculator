package org.daylight.coinscalculator.ui.elements;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

public class UIEditBox implements UIElement {
    private final EditBox editBox;
    private int x, y;

    public UIEditBox(Font font) {
        this.editBox = new EditBox(font, 0, 0, 100, 20, Component.literal(""));
    }

    @Override
    public int getPreferredWidth() {
        return 100;
    }

    @Override
    public int getPreferredHeight() {
        return 20;
    }

    @Override
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        editBox.setX(x);
        editBox.setY(y);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // EditBox сам себя рендерит через Minecraft
    }

    @Override
    public boolean onClick(double mouseX, double mouseY) {
        // обрабатывает сам
        return false;
    }

    public EditBox getEditBox() {
        return editBox;
    }
}
