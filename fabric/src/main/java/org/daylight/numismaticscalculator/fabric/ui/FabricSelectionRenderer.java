package org.daylight.numismaticscalculator.fabric.ui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.daylight.numismaticscalculator.ModColors;
import org.daylight.numismaticscalculator.UiState;
import org.daylight.numismaticscalculator.fabric.mixins.HandledScreenAccessor;
import org.daylight.numismaticscalculator.fabric.replacements.api.FabricAbstractContainerScreen;
import org.daylight.numismaticscalculator.replacements.IAbstractContainerScreen;
import org.daylight.numismaticscalculator.replacements.ISlot;
import org.daylight.numismaticscalculator.replacements.SingletonInstances;
import org.jetbrains.annotations.NotNull;

public class FabricSelectionRenderer {
    public static void renderSelection(@NotNull DrawContext g, HandledScreen<?> screen) {
        IAbstractContainerScreen<?> veryAbstractContainerScreen = new FabricAbstractContainerScreen<>(screen);

        if(UiState.selectionModeActive && UiState.selectionRendered && !UiState.selectionSlotValuesCoins.isEmpty() && !screen.getScreenHandler().slots.isEmpty()) {
//            System.out.println("Selection render");
            if(!(screen instanceof HandledScreenAccessor handledScreenAccessor)) return;
//            System.out.println("handledScreenAccessor " + handledScreenAccessor.getGuiLeft() + " " + handledScreenAccessor.getGuiTop());
//            System.out.println(UiState.selectionStartPointSlotIndex + " - " + UiState.selectionEndPointSlotIndex);
            for(Integer slotIndex : UiState.selectionSlotValuesCoins.keySet()) {
                ISlot slot = SingletonInstances.CALCULATOR_OVERLAY.getRealInventorySlot(veryAbstractContainerScreen, slotIndex); // screen.getMenu().getSlot(slotIndex);
                if(slot == null) continue;
                Integer value = UiState.selectionSlotValuesCoins.get(slotIndex);
                int color = (value != null && value > 0) ? ModColors.selectionCoins : ModColors.selectionUsual;
                g.fill(handledScreenAccessor.getGuiLeft() + slot.x(), handledScreenAccessor.getGuiTop() + slot.y(),
                        handledScreenAccessor.getGuiLeft() + slot.x() + 16, handledScreenAccessor.getGuiTop() + slot.y() + 16,
                        color);
            }
        }
    }
}
