package org.daylight.numismaticscalculator.replacements;

public interface IComponent {
    String getString();
    IMutableComponent copy();
    IMutableComponent empty();
    IMutableComponent literal(String text);
    IMutableComponent translatable(String key);
    IMutableComponent translatable(String key, Object... args);
}
