package org.daylight.numismaticscalculator.fabric.replacements;

import org.daylight.numismaticscalculator.fabric.replacements.api.FabricComponent;
import org.daylight.numismaticscalculator.fabric.replacements.api.FabricEditBox;
import org.daylight.numismaticscalculator.fabric.replacements.api.FabricFont;
import org.daylight.numismaticscalculator.replacements.IComponent;
import org.daylight.numismaticscalculator.replacements.IEditBox;
import org.daylight.numismaticscalculator.replacements.IEditBoxFactory;
import org.daylight.numismaticscalculator.replacements.IFont;

public class FabricEditBoxFactory implements IEditBoxFactory {
    @Override
    public IEditBox create(IFont font, int x, int y, int width, int height, IComponent component) {
        if (!(font instanceof FabricFont f)) {
            throw new IllegalArgumentException("Expected ForgeFont");
        }
        if (!(component instanceof FabricComponent c)) {
            throw new IllegalArgumentException("Expected ForgeComponent");
        }
        return new FabricEditBox(f.getDelegate(), x, y, width, height, c.getDelegate());
    }
}
