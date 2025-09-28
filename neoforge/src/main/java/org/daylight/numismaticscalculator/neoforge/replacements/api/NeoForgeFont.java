package org.daylight.numismaticscalculator.neoforge.replacements.api;

import net.minecraft.client.gui.Font;
import org.daylight.numismaticscalculator.replacements.IFont;

public class NeoForgeFont implements IFont {
    private final Font delegate;

    public NeoForgeFont(Font delegate) {
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
