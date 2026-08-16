package org.daylight.numismaticscalculator.fabric.replacements.api;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.daylight.numismaticscalculator.replacements.IComponent;
import org.daylight.numismaticscalculator.replacements.IMutableComponent;

public class FabricMutableComponent implements IMutableComponent {
    private MutableText delegate;
    public FabricMutableComponent(MutableText delegate) {
        this.delegate = delegate;
    }

    public MutableText getDelegate() {
        return delegate;
    }

    @Override
    public String getString() {
        return delegate.getString();
    }

    @Override
    public IMutableComponent copy() {
        return new FabricMutableComponent(delegate.copy());
    }

    @Override
    public IMutableComponent empty() {
        return new FabricMutableComponent(Text.empty());
    }

    @Override
    public IMutableComponent literal(String text) {
        return new FabricMutableComponent(Text.literal(text));
    }

    @Override
    public IMutableComponent translatable(String key) {
        return new FabricMutableComponent(Text.translatable(key));
    }

    @Override
    public IMutableComponent translatable(String key, Object... args) {
        return new FabricMutableComponent(Text.translatable(key, args));
    }


    @Override
    public IMutableComponent append(String text) {
        delegate.append(text);
        return this;
    }

    @Override
    public IMutableComponent append(IComponent component) {
        delegate.append(((FabricComponent) component).getDelegate());
        return this;
    }
}
