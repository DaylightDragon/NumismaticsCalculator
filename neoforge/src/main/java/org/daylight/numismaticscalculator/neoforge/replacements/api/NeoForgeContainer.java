package org.daylight.numismaticscalculator.neoforge.replacements.api;

import net.minecraft.world.Container;
import org.daylight.numismaticscalculator.replacements.IContainer;

public class NeoForgeContainer implements IContainer {
    private final Container delegate;

    public NeoForgeContainer(Container delegate) {
        this.delegate = delegate;
    }

    public Container getDelegate() {
        return delegate;
    }
}
