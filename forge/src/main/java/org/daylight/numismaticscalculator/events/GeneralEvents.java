package org.daylight.numismaticscalculator.events;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.daylight.numismaticscalculator.CoinsCalculator;
import org.daylight.numismaticscalculator.ModKeyBindings;
import org.daylight.numismaticscalculator.replacements.ForgeCoinValues;

@Mod.EventBusSubscriber(modid = CoinsCalculator.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class GeneralEvents {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        ModKeyBindings.init();
    }

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        ModKeyBindings.register(event);
    }

    @SubscribeEvent
    public static void onModelsReady(ModelEvent.BakingCompleted event) {
        ForgeCoinValues.init();
    }
}
