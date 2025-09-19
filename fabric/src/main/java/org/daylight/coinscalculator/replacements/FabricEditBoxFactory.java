package org.daylight.coinscalculator.replacements;

import org.daylight.coinscalculator.replacements.api.FabricComponent;
import org.daylight.coinscalculator.replacements.api.FabricEditBox;
import org.daylight.coinscalculator.replacements.api.FabricFont;

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
