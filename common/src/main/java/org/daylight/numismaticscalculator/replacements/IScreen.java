package org.daylight.numismaticscalculator.replacements;

public interface IScreen {
    int width();
    int height();
    IAbstractContainerScreen<?> getAsAbstractContainerScreen();
    boolean isModSettingsScreen();
    IModSettingsScreen getAsModSettingsScreen();
    boolean isAbstractContainerScreen();
}
