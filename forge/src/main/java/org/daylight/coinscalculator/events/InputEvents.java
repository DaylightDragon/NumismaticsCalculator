package org.daylight.coinscalculator.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.daylight.coinscalculator.CoinsCalculator;
import org.daylight.coinscalculator.replacements.api.ForgeKeyPressEvent;
import org.daylight.coinscalculator.replacements.api.ForgeScreen;
import org.daylight.coinscalculator.replacements.SingletonInstances;
import org.lwjgl.glfw.GLFW;

import java.util.HashSet;
import java.util.Set;

@Mod.EventBusSubscriber(modid = CoinsCalculator.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class InputEvents {
    private static boolean leftButtonDown = false;
    private static boolean rightButtonDown = false;

    private static boolean leftDown = false;
    private static boolean rightDown = false;

    @SubscribeEvent
    public static void onMouseButton(InputEvent.MouseButton.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        double mouseX = getMouseX();
        double mouseY = getMouseY();
        Screen screen = mc.screen;

        if (SingletonInstances.CALCULATOR_OVERLAY == null || screen == null) return;

        int button = event.getButton();
        if (event.getAction() == GLFW.GLFW_PRESS) {
            pressedButtons.add(button);
            if(SingletonInstances.CALCULATOR_OVERLAY.onMouseClick(mouseX, mouseY, event.getButton(), new ForgeScreen(screen))) event.setCanceled(true);
            if(SingletonInstances.GUI_MANAGER_OVERLAY.onMouseClick(mouseX, mouseY, event.getButton(), new ForgeScreen(screen))) event.setCanceled(true);
            if(SingletonInstances.MOD_SETTINGS_OVERLAY.onMouseClick(mouseX, mouseY, event.getButton(), new ForgeScreen(screen))) event.setCanceled(true);
        } else if (event.getAction() == GLFW.GLFW_RELEASE) {
            pressedButtons.remove(button);
            SingletonInstances.CALCULATOR_OVERLAY.onMouseRelease(mouseX, mouseY, event.getButton(), new ForgeScreen(screen));
        }
    }

    private static final Set<Integer> pressedButtons = new HashSet<>();
    private static double lastMouseX = -1;
    private static double lastMouseY = -1;

    private static int lastWindowWidth = -1;
    private static int lastWindowHeight = -1;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getInstance();

        double mouseX = mc.mouseHandler.xpos();
        double mouseY = mc.mouseHandler.ypos();

        if (lastMouseX != -1 && lastMouseY != -1) {
            if ((mouseX != lastMouseX || mouseY != lastMouseY) && pressedButtons.contains(0)) {
                double dx = mouseX - lastMouseX;
                double dy = mouseY - lastMouseY;
//                System.out.println("Mouse dragging detected: dx=" + dx + ", dy=" + dy);
                SingletonInstances.CALCULATOR_OVERLAY.onMouseDrag(getMouseX(), getMouseX(), 0, new ForgeScreen(Minecraft.getInstance().screen));
            }
        }

        lastMouseX = mouseX;
        lastMouseY = mouseY;

        checkWindowResize(event);
    }

    private static void checkWindowResize(TickEvent.ClientTickEvent event) {
        var window = Minecraft.getInstance().getWindow();
        int w = window.getGuiScaledWidth();
        int h = window.getGuiScaledHeight();

        if (w != lastWindowWidth || h != lastWindowHeight) {
            lastWindowWidth = w;
            lastWindowHeight = h;
            onWindowResized(w, h);
        }
    }

    private static void onWindowResized(int width, int height) {
        if(SingletonInstances.CALCULATOR_OVERLAY != null) SingletonInstances.CALCULATOR_OVERLAY.replacePositionAnimationData();
    }

//    @SubscribeEvent
//    public static void onMouseRelease(InputEvent.MouseButton event) {
//        if (inputHandler == null) return;
//        Minecraft mc = Minecraft.getInstance();
//        double mouseX = getMouseX();
//        double mouseY = getMouseY();
//        inputHandler.onMouseRelease(mouseX, mouseY, event.getButton(), mc.screen);
//    }

    @SubscribeEvent
    public static void onKeyPressed(InputEvent.Key event) {
        if(SingletonInstances.CALCULATOR_OVERLAY != null) SingletonInstances.CALCULATOR_OVERLAY.onKeyPressed(new ForgeKeyPressEvent(event)); // event.getKey(), event.getScanCode(), event.getModifiers());
        if(SingletonInstances.MOD_SETTINGS_OVERLAY != null) SingletonInstances.MOD_SETTINGS_OVERLAY.onKeyPressed(new ForgeKeyPressEvent(event));
    }

//    @SubscribeEvent
//    public static void onKeyboardChar(InputEvent.CharTyped event) {
//        if (inputHandler == null) return;
//        inputHandler.onCharTyped(event.getCodePoint(), event.getModifiers());
//    }

    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
//        if (calculatorOverlay != null) calculatorOverlay.onMouseScroll(event.getScrollDelta(), event.getScrollDelta(), event.getScrollDelta());
    }

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
