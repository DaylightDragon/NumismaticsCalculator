package org.daylight.numismaticscalculator.fabric.replacements.api;

import net.minecraft.network.chat.Component;
import org.daylight.numismaticscalculator.replacements.IComponent;

public class FabricComponent implements IComponent {
    private Component delegate;
    public FabricComponent(Component delegate) {
        this.delegate = delegate;
    }

    public Component getDelegate() {
        return delegate;
    }
}
