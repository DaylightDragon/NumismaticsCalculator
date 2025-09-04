package org.daylight.coinscalculator.ui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import org.daylight.coinscalculator.CoinValues;
import org.daylight.coinscalculator.ModColors;
import org.daylight.coinscalculator.UiState;
import org.spongepowered.asm.mixin.injection.struct.InjectorGroupInfo;

import java.util.Map;

public class SelectionRenderer {
    public static void renderSelection(GuiGraphics g, AbstractContainerScreen<?> screen) {
        if(UiState.selectionModeActive && UiState.selectionRendered && !UiState.selectionSlotValuesCoins.isEmpty()) {
//            System.out.println(UiState.selectionStartPointSlotIndex + " - " + UiState.selectionEndPointSlotIndex);
            for(Integer slotIndex : UiState.selectionSlotValuesCoins.keySet()) {
                Slot slot = screen.getMenu().getSlot(slotIndex);
                int color = UiState.selectionSlotValuesCoins.get(slotIndex) != null ? ModColors.selectionCoinsColor : ModColors.selectionUsualColor;
                g.fill(screen.getGuiLeft() + slot.x, screen.getGuiTop() + slot.y, screen.getGuiLeft() + slot.x + 16, screen.getGuiTop() + slot.y + 16, color);
            }
        }
    }
}
