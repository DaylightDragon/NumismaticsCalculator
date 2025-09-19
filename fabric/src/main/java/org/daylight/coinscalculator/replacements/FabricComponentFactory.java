package org.daylight.coinscalculator.replacements;

import net.minecraft.text.Text;
import org.daylight.coinscalculator.replacements.api.FabricComponent;

public class FabricComponentFactory implements IComponentFactory {
    @Override
    public IComponent literal(String text) {
        return new FabricComponent(Text.literal(text));
    }
}
