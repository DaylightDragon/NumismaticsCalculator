package org.daylight.coinscalculator;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.daylight.coinscalculator.replacements.SingletonInstances;
import org.daylight.coinscalculator.ui.FabricModSettingsScreenImpl;
import org.lwjgl.glfw.GLFW;

public class ModKeyBindings {
    public static KeyBinding TOGGLE_GUI;
    public static KeyBinding MOD_SETTINGS;

    private static boolean prevToggleDown = false;
    private static boolean prevSettingsDown = false;

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
            if (client == null || client.getWindow() == null) return;
            long window = client.getWindow().getHandle();

            // TOGGLE GUI
            InputUtil.Key toggleBound = TOGGLE_GUI.getDefaultKey();
            if (toggleBound.getCategory() == InputUtil.Type.KEYSYM) {
                boolean down = InputUtil.isKeyPressed(window, toggleBound.getCode());
                if (down && !prevToggleDown && !isTextFieldFocused(client)) {
                    SingletonInstances.GUI_MANAGER_OVERLAY.toggleMainOverlayState();
                }
                prevToggleDown = down;
            }

            // MOD SETTINGS
            InputUtil.Key settingsBound = MOD_SETTINGS.getDefaultKey();
            if (settingsBound.getCategory() == InputUtil.Type.KEYSYM) {
                boolean down = InputUtil.isKeyPressed(window, settingsBound.getCode());
                if (down && !prevSettingsDown && !isTextFieldFocused(client)) {
                    FabricModSettingsScreenImpl.setAsScreen();
                }
                prevSettingsDown = down;
            }
        });


    }

    private static boolean isTextFieldFocused(MinecraftClient client) {
        if (client.currentScreen == null) return false;
        return client.currentScreen.getFocused() instanceof net.minecraft.client.gui.widget.TextFieldWidget;
        // delegate.isFocused()
    }
}
