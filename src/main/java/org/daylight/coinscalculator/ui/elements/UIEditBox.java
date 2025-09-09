package org.daylight.coinscalculator.ui.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.ScreenEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class UIEditBox extends UIElement {
    private final EditBox editBox;

    public UIEditBox(Font font, int width, int height) {
        this.width = width;
        this.height = height;
        this.editBox = new EditBox(font, x, y, width, height, Component.literal(""));
    }

    public UIEditBox allowOnlyNumeric() {
        editBox.setFilter(text -> text.isEmpty() || text.matches("\\d+"));
        return this;
    }

    @Override
    public int getPreferredWidth() {
        return 80;
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
    public void setBounds(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        editBox.setX(x);
        editBox.setY(y);
        editBox.setWidth(width);
        editBox.setHeight(height);
    }

    @Override
    public void updateInternalVisibility(boolean value) {
        boolean finalValue = value && isEnabled() && isVisible();
//        System.out.println("Editbox updateInternalVisibility " + finalValue);
        super.updateInternalVisibility(finalValue);
        setBounds(x, y, width, height);
        editBox.setVisible(finalValue);
//        if(value) setBounds(x, y, width, height);
    }

    @Override
    public void relinkListeners(ScreenEvent.Init.Post event) {
        super.relinkListeners(event);
//        System.out.println("Adding listener to editBox");
        event.addListener(editBox);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        if(Minecraft.getInstance().screen == null) editBox.render(graphics, mouseX, mouseY, partialTick);
        // EditBox renders by itself
//        System.out.println("Rendering " + this.getClass().getSimpleName() +
//                " at x=" + x + ", y=" + y + ", width=" + width + ", height=" + height);
//        System.out.println("Real coords: " + editBox.getX() + ", " + editBox.getY() + " width: " + editBox.getWidth() + " height: " + editBox.getHeight());
    }

    public UIEditBox setOnValueChange(Consumer<String> onValueChange) {
        editBox.setResponder(onValueChange);
        return this;
    }

    @Override
    public boolean onClick(double mouseX, double mouseY) {
//        System.out.println("UIEditBox onClick");
//        return editBox.mouseClicked(mouseX, mouseY, 0); // 0 = left click
        return false;
    }

    public boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
        return editBox.keyPressed(keyCode, scanCode, modifiers);
    }

    public boolean onCharTyped(char codePoint, int modifiers) {
        return editBox.charTyped(codePoint, modifiers);
    }

    public EditBox getEditBox() {
        return editBox;
    }
}
