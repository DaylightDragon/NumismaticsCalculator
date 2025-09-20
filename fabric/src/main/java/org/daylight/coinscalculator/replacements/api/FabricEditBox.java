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

    public FabricEditBox(TextRenderer font, int x, int y, int width, int height, Text component) {
        this.delegate = new TextFieldWidget(font, x, y, width, 20, component);
        delegate.setWidth(width);
//        delegate.set
    }

    public TextFieldWidget getDelegate() {
        return delegate;
    }

    private String lastText = "";

    @Override
    public void setFilter(@NotNull Predicate<String> filter) {
        delegate.setChangedListener(newText -> {
            if (!filter.test(newText)) {
                delegate.setText(lastText);
            }
            lastText = newText;
        });
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
        delegate.setText(value);
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
        delegate.setText(delegate.getText() + text);
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