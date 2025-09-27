package org.daylight.numismaticscalculator.fabric.replacements.api;

import net.minecraft.text.Text;
import org.daylight.numismaticscalculator.replacements.IComponent;

public class FabricComponent implements IComponent {
    private Text delegate;
    public FabricComponent(Text delegate) {
        this.delegate = delegate;
    }

    public Text getDelegate() {
        return delegate;
    }
}
