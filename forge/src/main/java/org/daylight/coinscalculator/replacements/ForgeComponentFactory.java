package org.daylight.coinscalculator.replacements;

import net.minecraft.network.chat.Component;

public class ForgeComponentFactory implements IComponentFactory {
    @Override
    public IComponent literal(String text) {
        return new ForgeComponent(Component.literal(text));
    }
}
