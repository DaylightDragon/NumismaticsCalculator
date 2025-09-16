package org.daylight.coinscalculator.replacements;

public interface ISlot {
    IItem getItemActual();
    IItemStack getItemStack();
    int x();
    int y();
    Class<?> getContainerClass();
}
