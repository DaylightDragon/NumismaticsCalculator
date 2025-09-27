package org.daylight.numismaticscalculator.forge.replacements.api;

import net.minecraft.world.Container;
import org.daylight.numismaticscalculator.replacements.IContainer;

public class ForgeContainer implements IContainer {
    private final Container delegate;

    public ForgeContainer(Container delegate) {
        this.delegate = delegate;
    }

    public Container getDelegate() {
        return delegate;
    }
}
