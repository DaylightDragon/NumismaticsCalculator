package org.daylight.numismaticscalculator.neoforge.events;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import org.daylight.numismaticscalculator.neoforge.CoinsCalculator;
import org.daylight.numismaticscalculator.neoforge.events.InputEvents;
import org.daylight.numismaticscalculator.neoforge.replacements.api.NeoForgeGuiGraphics;
import org.daylight.numismaticscalculator.replacements.SingletonInstances;

public class GUIEvents {
    @SubscribeEvent
    public void onRegisterGuiOverlays(RegisterGuiLayersEvent event) {
        event.registerAboveAll(ResourceLocation.fromNamespaceAndPath(CoinsCalculator.MODID, "coin_calculator_overlay"), (graphics, deltaTracker) -> {
            if (Minecraft.getInstance().screen == null) {
//                System.out.println("render overlay");
                SingletonInstances.CALCULATOR_OVERLAY.render(new NeoForgeGuiGraphics(graphics), deltaTracker.getGameTimeDeltaPartialTick(true), InputEvents.getMouseX(), InputEvents.getMouseY());
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
