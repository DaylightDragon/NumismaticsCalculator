package org.daylight.numismaticscalculator.replacements;


public interface IConfigValue<T> {
    T get();
    void set(T value);
}
