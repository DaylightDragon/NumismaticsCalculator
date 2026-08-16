package org.daylight.numismaticscalculator.replacements;

public interface IComponentFactory {
    IMutableComponent empty();
    IMutableComponent literal(String text);
    IMutableComponent translatable(String key);
    IMutableComponent translatable(String key, Object... args);
}
