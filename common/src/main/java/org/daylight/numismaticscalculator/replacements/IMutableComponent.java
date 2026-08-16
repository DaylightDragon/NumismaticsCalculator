package org.daylight.numismaticscalculator.replacements;

public interface IMutableComponent extends IComponent {
    IMutableComponent append(String text);
    IMutableComponent append(IComponent component);
}
