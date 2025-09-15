package org.daylight.coinscalculator.replacements;

import net.minecraft.network.chat.Component;

public class ForgeComponent implements IComponent {
    private final Component delegate;

    public ForgeComponent(Component delegate) {
        this.delegate = delegate;
    }

    public Component getDelegate() {
        return delegate;
    }
}
