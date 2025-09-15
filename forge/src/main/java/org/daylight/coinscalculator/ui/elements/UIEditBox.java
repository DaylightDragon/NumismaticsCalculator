package org.daylight.coinscalculator.ui.elements;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ScreenEvent;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

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

    public UIEditBox setText(String text) {
        editBox.setValue(text);
        return this;
    }

    @Override
    public int getPreferredWidth() {
        return clampWidth(80);
    }

    @Override
    public int getPreferredHeight() {
        return clampWidth(20);
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
        if(event.getListenersList().contains(editBox)) return;
//        event.addListener(editBox); // temporary disabled!
        System.out.println("added listener to " + this);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
//        if(Minecraft.getInstance().screen == null) {
            editBox.render(graphics, mouseX, mouseY, partialTick);
//        }
        // EditBox renders by itself
//        System.out.println("Rendering " + this.getClass().getSimpleName() +
//                " at x=" + x + ", y=" + y + ", width=" + width + ", height=" + height);
//        System.out.println("Real coords: " + editBox.getX() + ", " + editBox.getY() + " width: " + editBox.getWidth() + " height: " + editBox.getHeight());
    }

    public UIEditBox setOnValueChange(Consumer<String> onValueChange) {
        editBox.setResponder(onValueChange);
        return this;
    }

//    @Override
//    public boolean onClick(double mouseX, double mouseY) {
////        System.out.println("UIEditBox onClick");
////        return editBox.mouseClicked(mouseX, mouseY, 0); // 0 = left click
//        return false;
//    }

//    public boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
//        return editBox.keyPressed(keyCode, scanCode, modifiers);
//    }
//
//    public boolean onCharTyped(char codePoint, int modifiers) {
//        return editBox.charTyped(codePoint, modifiers);
//    }

    public EditBox getEditBox() {
        return editBox;
    }

    @Override
    public boolean onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);
        if (isEnabled() && isMouseOver(mouseX, mouseY)) {
            editBox.setFocused(true);
            return true;
        } else editBox.setFocused(false);

        return false;
    }

    public void keyPressed(InputEvent.Key event) {
        if (!isEnabled()) return;
        if (!editBox.isFocused()) return;

        int key = event.getKey();
        int scanCode = event.getScanCode();
        int action = event.getAction();

        if (action == InputConstants.PRESS || action == InputConstants.REPEAT) {
            if (key == GLFW.GLFW_KEY_BACKSPACE) {
                editBox.deleteChars(-1);
                return;
            }
            if (key == GLFW.GLFW_KEY_DELETE) {
                editBox.deleteChars(1);
                return;
            }
            if (key == GLFW.GLFW_KEY_LEFT) {
                editBox.moveCursor(-1);
                return;
            }
            if (key == GLFW.GLFW_KEY_RIGHT) {
                editBox.moveCursor(1);
                return;
            }
        }

        if (action == InputConstants.PRESS) {
            String name = GLFW.glfwGetKeyName(key, scanCode);
            if (name != null && !name.isEmpty()) {
                // Тут можно ещё учесть shift для верхнего регистра
                editBox.insertText(name);
            }
        }
    }
}
