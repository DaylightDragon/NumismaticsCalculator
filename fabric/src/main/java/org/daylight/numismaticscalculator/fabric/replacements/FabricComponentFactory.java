package org.daylight.numismaticscalculator.fabric.replacements;

import net.minecraft.text.Text;
import org.daylight.numismaticscalculator.fabric.replacements.api.FabricComponent;
import org.daylight.numismaticscalculator.fabric.replacements.api.FabricMutableComponent;
import org.daylight.numismaticscalculator.replacements.IComponentFactory;
import org.daylight.numismaticscalculator.replacements.IMutableComponent;

public class FabricComponentFactory implements IComponentFactory {
    @Override
    public IMutableComponent empty() {
        return new FabricMutableComponent(Text.empty());
    }

    @Override
    public IMutableComponent literal(String text) {
        return new FabricMutableComponent(Text.literal(text));
    }

    @Override
    public IMutableComponent translatable(String key) {
        return new FabricMutableComponent(Text.translatable(key));
    }

    @Override
    public IMutableComponent translatable(String key, Object... args) {
        return new FabricMutableComponent(Text.translatable(key, args));
    }
}
