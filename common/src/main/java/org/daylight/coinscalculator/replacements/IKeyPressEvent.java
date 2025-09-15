package org.daylight.coinscalculator.replacements;

public interface IKeyPressEvent {
    int getKey();
    int getScanCode();
    int getAction();
    boolean isActionPress();
    boolean isActionRepeat();
    boolean isBackspacePressed();
    boolean isDeletePressed();
    boolean isLeftPressed();
    boolean isRightPressed();
    String getKeyName();
}
