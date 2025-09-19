package org.daylight.coinscalculator.replacements.api;

import org.daylight.coinscalculator.replacements.IComponent;
import org.daylight.coinscalculator.replacements.IEditBox;
import org.daylight.coinscalculator.replacements.IEditBoxFactory;
import org.daylight.coinscalculator.replacements.IFont;

public class ForgeEditBoxFactory implements IEditBoxFactory {
    @Override
    public IEditBox create(IFont font, int x, int y, int width, int height, IComponent component) {
        if (!(font instanceof ForgeFont f)) {
            throw new IllegalArgumentException("Expected ForgeFont");
        }
        if (!(component instanceof ForgeComponent c)) {
            throw new IllegalArgumentException("Expected ForgeComponent");
        }
        return new ForgeEditBox(f.getDelegate(), x, y, width, height, c.getDelegate());
    }
}
