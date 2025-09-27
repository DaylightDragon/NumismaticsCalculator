package org.daylight.numismaticscalculator.replacements;

import net.minecraft.text.Text;
import org.daylight.numismaticscalculator.replacements.api.FabricComponent;

public class FabricComponentFactory implements IComponentFactory {
    @Override
    public IComponent literal(String text) {
        return new FabricComponent(Text.literal(text));
    }
}
