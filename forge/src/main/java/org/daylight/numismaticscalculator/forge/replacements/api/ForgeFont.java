package org.daylight.numismaticscalculator.forge.replacements.api;

import net.minecraft.client.gui.Font;
import org.daylight.numismaticscalculator.replacements.IFont;

public class ForgeFont implements IFont {
    private final Font delegate;

    public ForgeFont(Font delegate) {
        this.delegate = delegate;
    }

    public Font getDelegate() {
        return delegate;
    }

    @Override
    public int width(String text) {
        return delegate.width(text);
    }

    @Override
    public int lineHeight() {
        return delegate.lineHeight;
    }
}
