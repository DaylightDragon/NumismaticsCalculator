package org.daylight.numismaticscalculator.forge.replacements.api;

import org.daylight.numismaticscalculator.replacements.IComponent;
import org.daylight.numismaticscalculator.replacements.IEditBox;
import org.daylight.numismaticscalculator.replacements.IEditBoxFactory;
import org.daylight.numismaticscalculator.replacements.IFont;

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
