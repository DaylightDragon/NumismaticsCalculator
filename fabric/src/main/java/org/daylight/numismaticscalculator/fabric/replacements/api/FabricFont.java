package org.daylight.numismaticscalculator.fabric.replacements.api;

import net.minecraft.client.gui.Font;
import org.daylight.numismaticscalculator.replacements.IFont;
import org.jetbrains.annotations.NotNull;

public class FabricFont implements IFont {
    private Font delegate;
    public FabricFont(Font delegate) {
        this.delegate = delegate;
    }

    public Font getDelegate() {
        return delegate;
    }

    @Override
    public int width(@NotNull String text) {
        return delegate.width(text);
    }

    @Override
    public int lineHeight() {
        return delegate.lineHeight;
    }
}
