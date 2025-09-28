package org.daylight.numismaticscalculator.neoforge.replacements.api;

import org.daylight.numismaticscalculator.neoforge.replacements.api.NeoForgeComponent;
import org.daylight.numismaticscalculator.neoforge.replacements.api.NeoForgeEditBox;
import org.daylight.numismaticscalculator.replacements.IComponent;
import org.daylight.numismaticscalculator.replacements.IEditBox;
import org.daylight.numismaticscalculator.replacements.IEditBoxFactory;
import org.daylight.numismaticscalculator.replacements.IFont;

public class NeoForgeEditBoxFactory implements IEditBoxFactory {
    @Override
    public IEditBox create(IFont font, int x, int y, int width, int height, IComponent component) {
        if (!(font instanceof NeoForgeFont f)) {
            throw new IllegalArgumentException("Expected ForgeFont");
        }
        if (!(component instanceof NeoForgeComponent c)) {
            throw new IllegalArgumentException("Expected ForgeComponent");
        }
        return new NeoForgeEditBox(f.getDelegate(), x, y, width, height, c.getDelegate());
    }
}
