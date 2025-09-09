package org.daylight.coinscalculator.ui.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.ScreenEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class UICheckBox extends UIElement {
    private final Checkbox checkbox;
    private Consumer<Boolean> onValueChange;
    private Font font;

    public UICheckBox(Font font, String label, boolean initialState) {
        this.checkbox = new Checkbox(x, y, 20, 20, Component.literal(label), initialState) {
            @Override
            public void onPress() {
                super.onPress();
                if(onValueChange != null) onValueChange.accept(selected());
            }
        };
        this.font = font;
    }

    public UICheckBox setOnValueChange(Consumer<Boolean> callback) {
        this.onValueChange = callback;
        return this;
    }

    @Override
    public int getPreferredWidth() {
        return checkbox.getWidth();
//        return 150; // можно сделать динамически от текста
    }

    @Override
    public int getPreferredHeight() {
        return checkbox.getHeight();
//        return 20;
    }

    @Override
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        checkbox.setX(x);
        checkbox.setY(y);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        width = checkbox.getWidth();
        height = checkbox.getHeight();

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        checkbox.setX(x);
        checkbox.setY(y);
        checkbox.setWidth(width);
        checkbox.setHeight(height);
    }

    @Override
    public void updateInternalVisibility(boolean value) {
        boolean finalValue = value && isEnabled() && isVisible();
        super.updateInternalVisibility(finalValue);
        setBounds(x, y, width, height);
        checkbox.visible = finalValue; // finalValue;
    }

    @Override
    public void relinkListeners(ScreenEvent.Init.Post event) {
        super.relinkListeners(event);
        event.addListener(checkbox);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
//        System.out.println(checkbox.getX() + " " + checkbox.getY() + " " + checkbox.getWidth() + " " + checkbox.getHeight());
        if (Minecraft.getInstance().screen == null) {
            checkbox.render(graphics, mouseX, mouseY, partialTick);
        }
    }

//    @Override
//    public boolean onClick(double mouseX, double mouseY) {
//        if(!isEnabled()) return false;
//        onValueChange.accept(checkbox.selected());
//        return false;
//    }

    //    @Override
//    public boolean onClick(double mouseX, double mouseY) {
//        if(!isEnabled()) return false;
//        boolean clicked = checkbox.mouseClicked(mouseX - x, mouseY - y, 0);
//        System.out.println("Clicked: " + clicked);
//        if (clicked && onValueChange != null) {
//            System.out.println("Selected: " + checkbox.selected());
//            onValueChange.accept(checkbox.selected());
//        }
//        return clicked;
//    }

//    public UICheckBox setOnValueChange(Consumer<String> onValueChange) {
//        checkbox.setResponder(onValueChange);
//        return this;
//    }

    public boolean isChecked() {
        return checkbox.selected();
    }

    public void setChecked(boolean value) {
        if(value != checkbox.selected()) {checkbox.onPress();
            if (onValueChange != null) {
                onValueChange.accept(value);
            }
        }
    }

    public Checkbox getCheckbox() {
        return checkbox;
    }
}

