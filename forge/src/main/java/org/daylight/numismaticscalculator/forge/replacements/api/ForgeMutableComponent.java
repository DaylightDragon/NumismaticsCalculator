package org.daylight.numismaticscalculator.forge.replacements.api;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.daylight.numismaticscalculator.replacements.IComponent;
import org.daylight.numismaticscalculator.replacements.IMutableComponent;

public class ForgeMutableComponent implements IMutableComponent {
    private MutableComponent delegate;
    public ForgeMutableComponent(MutableComponent delegate) {
        this.delegate = delegate;
    }

    public MutableComponent getDelegate() {
        return delegate;
    }

    @Override
    public String getString() {
        return delegate.getString();
    }

    @Override
    public IMutableComponent copy() {
        return new ForgeMutableComponent(delegate.copy());
    }

    @Override
    public IMutableComponent empty() {
        return new ForgeMutableComponent(Component.empty());
    }

    @Override
    public IMutableComponent literal(String text) {
        return new ForgeMutableComponent(Component.literal(text));
    }

    @Override
    public IMutableComponent translatable(String key) {
        return new ForgeMutableComponent(Component.translatable(key));
    }

    @Override
    public IMutableComponent translatable(String key, Object... args) {
        return new ForgeMutableComponent(Component.translatable(key, args));
    }


    @Override
    public IMutableComponent append(String text) {
        delegate.append(text);
        return this;
    }

    @Override
    public IMutableComponent append(IComponent component) {
        delegate.append(((ForgeComponent) component).getDelegate());
        return this;
    }
}
