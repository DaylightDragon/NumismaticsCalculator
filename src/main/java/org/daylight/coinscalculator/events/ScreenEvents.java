package org.daylight.coinscalculator.events;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.daylight.coinscalculator.ui.SelectionRenderer;
import org.daylight.coinscalculator.ui.overlays.CalculatorOverlay;
import org.daylight.coinscalculator.ui.overlays.GuiManagerOverlay;

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
    }

    @SubscribeEvent
    public void onScreenInit(ScreenEvent.Init.Post event) {
        if (CalculatorOverlay.shouldRenderOnScreen(event.getScreen())) {
//            System.out.println("Relink main");
            CalculatorOverlay.getInstance().relinkListeners(event);
        }
        if(GuiManagerOverlay.shouldRenderOnScreen(event.getScreen())) {
            GuiManagerOverlay.getInstance().relinkListeners(event);
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
