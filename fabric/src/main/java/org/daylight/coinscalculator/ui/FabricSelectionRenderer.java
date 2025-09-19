package org.daylight.coinscalculator.ui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.daylight.coinscalculator.ModColors;
import org.daylight.coinscalculator.UiState;
import org.daylight.coinscalculator.replacements.IAbstractContainerScreen;
import org.daylight.coinscalculator.replacements.ISlot;
import org.daylight.coinscalculator.replacements.SingletonInstances;
import org.daylight.coinscalculator.replacements.api.FabricAbstractContainerScreen;
import org.jetbrains.annotations.NotNull;

public class FabricSelectionRenderer {
    public static void renderSelection(@NotNull DrawContext g, HandledScreen<?> screen) {
        IAbstractContainerScreen<?> veryAbstractContainerScreen = new FabricAbstractContainerScreen(screen);

//        System.out.println(UiState.selectionModeActive + " " + UiState.selectionRendered  + " " + !UiState.selectionSlotValuesCoins.isEmpty());
        if(UiState.selectionModeActive && UiState.selectionRendered && !UiState.selectionSlotValuesCoins.isEmpty() && !screen.getScreenHandler().slots.isEmpty()) {
//            System.out.println("Selection render");
//            System.out.println(UiState.selectionStartPointSlotIndex + " - " + UiState.selectionEndPointSlotIndex);
            for(Integer slotIndex : UiState.selectionSlotValuesCoins.keySet()) {
                ISlot slot = SingletonInstances.CALCULATOR_OVERLAY.getRealInventorySlot(veryAbstractContainerScreen, slotIndex); // screen.getMenu().getSlot(slotIndex);
                int color = UiState.selectionSlotValuesCoins.get(slotIndex) != null ? ModColors.selectionCoins : ModColors.selectionUsual;
                g.fill(screen.getNavigationFocus().getLeft() + slot.x(), screen.getNavigationFocus().getTop() + slot.y(),
                        screen.getNavigationFocus().getLeft() + slot.x() + 16, screen.getNavigationFocus().getTop() + slot.y() + 16,
                        color);
            }
        }
    }
}
