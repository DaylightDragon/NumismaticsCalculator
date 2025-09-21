package org.daylight.coinscalculator.ui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.daylight.coinscalculator.ModColors;
import org.daylight.coinscalculator.UiState;
import org.daylight.coinscalculator.mixins.HandledScreenAccessor;
import org.daylight.coinscalculator.replacements.IAbstractContainerScreen;
import org.daylight.coinscalculator.replacements.ISlot;
import org.daylight.coinscalculator.replacements.SingletonInstances;
import org.daylight.coinscalculator.replacements.api.FabricAbstractContainerScreen;
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
