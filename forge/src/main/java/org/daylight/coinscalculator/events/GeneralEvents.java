package org.daylight.coinscalculator.events;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.daylight.coinscalculator.CoinsCalculator;
import org.daylight.coinscalculator.ModKeyBindings;
import org.daylight.coinscalculator.ModResources;
import org.daylight.coinscalculator.replacements.ForgeCoinValues;
import org.daylight.coinscalculator.replacements.ForgeResources;

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
