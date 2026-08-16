package org.daylight.numismaticscalculator.forge.replacements.api;

import net.minecraft.network.chat.Component;
import org.daylight.numismaticscalculator.replacements.IComponent;
import org.daylight.numismaticscalculator.replacements.IComponentFactory;
import org.daylight.numismaticscalculator.replacements.IMutableComponent;

public class ForgeComponentFactory implements IComponentFactory {
    @Override
    public IMutableComponent empty() {
        return new ForgeMutableComponent(Component.empty());
    }

    @Override
    public IMutableComponent literal(String text) {
        return new ForgeMutableComponent(Component.literal(text));
    }

    @Override
    public IMutableComponent translatable(String key) {
        return new ForgeMutableComponent(Component.translatable(key));
    }

    @Override
    public IMutableComponent translatable(String key, Object... args) {
        return new ForgeMutableComponent(Component.translatable(key, args));
    }
}
