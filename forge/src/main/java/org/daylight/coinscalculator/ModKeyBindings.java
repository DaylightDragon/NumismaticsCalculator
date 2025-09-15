package org.daylight.coinscalculator;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import org.daylight.coinscalculator.ui.overlays.GuiManagerOverlay;
import org.daylight.coinscalculator.ui.screens.ModSettingsScreen;
import org.lwjgl.glfw.GLFW;

public class ModKeyBindings {
    public static final KeyMapping TOGGLE_GUI = new KeyMapping("key." + CoinsCalculator.MODID + ".toggle_overlay",
            GLFW.GLFW_KEY_G,
            "key.categories." + CoinsCalculator.MODID);

    public static final KeyMapping MOD_SETTINGS = new KeyMapping("key." + CoinsCalculator.MODID + ".mod_settings",
            GLFW.GLFW_KEY_K,
            "key.categories." + CoinsCalculator.MODID);

    public static void register(RegisterKeyMappingsEvent event) {
        event.register(TOGGLE_GUI);
        event.register(MOD_SETTINGS);
    }

    public static void init() {
        MinecraftForge.EVENT_BUS.addListener(ModKeyBindings::onKeyInput);
    }

    private static void onKeyInput(InputEvent.Key event) {
        if (TOGGLE_GUI.consumeClick()) {
            GuiManagerOverlay.toggleMainOverlayState();
        }
        if (MOD_SETTINGS.consumeClick()) {
            ModSettingsScreen.setAsScreen();
        }
    }
}
