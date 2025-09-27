package org.daylight.numismaticscalculator.replacements;

public interface IRegisterListenersEvent {
    boolean containsListener(Object listener);
    void addListener(Object listener);
    boolean isAbstractContainerScreen();
    IScreen getScreen();
    IAbstractContainerScreen<?> getAsAbstractContainerScreen();
    IModSettingsScreen getAsModSettingsScreen();
    boolean isModSettingsScreen();
}
