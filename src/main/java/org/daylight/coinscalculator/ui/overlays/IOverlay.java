package org.daylight.coinscalculator.ui.overlays;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ScreenEvent;
import org.daylight.coinscalculator.ModResources;
import org.daylight.coinscalculator.UiState;
import org.daylight.coinscalculator.events.InputEvents;
import org.daylight.coinscalculator.ui.elements.UIButton;
import org.daylight.coinscalculator.ui.elements.UIHorizontalLayout;
import org.daylight.coinscalculator.ui.elements.UIPanel;
import org.daylight.coinscalculator.ui.elements.UIVerticalLayout;
import org.daylight.coinscalculator.ui.screens.ModSettingsScreen;
import org.daylight.coinscalculator.util.tuples.Quartet;
import org.jetbrains.annotations.NotNull;

public interface IOverlay {
    void render(@NotNull GuiGraphics guiGraphics, float partialTick, Integer mouseX, Integer mouseY);

//    void init(AbstractContainerScreen<?> screen);

//    Quartet<Integer, Integer, Integer, Integer> getOverlayBoundsForScreen(AbstractContainerScreen<?> screen);

    boolean onMouseClick(double mouseX, double mouseY, int button, Screen screenOrig);
    boolean onMouseDrag(double mouseX, double mouseY, int button, Screen screenOrig);
    void onMouseRelease(double mouseX, double mouseY, int button, Screen screenOrig);

    void updateOverlayPosition(Screen screen);
    void onKeyPressed(InputEvent.Key event);
}
