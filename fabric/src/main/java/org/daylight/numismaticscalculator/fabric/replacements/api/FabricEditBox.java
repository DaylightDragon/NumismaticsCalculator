package org.daylight.numismaticscalculator.fabric.replacements.api;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.daylight.numismaticscalculator.replacements.IEditBox;
import org.daylight.numismaticscalculator.replacements.IGuiGraphics;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class FabricEditBox implements IEditBox {
    private final EditBox delegate;
    private boolean allowOnlyNumeric;

    public FabricEditBox(Font font, int x, int y, int width, int height, Component component) {
        this.delegate = new EditBox(font, x, y, width, 20, component);
        delegate.setWidth(width);
//        delegate.set
    }

    public EditBox getDelegate() {
        return delegate;
    }

    private boolean onlyNumeric = false;
    private Predicate<String> filterNumeric = text -> text.isEmpty() || text.matches("\\d+");

    @Override
    public void setOnlyNumeric(boolean onlyNumeric) {
        this.onlyNumeric = onlyNumeric;
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
        delegate.setValue(cleanInput(value));
    }

    @Override
    public boolean isFocused() {
        return delegate.isFocused();
    }

    @Override
    public void setFocused(boolean focused) {
        delegate.setFocused(focused);
    }

    private String cleanInput(String input) {
        if(!onlyNumeric) return input;
        return input.replaceAll("[^0-9]", "");
    }

    @Override
    public void insertText(@NotNull String input) {
        input = cleanInput(input);

        String current = delegate.getValue();
        if (current == null) current = "";

        int cursor = delegate.getCursorPosition();
        cursor = Math.max(0, Math.min(cursor, current.length())); // anti out of bounds

        String before = current.substring(0, cursor);
        String after = current.substring(cursor);

        String newText = before + input + after;
        delegate.setValue(newText);

        delegate.setCursorPosition(cursor + input.length());
    }

    @Override
    public void setWidth(int width) {
        delegate.setWidth(width);
    }

    @Override
    public void setHeight(int height) {
//        delegate.setHeight(height); // not possible
    }

    @Override
    public void setVisible(boolean visible) {
        delegate.visible = visible;
    }

    @Override
    public void setResponder(Consumer<String> onValueChange) {
        delegate.setResponder(onValueChange);
    }

    @Override
    public void deleteChars(int count) {
        String text = delegate.getValue();
        if (text == null || text.isEmpty()) {
            return;
        }

        int cursor = delegate.getCursorPosition();
        cursor = Math.max(0, Math.min(cursor, text.length()));
        if (cursor <= 0) {
            return;
        }

        int start = Math.max(0, cursor - Math.abs(count));
        int end = Math.max(start, Math.min(cursor, text.length()));

        // if indexes are the same
        if (start >= end) {
            return;
        }

        String newText;
        try {
            newText = text.substring(0, start) + text.substring(end);
        } catch (IndexOutOfBoundsException e) {
            // default just in case
            newText = text;
        }

        delegate.setValue(newText);
        delegate.setCursorPosition(start);
    }

    @Override
    public void moveCursor(int i) {
        delegate.moveCursor(i, false); // no method
    }

    @Override
    public void render(@NotNull IGuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        if(!(graphics instanceof FabricGuiGraphics fabricGuiGraphics)) throw new IllegalArgumentException();
        GuiGraphics drawContext = fabricGuiGraphics.getDelegate();
        delegate.render(drawContext, mouseX, mouseY, partialTick);
    }

    @Override
    public String getValue() {
        return delegate.getValue();
    }
}