package org.daylight.numismaticscalculator.neoforge.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.daylight.numismaticscalculator.neoforge.replacements.api.NeoForgeKeyPressEvent;
import org.daylight.numismaticscalculator.neoforge.replacements.api.NeoForgeScreen;
import org.daylight.numismaticscalculator.replacements.SingletonInstances;
import org.lwjgl.glfw.GLFW;

import java.util.HashSet;
import java.util.Set;

public class InputEvents {
    protected static boolean leftButtonDown = false;
    protected static boolean rightButtonDown = false;

    protected static boolean leftDown = false;
    protected static boolean rightDown = false;

    protected static final Set<Integer> pressedButtons = new HashSet<>();
    protected static double lastMouseX = -1;
    protected static double lastMouseY = -1;

    protected static int lastWindowWidth = -1;
    protected static int lastWindowHeight = -1;

//    @SubscribeEvent
//    public static void onMouseRelease(InputEvent.MouseButton event) {
//        if (inputHandler == null) return;
//        Minecraft mc = Minecraft.getInstance();
//        double mouseX = getMouseX();
//        double mouseY = getMouseY();
//        inputHandler.onMouseRelease(mouseX, mouseY, event.getButton(), mc.screen);
//    }

//    @SubscribeEvent
//    public static void onKeyboardChar(InputEvent.CharTyped event) {
//        if (inputHandler == null) return;
//        inputHandler.onCharTyped(event.getCodePoint(), event.getModifiers());
//    }

    public static int getMouseX() {
        Minecraft mc = Minecraft.getInstance();
        return (int) (mc.mouseHandler.xpos() * mc.getWindow().getGuiScaledWidth() / mc.getWindow().getWidth());
    }

    public static int getMouseY() {
        Minecraft mc = Minecraft.getInstance();
        return (int) (mc.mouseHandler.ypos() * mc.getWindow().getGuiScaledHeight() / mc.getWindow().getHeight());
    }

//    @SubscribeEvent
//    public static void onCharTyped(ScreenEvent.KeyboardCharTyped.Pre event) {
//        if (event.getScreen() instanceof YourScreen screen) {
//            if (screen.getFocused() instanceof EditBox editBox) {
//                String current = editBox.getValue();
//                System.out.println("Текущее значение: " + current);
//            }
//        }
//    }
}
