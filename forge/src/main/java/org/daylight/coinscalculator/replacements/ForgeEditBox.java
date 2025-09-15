package org.daylight.coinscalculator.replacements;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.daylight.coinscalculator.replacements.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class ForgeEditBox implements IEditBox {
    private final EditBox delegate;

    public ForgeEditBox(Font font, int x, int y, int width, int height, Component component) {
        this.delegate = new EditBox(font, x, y, width, height, component);
    }

    public EditBox getDelegate() {
        return delegate;
    }

    @Override
    public void setFilter(@NotNull Predicate<String> filter) {
        delegate.setFilter(filter::test);
    }

    @Override
    public void setX(int x) {
        delegate.setX(x);
    }

    @Override
    public void setY(int y) {
        delegate.setY(y);
    }

    @Override
    public void setValue(@NotNull String value) {
        delegate.setValue(value);
    }

    @Override
    public boolean isFocused() {
        return delegate.isFocused();
    }

    @Override
    public void setFocused(boolean focused) {
        delegate.setFocused(focused);
    }

    @Override
    public void insertText(@NotNull String text) {
        delegate.insertText(text);
    }

    @Override
    public void setWidth(int width) {
        delegate.setWidth(width);
    }

    @Override
    public void setHeight(int height) {
        delegate.setHeight(height);
    }

    @Override
    public void setVisible(boolean finalValue) {
        delegate.setVisible(finalValue);
    }

    @Override
    public void setResponder(Consumer<String> onValueChange) {
        delegate.setResponder(onValueChange);
    }

    @Override
    public void deleteChars(int i) {
        delegate.deleteChars(i);
    }

    @Override
    public void moveCursor(int i) {
        delegate.moveCursor(i);
    }

    @Override
    public void render(@NotNull IGuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        if (graphics instanceof ForgeGuiGraphics forgeGraphics) {
            delegate.render(forgeGraphics.getDelegate(), mouseX, mouseY, partialTick);
        }
    }
}