package org.daylight.numismaticscalculator.replacements;

public interface IAbstractContainerScreen<T> extends IScreen {
    int getGuiLeft();
    int getGuiTop();
    int countSlots();
    IAbstractContainerMenu getMenu();
    ISlot getSlotUnderMouse();
}
