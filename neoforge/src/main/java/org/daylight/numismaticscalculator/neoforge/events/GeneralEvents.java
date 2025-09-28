package org.daylight.numismaticscalculator.neoforge.events;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.daylight.numismaticscalculator.neoforge.ModKeyBindings;
import org.daylight.numismaticscalculator.neoforge.replacements.NeoForgeCoinValues;

public class GeneralEvents {
    @SubscribeEvent
    public void onClientSetup(FMLClientSetupEvent event) {
        ModKeyBindings.init();
    }

    @SubscribeEvent
    public void registerKeyMappings(RegisterKeyMappingsEvent event) {
        ModKeyBindings.register(event);
    }

    @SubscribeEvent
    public void onModelsReady(ModelEvent.BakingCompleted event) {
        NeoForgeCoinValues.init();
    }
}
