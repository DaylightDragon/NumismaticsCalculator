package org.daylight.numismaticscalculator.replacements;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface IEditBox {
    void setOnlyNumeric(boolean onlyNumeric);
    void setX(int x);
    void setY(int y);
    void setValue(@NotNull String value);
    boolean isFocused();
    void setFocused(boolean focused);
    void insertText(@NotNull String text);

    void setWidth(int width);

    void setHeight(int height);

    void setVisible(boolean finalValue);

    void setResponder(Consumer<String> onValueChange);

    void deleteChars(int i);

    void moveCursor(int i);

    void render(@NotNull IGuiGraphics graphics, int mouseX, int mouseY, float partialTick);

    String getValue();
}
