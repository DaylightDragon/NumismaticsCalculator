package org.daylight.coinscalculator.replacements;


public interface IConfigValue<T> {
    T get();
    void set(T value);
}
