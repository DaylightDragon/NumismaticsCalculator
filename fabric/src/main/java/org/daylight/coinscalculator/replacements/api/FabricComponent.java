package org.daylight.coinscalculator.replacements.api;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.Text;
import org.daylight.coinscalculator.replacements.IComponent;
import org.daylight.coinscalculator.replacements.IFont;
import org.jetbrains.annotations.NotNull;

public class FabricComponent implements IComponent {
    private Text delegate;
    public FabricComponent(Text delegate) {
        this.delegate = delegate;
    }

    public Text getDelegate() {
        return delegate;
    }
}
