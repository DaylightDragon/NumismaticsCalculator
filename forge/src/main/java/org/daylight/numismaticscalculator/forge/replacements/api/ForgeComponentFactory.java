package org.daylight.numismaticscalculator.forge.replacements.api;

import net.minecraft.network.chat.Component;
import org.daylight.numismaticscalculator.replacements.IComponent;
import org.daylight.numismaticscalculator.replacements.IComponentFactory;

public class ForgeComponentFactory implements IComponentFactory {
    @Override
    public IComponent literal(String text) {
        return new ForgeComponent(Component.literal(text));
    }
}
