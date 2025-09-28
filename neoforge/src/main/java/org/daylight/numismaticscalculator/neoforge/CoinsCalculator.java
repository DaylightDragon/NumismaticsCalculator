package org.daylight.numismaticscalculator.neoforge;

import com.mojang.logging.LogUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.ClientNeoForgeMod;
import net.neoforged.neoforge.client.loading.ClientModLoader;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.daylight.numismaticscalculator.neoforge.config.ConfigHandler;
import org.daylight.numismaticscalculator.neoforge.events.*;
import org.daylight.numismaticscalculator.neoforge.replacements.NeoForgeCoinValues;

import org.daylight.numismaticscalculator.neoforge.replacements.NeoForgeMinecraftUtilities;
import org.daylight.numismaticscalculator.neoforge.replacements.NeoForgeResources;
import org.daylight.numismaticscalculator.neoforge.replacements.api.NeoForgeComponentFactory;
import org.daylight.numismaticscalculator.neoforge.replacements.api.NeoForgeEditBoxFactory;
import org.daylight.numismaticscalculator.neoforge.replacements.api.NeoForgeRenderSystem;
import org.daylight.numismaticscalculator.neoforge.ui.overlays.NeoForgeCalculatorOverlay;
import org.daylight.numismaticscalculator.neoforge.ui.overlays.NeoForgeGuiManagerOverlay;
import org.daylight.numismaticscalculator.neoforge.ui.overlays.NeoForgeModSettingsOverlay;
import org.daylight.numismaticscalculator.neoforge.util.ForgeDrawingUtils;
import org.daylight.numismaticscalculator.neoforge.replacements.NeoForgeInputUtils;
import org.daylight.numismaticscalculator.replacements.SingletonInstances;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CoinsCalculator.MODID)
public class CoinsCalculator
{
    public static final String MODID = "numismaticscalculator";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CoinsCalculator(ModContainer context) {
        IEventBus modEventBus = context.getEventBus();
        assert modEventBus != null;

        registerCommonSingletonInstances();
//        ConfigHandler2.load();
        context.registerConfig(ModConfig.Type.CLIENT, ConfigHandler.SPEC);

//        MinecraftForge.EVENT_BUS.register(TooltipEvents.class);
//        MinecraftForge.EVENT_BUS.register(GUIEvents.class);

//        modEventBus.register(new ScreenEvents());
        NeoForge.EVENT_BUS.register(new ScreenEvents());
        NeoForge.EVENT_BUS.addListener(TooltipEvents::onTooltip);
//        context.getModEventBus().addListener(MainEvents::onRegisterGuiOverlays);
//        context.getModEventBus().addListener(GUIEvents::onRegisterGuiOverlays);
//        context.getModEventBus().addListener(GUIEvents::renderOverlay); // bad

        modEventBus.register(new GUIEvents());
        modEventBus.register(new GeneralEvents());

//        NeoForge.EVENT_BUS.register(new InputEvents());
//        modEventBus.register(new InputTickEvents());
//        InputTickEvents.register(modEventBus);

        InventoryChangeEventsNeoForge.register(NeoForge.EVENT_BUS); // TODO should have been client dist only

        InventoryChangeEventsNeoForge.init();
    }

    private void registerCommonSingletonInstances() {
        SingletonInstances.DRAWING_UTILS = new ForgeDrawingUtils();
        SingletonInstances.RENDER_SYSTEM = new NeoForgeRenderSystem();
        SingletonInstances.MOD_RESOURCES = new NeoForgeResources();
        SingletonInstances.EDITBOXES = new NeoForgeEditBoxFactory();
        SingletonInstances.COMPONENTS = new NeoForgeComponentFactory();
        SingletonInstances.COIN_VALUES = new NeoForgeCoinValues();
        SingletonInstances.MINECRAFT_UTILS = new NeoForgeMinecraftUtilities();
        SingletonInstances.INPUT_UTILS = new NeoForgeInputUtils();

        SingletonInstances.CALCULATOR_OVERLAY = new NeoForgeCalculatorOverlay();
        SingletonInstances.GUI_MANAGER_OVERLAY = new NeoForgeGuiManagerOverlay();
        SingletonInstances.MOD_SETTINGS_OVERLAY = new NeoForgeModSettingsOverlay();
    }
}
