package org.daylight.numismaticscalculator.fabric.replacements;

import net.minecraft.text.Text;
import org.daylight.numismaticscalculator.fabric.replacements.api.FabricComponent;
import org.daylight.numismaticscalculator.replacements.IComponent;
import org.daylight.numismaticscalculator.replacements.IComponentFactory;

public class FabricComponentFactory implements IComponentFactory {
    @Override
    public IComponent literal(String text) {
        return new FabricComponent(Text.literal(text));
    }
}
