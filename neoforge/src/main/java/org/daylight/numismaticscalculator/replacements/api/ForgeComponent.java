package org.daylight.numismaticscalculator.replacements.api;

import net.minecraft.network.chat.Component;
import org.daylight.numismaticscalculator.replacements.IComponent;

public class ForgeComponent implements IComponent {
    private final Component delegate;

    public ForgeComponent(Component delegate) {
        this.delegate = delegate;
    }

    public Component getDelegate() {
        return delegate;
    }
}
