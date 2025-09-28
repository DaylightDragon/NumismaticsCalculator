package org.daylight.numismaticscalculator.neoforge.replacements.api;

import net.minecraft.network.chat.Component;
import org.daylight.numismaticscalculator.replacements.IComponent;
import org.daylight.numismaticscalculator.replacements.IComponentFactory;

public class NeoForgeComponentFactory implements IComponentFactory {
    @Override
    public IComponent literal(String text) {
        return new NeoForgeComponent(Component.literal(text));
    }
}
