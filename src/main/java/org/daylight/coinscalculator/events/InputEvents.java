package org.daylight.coinscalculator.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.daylight.coinscalculator.CoinsCalculator;
import org.daylight.coinscalculator.ui.overlays.CalculatorOverlay;
import org.daylight.coinscalculator.ui.overlays.GuiManagerOverlay;
import org.lwjgl.glfw.GLFW;

import java.util.HashSet;
import java.util.Set;

@Mod.EventBusSubscriber(modid = CoinsCalculator.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class InputEvents {

    private static CalculatorOverlay calculatorOverlay = CalculatorOverlay.getInstance();
    private static GuiManagerOverlay guiManagerOverlay = GuiManagerOverlay.getInstance();

    private static boolean leftButtonDown = false;
    private static boolean rightButtonDown = false;

    private static boolean leftDown = false;
    private static boolean rightDown = false;

//    @SubscribeEvent
//    public static void onMouseButton2(InputEvent.MouseButton event) {
//        // 0 = ЛКМ, 1 = ПКМ, 2 = Средняя
//        double mouseX = getMouseX();
//        double mouseY = getMouseY();
//        if (event.getButton() == 0) {
//            leftDown = event.getAction() == 1; // 1 = PRESS, 0 = RELEASE
//            if (leftDown) {
//                System.out.println("ЛКМ нажата");
//                if(inputHandler.onMouseClick(mouseX, mouseY, event.getButton(), Minecraft.getInstance().screen)) event.setCanceled(true);
//            } else {
//                System.out.println("ЛКМ отпущена");
//            }
//        }
//        if (event.getButton() == 1) {
//            rightDown = event.getAction() == 1;
//        }
//    }

//    @SubscribeEvent
//    public static void onMouseMove(InputEvent.Mouse event) {
//        if (Minecraft.getInstance().screen == null) {
//            if (leftDown) {
//                System.out.println("Мышь двигается с зажатой ЛКМ: x=" + event.getMouseX() + " y=" + event.getMouseY());
//            }
//        }
//    }

//    @SubscribeEvent
//    public static void onMouseClick(InputEvent.MouseButton.Pre event) {
//        if (inputHandler == null) return;
//        Minecraft mc = Minecraft.getInstance();
//        double mouseX = getMouseX();
//        double mouseY = getMouseY();
//        if(inputHandler.onMouseClick(mouseX, mouseY, event.getButton(), mc.screen)) event.setCanceled(true);
//    }

    @SubscribeEvent
    public static void onMouseButton(InputEvent.MouseButton.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        double mouseX = getMouseX();
        double mouseY = getMouseY();
        Screen screen = mc.screen;

        if (calculatorOverlay == null || screen == null) return;

        int button = event.getButton();
        if (event.getAction() == GLFW.GLFW_PRESS) {
            pressedButtons.add(button);
            if(calculatorOverlay.onMouseClick(mouseX, mouseY, event.getButton(), screen)) event.setCanceled(true);
            if(guiManagerOverlay.onMouseClick(mouseX, mouseY, event.getButton(), screen)) event.setCanceled(true);
        } else if (event.getAction() == GLFW.GLFW_RELEASE) {
            pressedButtons.remove(button);
            calculatorOverlay.onMouseRelease(mouseX, mouseY, event.getButton(), screen);
        }
    }

    private static final Set<Integer> pressedButtons = new HashSet<>();
    private static double lastMouseX = -1;
    private static double lastMouseY = -1;

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
                calculatorOverlay.onMouseDrag(getMouseX(), getMouseX(), 0, Minecraft.getInstance().screen);
            }
        }

        lastMouseX = mouseX;
        lastMouseY = mouseY;
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
        if (calculatorOverlay == null) return;
        calculatorOverlay.onKeyPressed(event.getKey(), event.getScanCode(), event.getModifiers());
    }

//    @SubscribeEvent
//    public static void onKeyboardChar(InputEvent.CharTyped event) {
//        if (inputHandler == null) return;
//        inputHandler.onCharTyped(event.getCodePoint(), event.getModifiers());
//    }

    @SubscribeEvent
    public static void onMouseScrolled(InputEvent.MouseScrollingEvent event) {
        if (calculatorOverlay == null) return;
        calculatorOverlay.onMouseScrolled(event.getScrollDelta(), event.getScrollDelta(), event.getScrollDelta());
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
