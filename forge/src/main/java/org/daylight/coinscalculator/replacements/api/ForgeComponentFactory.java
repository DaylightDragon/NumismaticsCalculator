package org.daylight.coinscalculator.replacements.api;

import net.minecraft.network.chat.Component;
import org.daylight.coinscalculator.replacements.IComponent;
import org.daylight.coinscalculator.replacements.IComponentFactory;

public class ForgeComponentFactory implements IComponentFactory {
    @Override
    public IComponent literal(String text) {
        return new ForgeComponent(Component.literal(text));
    }
}
