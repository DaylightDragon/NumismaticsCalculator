package org.daylight.numismaticscalculator.replacements.api;

import net.minecraft.client.font.TextRenderer;
import org.daylight.numismaticscalculator.replacements.IFont;
import org.jetbrains.annotations.NotNull;

public class FabricFont implements IFont {
    private TextRenderer delegate;
    public FabricFont(TextRenderer delegate) {
        this.delegate = delegate;
    }

    public TextRenderer getDelegate() {
        return delegate;
    }

    @Override
    public int width(@NotNull String text) {
        return delegate.getWidth(text);
    }

    @Override
    public int lineHeight() {
        return delegate.fontHeight;
    }
}
