package org.daylight.coinscalculator.events;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.daylight.coinscalculator.CoinsCalculator;
import org.daylight.coinscalculator.ui.overlays.CalculatorOverlay;
import org.daylight.coinscalculator.ui.overlays.CoinsOverlay;

@Mod.EventBusSubscriber(modid = CoinsCalculator.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GUIEvents {
    @SubscribeEvent
    public static void onRegisterGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("coins_overlay", new CoinsOverlay());
//        event.registerAboveAll("coin_calculator_overlay", new CalculatorOverlay());
        event.registerAboveAll("coin_calculator_overlay2", (gui, graphics, partialTick, width, height) -> {
            if (Minecraft.getInstance().screen == null) {
//                System.out.println("render overlay");
                CalculatorOverlay.getInstance().render(graphics, partialTick, width, height, 0, 0);
            }
        });
        CoinsCalculator.LOGGER.info("Registering overlays");
    }

//    @SubscribeEvent(priority = EventPriority.LOWEST)
//    public static void renderOverlay(RenderGuiOverlayEvent.Post event) {
//        if(event.getPhase() != EventPriority.LOWEST) return;
//        if(Minecraft.getInstance().screen == null) {
//            System.out.println("renderOverlay");
//            CalculatorOverlay.getInstance().render(event.getGuiGraphics(), event.getPartialTick(), event.getWindow().getGuiScaledWidth(), event.getWindow().getGuiScaledHeight(), 0, 0);
//        }
//    }
}
