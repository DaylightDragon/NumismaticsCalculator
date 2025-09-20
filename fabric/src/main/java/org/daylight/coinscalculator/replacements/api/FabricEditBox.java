package org.daylight.coinscalculator.replacements.api;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.daylight.coinscalculator.replacements.IEditBox;
import org.daylight.coinscalculator.replacements.IGuiGraphics;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class FabricEditBox implements IEditBox {
    private final TextFieldWidget delegate;
    private boolean allowOnlyNumeric;

    public FabricEditBox(TextRenderer font, int x, int y, int width, int height, Text component) {
        this.delegate = new TextFieldWidget(font, x, y, width, 20, component);
        delegate.setWidth(width);
//        delegate.set
    }

    public TextFieldWidget getDelegate() {
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
        delegate.setText(cleanInput(value));
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

        String current = delegate.getText();
        if (current == null) current = "";

        int cursor = delegate.getCursor();
        cursor = Math.max(0, Math.min(cursor, current.length())); // anti out of bounds

        String before = current.substring(0, cursor);
        String after = current.substring(cursor);

        String newText = before + input + after;
        delegate.setText(newText);

        delegate.setCursor(cursor + input.length());
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
        delegate.setChangedListener(onValueChange);
    }

    @Override
    public void deleteChars(int count) {
        String text = delegate.getText();
        if (text == null || text.isEmpty()) {
            return;
        }

        int cursor = delegate.getCursor();
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

        delegate.setText(newText);
        delegate.setCursor(start);
    }

    @Override
    public void moveCursor(int i) {
        delegate.moveCursor(i); // no method
    }

    @Override
    public void render(@NotNull IGuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        if(!(graphics instanceof FabricGuiGraphics fabricGuiGraphics)) throw new IllegalArgumentException();
        DrawContext drawContext = fabricGuiGraphics.getDelegate();
        delegate.render(drawContext, mouseX, mouseY, partialTick);
    }

    @Override
    public String getValue() {
        return delegate.getText();
    }
}