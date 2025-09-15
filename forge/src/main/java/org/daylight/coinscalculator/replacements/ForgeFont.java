package org.daylight.coinscalculator.replacements;

import net.minecraft.client.gui.Font;

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
