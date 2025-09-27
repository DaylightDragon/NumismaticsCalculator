package org.daylight.numismaticscalculator.events;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.daylight.numismaticscalculator.CoinsCalculator;
import org.daylight.numismaticscalculator.replacements.SingletonInstances;
import org.daylight.numismaticscalculator.replacements.api.ForgeGuiGraphics;

@Mod.EventBusSubscriber(modid = CoinsCalculator.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GUIEvents {
    @SubscribeEvent
    public static void onRegisterGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("coin_calculator_overlay", (gui, graphics, partialTick, width, height) -> {
            if (Minecraft.getInstance().screen == null) {
//                System.out.println("render overlay");
                SingletonInstances.CALCULATOR_OVERLAY.render(new ForgeGuiGraphics(graphics), partialTick, InputEvents.getMouseX(), InputEvents.getMouseY());
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
