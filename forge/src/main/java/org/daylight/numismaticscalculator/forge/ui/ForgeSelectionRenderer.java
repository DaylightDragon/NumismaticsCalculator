package org.daylight.numismaticscalculator.forge.ui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.daylight.numismaticscalculator.ModColors;
import org.daylight.numismaticscalculator.UiState;
import org.daylight.numismaticscalculator.forge.replacements.api.ForgeAbstractContainerScreen;
import org.daylight.numismaticscalculator.replacements.IAbstractContainerScreen;
import org.daylight.numismaticscalculator.replacements.ISlot;
import org.daylight.numismaticscalculator.replacements.SingletonInstances;
import org.jetbrains.annotations.NotNull;

public class ForgeSelectionRenderer {
    public static void renderSelection(@NotNull GuiGraphics g, AbstractContainerScreen<?> screen) {
        IAbstractContainerScreen<?> veryAbstractContainerScreen = new ForgeAbstractContainerScreen<>(screen);

//        System.out.println(UiState.selectionModeActive + " " + UiState.selectionRendered  + " " + !UiState.selectionSlotValuesCoins.isEmpty());
        if(UiState.selectionModeActive && UiState.selectionRendered && !UiState.selectionSlotValuesCoins.isEmpty() && !screen.getMenu().slots.isEmpty()) {
//            System.out.println("Selection render");
//            System.out.println(UiState.selectionStartPointSlotIndex + " - " + UiState.selectionEndPointSlotIndex);
            for(Integer slotIndex : UiState.selectionSlotValuesCoins.keySet()) {
                ISlot slot = SingletonInstances.CALCULATOR_OVERLAY.getRealInventorySlot(veryAbstractContainerScreen, slotIndex); // screen.getMenu().getSlot(slotIndex);
                if(slot == null) continue;
                Integer value = UiState.selectionSlotValuesCoins.get(slotIndex);
                int color = (value != null && value > 0) ? ModColors.selectionCoins : ModColors.selectionUsual;
                g.fill(screen.getGuiLeft() + slot.x(), screen.getGuiTop() + slot.y(),
                        screen.getGuiLeft() + slot.x() + 16, screen.getGuiTop() + slot.y() + 16,
                        color);
            }
        }
    }
}
