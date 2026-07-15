package org.daylight.numismaticscalculator.fabric;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import org.daylight.numismaticscalculator.fabric.client.CoinsCalculator;
import org.daylight.numismaticscalculator.fabric.ui.FabricModSettingsScreenImpl;
import org.daylight.numismaticscalculator.replacements.SingletonInstances;
import org.lwjgl.glfw.GLFW;

public class ModKeyBindings {
    public static KeyMapping TOGGLE_GUI;
    public static KeyMapping MOD_SETTINGS;

    private static boolean prevToggleDown = false;
    private static boolean prevSettingsDown = false;

    public static void register() {
        TOGGLE_GUI = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key." + CoinsCalculator.MOD_ID + ".toggle_overlay",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                "key.categories." + CoinsCalculator.MOD_ID
        ));

        MOD_SETTINGS = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key." + CoinsCalculator.MOD_ID + ".mod_settings",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                "key.categories." + CoinsCalculator.MOD_ID
        ));

        // Обработка нажатий
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client == null || client.getWindow() == null) return;
            long window = client.getWindow().getWindow();

            // TOGGLE GUI
            InputConstants.Key toggleBound = TOGGLE_GUI.getDefaultKey();
            if (toggleBound.getType() == InputConstants.Type.KEYSYM) {
                boolean down = InputConstants.isKeyDown(window, toggleBound.getValue());
                if (down && !prevToggleDown && !isTextFieldFocused(client)) {
                    SingletonInstances.GUI_MANAGER_OVERLAY.toggleMainOverlayState();
                }
                prevToggleDown = down;
            }

            // MOD SETTINGS
            InputConstants.Key settingsBound = MOD_SETTINGS.getDefaultKey();
            if (settingsBound.getType() == InputConstants.Type.KEYSYM) {
                boolean down = InputConstants.isKeyDown(window, settingsBound.getValue());
                if (down && !prevSettingsDown && !isTextFieldFocused(client)) {
                    FabricModSettingsScreenImpl.setAsScreen();
                }
                prevSettingsDown = down;
            }
        });


    }

    private static boolean isTextFieldFocused(Minecraft client) {
        if (client.screen == null) return false;
        return client.screen.getFocused() instanceof EditBox;
        // delegate.isFocused()
    }
}
