package org.daylight.numismaticscalculator.replacements;

import java.util.List;

public interface IMinecraftUtilities {
    IFont getMinecraftFont();
    void execute(Runnable runnable);
    List<IItemStack> getInventoryItems();
    IScreen getScreen();
}
