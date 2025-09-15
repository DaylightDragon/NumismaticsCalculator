package org.daylight.coinscalculator.replacements;

public interface IEditBoxFactory {
    IEditBox create(IFont font, int x, int y, int width, int height, IComponent component);
}
