package org.daylight.numismaticscalculator.replacements;

public interface IModResources {
    enum Type {
        SELECTION_DEFAULT,
        SELECTION_ACTIVE,
        GEAR_ICON,
        CURRENCY_ICON,
        CHECKBOX_NORMAL,
        CHECKBOX_SELECTED,
        SUM_ICON,
        WEIGHING_MACHINE_ICON
    }

    IResourceLocation getResourceLocation(Type type);
}
