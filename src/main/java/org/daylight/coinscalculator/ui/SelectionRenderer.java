package org.daylight.coinscalculator.ui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import org.daylight.coinscalculator.ModColors;
import org.daylight.coinscalculator.UiState;
import org.daylight.coinscalculator.ui.overlays.CalculatorOverlay;
import org.jetbrains.annotations.NotNull;

public class SelectionRenderer {
    public static void renderSelection(@NotNull GuiGraphics g, AbstractContainerScreen<?> screen) {
        if(UiState.selectionModeActive && UiState.selectionRendered && !UiState.selectionSlotValuesCoins.isEmpty()) {
//            System.out.println("Selection render");
//            System.out.println(UiState.selectionStartPointSlotIndex + " - " + UiState.selectionEndPointSlotIndex);
            for(Integer slotIndex : UiState.selectionSlotValuesCoins.keySet()) {
                Slot slot = CalculatorOverlay.getRealInventorySlot(screen, slotIndex); // screen.getMenu().getSlot(slotIndex);
                int color = UiState.selectionSlotValuesCoins.get(slotIndex) != null ? ModColors.selectionCoins : ModColors.selectionUsual;
                g.fill(screen.getGuiLeft() + slot.x, screen.getGuiTop() + slot.y, screen.getGuiLeft() + slot.x + 16, screen.getGuiTop() + slot.y + 16, color);
            }
        }
    }
}
