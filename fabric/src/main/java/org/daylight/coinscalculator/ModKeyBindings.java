package org.daylight.coinscalculator;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.daylight.coinscalculator.replacements.SingletonInstances;
import org.daylight.coinscalculator.ui.FabricModSettingsScreenImpl;
import org.lwjgl.glfw.GLFW;

public class ModKeyBindings {
    public static KeyBinding TOGGLE_GUI;
    public static KeyBinding MOD_SETTINGS;

    public static void register() {
        TOGGLE_GUI = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key." + CoinsCalculator.MOD_ID + ".toggle_overlay",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                "key.categories." + CoinsCalculator.MOD_ID
        ));

        MOD_SETTINGS = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key." + CoinsCalculator.MOD_ID + ".mod_settings",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                "key.categories." + CoinsCalculator.MOD_ID
        ));

        // Обработка нажатий
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (TOGGLE_GUI.wasPressed()) {
                SingletonInstances.GUI_MANAGER_OVERLAY.toggleMainOverlayState();
            }
            if (MOD_SETTINGS.wasPressed()) {
                FabricModSettingsScreenImpl.setAsScreen();
            }
        });
    }
}
