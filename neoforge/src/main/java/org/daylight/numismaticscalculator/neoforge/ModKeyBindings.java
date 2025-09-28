package org.daylight.numismaticscalculator.neoforge;

import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.daylight.numismaticscalculator.neoforge.ui.screens.NeoForgeModSettingsScreenImpl;
import org.daylight.numismaticscalculator.replacements.SingletonInstances;
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
        NeoForge.EVENT_BUS.addListener(ModKeyBindings::onKeyInput);
    }

    private static void onKeyInput(InputEvent.Key event) {
        if (TOGGLE_GUI.consumeClick()) {
            SingletonInstances.GUI_MANAGER_OVERLAY.toggleMainOverlayState();
        }
        if (MOD_SETTINGS.consumeClick()) {
            NeoForgeModSettingsScreenImpl.setAsScreen();
        }
    }
}
