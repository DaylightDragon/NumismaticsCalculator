package org.daylight.coinscalculator.events;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.daylight.coinscalculator.ModKeyBindings;
import org.daylight.coinscalculator.ui.SelectionRenderer;
import org.daylight.coinscalculator.ui.overlays.CalculatorOverlay;
import org.daylight.coinscalculator.ui.overlays.GuiManagerOverlay;
import org.daylight.coinscalculator.ui.overlays.ModSettingsOverlay;
import org.daylight.coinscalculator.ui.screens.ModSettingsScreen;

public class ScreenEvents {
    @SubscribeEvent
    public void onClientSetup(FMLClientSetupEvent event) {

    }

    @SubscribeEvent
    public void onScreenRender(ScreenEvent.Render.Post event) {
        if (CalculatorOverlay.shouldRenderOnScreen(event.getScreen())) {
            CalculatorOverlay.getInstance().render(event.getGuiGraphics(), event.getPartialTick(), event.getMouseX(), event.getMouseY());
            SelectionRenderer.renderSelection(event.getGuiGraphics(), (AbstractContainerScreen<?>) event.getScreen());
        }
        GuiManagerOverlay.getInstance().render(event.getGuiGraphics(), event.getPartialTick(), event.getMouseX(), event.getMouseY());
        ModSettingsOverlay.getInstance().render(event.getGuiGraphics(), event.getPartialTick(), event.getMouseX(), event.getMouseY());
    }

    @SubscribeEvent
    public void onScreenInit(ScreenEvent.Init.Post event) {
        if (CalculatorOverlay.shouldRenderOnScreen(event.getScreen())) {
//            System.out.println("Relink main");
            CalculatorOverlay.getInstance().relinkListeners(event);
            CalculatorOverlay.getInstance().updateOverlayPosition(event.getScreen());
        }
        if(GuiManagerOverlay.shouldRenderOnScreen(event.getScreen())) {
            GuiManagerOverlay.getInstance().relinkListeners(event);
            GuiManagerOverlay.getInstance().updateOverlayPosition(event.getScreen());
        }
        if(ModSettingsOverlay.shouldRenderOnScreen(event.getScreen())) {
            ModSettingsOverlay.getInstance().relinkListeners(event);
            ModSettingsOverlay.getInstance().updateOverlayPosition(event.getScreen());
        }
    }

    @SubscribeEvent
    public void onScreenOpen(ScreenEvent.Opening event) {
//        Screen newScreen = event.getScreen();
        CalculatorOverlay.getInstance().onScreenChange(event.getScreen());
    }

    @SubscribeEvent
    public void onScreenKey(ScreenEvent.KeyPressed.Post event) {
        if (event.getScreen() instanceof AbstractContainerScreen<?>) {
            InputConstants.Key key = InputConstants.getKey(event.getKeyCode(), event.getScanCode());
            if (ModKeyBindings.TOGGLE_GUI.isActiveAndMatches(key)) {
                GuiManagerOverlay.toggleMainOverlayState();
            }
            if (ModKeyBindings.MOD_SETTINGS.isActiveAndMatches(key)) {
                ModSettingsScreen.setAsScreen();
            }
        }
    }

    // Unused
    @SubscribeEvent
    public void onRenderOverlay(RenderGuiOverlayEvent.Post event) {
//        Minecraft mc = Minecraft.getInstance();
//        if(mc.screen == null) return;
////        if (event.getOverlay() != VanillaGuiOverlay.ALL) return;
//
////        CoinsCalculator.LOGGER.info(mc.screen.getClass().getSimpleName());
//        if (!(mc.screen instanceof InventoryScreen || mc.screen instanceof CreativeModeInventoryScreen)) return;
////        CoinsCalculator.LOGGER.info("INVENTORY");
//
//        int width = event.getWindow().getGuiScaledWidth();
//        int height = event.getWindow().getGuiScaledHeight();
//
//        int x = width / 2 + 10;  // пример позиции справа от игрока
//        int y = height / 2 - 20; // пример позиции выше центра
//
//        MainWidget.drawWidget(mc, event, x, y);
    }
}
