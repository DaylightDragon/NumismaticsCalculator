package org.daylight.coinscalculator.replacements;

import net.minecraft.world.Container;

public class ForgeContainer implements IContainer {
    private final Container delegate;

    public ForgeContainer(Container delegate) {
        this.delegate = delegate;
    }

    public Container getDelegate() {
        return delegate;
    }
}
