package org.daylight.coinscalculator.replacements;

public interface IScreen {
    int width();
    int height();
    IAbstractContainerScreen<?> getAsAbstractContainerScreen();
    boolean isModSettingsScreen();
    IModSettingsScreen getAsModSettingsScreen();
    boolean isAbstractContainerScreen();
}
