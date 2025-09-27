package org.daylight.numismaticscalculator;

import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.daylight.numismaticscalculator.events.GUIEvents;
import org.daylight.numismaticscalculator.events.InventoryChangeEventsForge;
import org.daylight.numismaticscalculator.events.ScreenEvents;
import org.daylight.numismaticscalculator.events.TooltipEvents;
import org.daylight.numismaticscalculator.replacements.*;
import org.daylight.numismaticscalculator.replacements.api.ForgeComponentFactory;
import org.daylight.numismaticscalculator.replacements.api.ForgeEditBoxFactory;
import org.daylight.numismaticscalculator.replacements.api.ForgeRenderSystem;
import org.daylight.numismaticscalculator.ui.overlays.ForgeCalculatorOverlay;
import org.daylight.numismaticscalculator.ui.overlays.ForgeGuiManagerOverlay;
import org.daylight.numismaticscalculator.ui.overlays.ForgeModSettingsOverlay;
import org.daylight.numismaticscalculator.util.ForgeDrawingUtils;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CoinsCalculator.MODID)
public class CoinsCalculator
{
    public static final String MODID = "numismaticscalculator";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CoinsCalculator() {}

    public CoinsCalculator(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        registerCommonSingletonInstances();
//        ConfigHandler2.load();
//        context.registerConfig(ModConfig.Type.CLIENT, ConfigHandler.SPEC);

        modEventBus.addListener(this::commonSetup);

//        MinecraftForge.EVENT_BUS.register(TooltipEvents.class);
//        MinecraftForge.EVENT_BUS.register(GUIEvents.class);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new ScreenEvents());
        MinecraftForge.EVENT_BUS.addListener(TooltipEvents::onTooltip);
//        context.getModEventBus().addListener(MainEvents::onRegisterGuiOverlays);
//        context.getModEventBus().addListener(GUIEvents::onRegisterGuiOverlays);
//        context.getModEventBus().addListener(GUIEvents::renderOverlay); // bad
        context.getModEventBus().addListener(GUIEvents::onRegisterGuiOverlays);

        InventoryChangeEventsForge.init();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

        }
    }

    private void registerCommonSingletonInstances() {
        SingletonInstances.DRAWING_UTILS = new ForgeDrawingUtils();
        SingletonInstances.RENDER_SYSTEM = new ForgeRenderSystem();
        SingletonInstances.MOD_RESOURCES = new ForgeResources();
        SingletonInstances.EDITBOXES = new ForgeEditBoxFactory();
        SingletonInstances.COMPONENTS = new ForgeComponentFactory();
        SingletonInstances.COIN_VALUES = new ForgeCoinValues();
        SingletonInstances.MINECRAFT_UTILS = new ForgeMinecraftUtilities();
        SingletonInstances.INPUT_UTILS = new ForgeInputUtils();

        SingletonInstances.CALCULATOR_OVERLAY = new ForgeCalculatorOverlay();
        SingletonInstances.GUI_MANAGER_OVERLAY = new ForgeGuiManagerOverlay();
        SingletonInstances.MOD_SETTINGS_OVERLAY = new ForgeModSettingsOverlay();
    }
}
