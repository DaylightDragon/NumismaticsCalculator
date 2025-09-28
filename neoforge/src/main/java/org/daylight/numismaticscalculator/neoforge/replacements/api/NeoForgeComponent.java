package org.daylight.numismaticscalculator.neoforge.replacements.api;

import net.minecraft.network.chat.Component;
import org.daylight.numismaticscalculator.replacements.IComponent;

public class NeoForgeComponent implements IComponent {
    private final Component delegate;

    public NeoForgeComponent(Component delegate) {
        this.delegate = delegate;
    }

    public Component getDelegate() {
        return delegate;
    }
}
