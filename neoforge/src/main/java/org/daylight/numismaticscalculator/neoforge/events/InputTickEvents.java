package org.daylight.numismaticscalculator.neoforge.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.daylight.numismaticscalculator.neoforge.replacements.api.NeoForgeKeyPressEvent;
import org.daylight.numismaticscalculator.neoforge.replacements.api.NeoForgeScreen;
import org.daylight.numismaticscalculator.replacements.SingletonInstances;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber
public class InputTickEvents {
//    public static void register(IEventBus bus) {
//        NeoForge.EVENT_BUS.addListener(InputTickEvents::onClientTick);
//        NeoForge.EVENT_BUS.addListener(InputTickEvents::onMouseButton);
//        NeoForge.EVENT_BUS.addListener(InputTickEvents::onKeyPressed);
//    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
//        if (event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getInstance();

        double mouseX = mc.mouseHandler.xpos();
        double mouseY = mc.mouseHandler.ypos();

        if (InputEvents.lastMouseX != -1 && InputEvents.lastMouseY != -1) {
            if ((mouseX != InputEvents.lastMouseX || mouseY != InputEvents.lastMouseY) && InputEvents.pressedButtons.contains(0)) {
                double dx = mouseX - InputEvents.lastMouseX;
                double dy = mouseY - InputEvents.lastMouseY;
//                System.out.println("Mouse dragging detected: dx=" + dx + ", dy=" + dy);
                SingletonInstances.CALCULATOR_OVERLAY.onMouseDrag(InputEvents.getMouseX(), InputEvents.getMouseX(), 0, new NeoForgeScreen(Minecraft.getInstance().screen));
            }
        }

        InputEvents.lastMouseX = mouseX;
        InputEvents.lastMouseY = mouseY;

        checkWindowResize(event);
    }

    private static void checkWindowResize(ClientTickEvent.Post event) {
        var window = Minecraft.getInstance().getWindow();
        int w = window.getGuiScaledWidth();
        int h = window.getGuiScaledHeight();

        if (w != InputEvents.lastWindowWidth || h != InputEvents.lastWindowHeight) {
            InputEvents.lastWindowWidth = w;
            InputEvents.lastWindowHeight = h;
            onWindowResized(w, h);
        }
    }

    private static void onWindowResized(int width, int height) {
        if(SingletonInstances.CALCULATOR_OVERLAY != null) SingletonInstances.CALCULATOR_OVERLAY.replacePositionAnimationData();
    }

    @SubscribeEvent
    public static void onMouseButton(InputEvent.MouseButton.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        double mouseX = InputEvents.getMouseX();
        double mouseY = InputEvents.getMouseY();
        Screen screen = mc.screen;

        if (SingletonInstances.CALCULATOR_OVERLAY == null || screen == null) return;

        int button = event.getButton();
        if (event.getAction() == GLFW.GLFW_PRESS) {
            InputEvents.pressedButtons.add(button);
            if(SingletonInstances.CALCULATOR_OVERLAY.onMouseClick(mouseX, mouseY, event.getButton(), new NeoForgeScreen(screen))) event.setCanceled(true);
            if(SingletonInstances.GUI_MANAGER_OVERLAY.onMouseClick(mouseX, mouseY, event.getButton(), new NeoForgeScreen(screen))) event.setCanceled(true);
            if(SingletonInstances.MOD_SETTINGS_OVERLAY.onMouseClick(mouseX, mouseY, event.getButton(), new NeoForgeScreen(screen))) event.setCanceled(true);
        } else if (event.getAction() == GLFW.GLFW_RELEASE) {
            InputEvents.pressedButtons.remove(button);
            SingletonInstances.CALCULATOR_OVERLAY.onMouseRelease(mouseX, mouseY, event.getButton(), new NeoForgeScreen(screen));
        }
    }

    @SubscribeEvent
    public static void onKeyPressed(InputEvent.Key event) {
        if(SingletonInstances.CALCULATOR_OVERLAY != null) SingletonInstances.CALCULATOR_OVERLAY.onKeyPressed(new NeoForgeKeyPressEvent(event)); // event.getKey(), event.getScanCode(), event.getModifiers());
        if(SingletonInstances.MOD_SETTINGS_OVERLAY != null) SingletonInstances.MOD_SETTINGS_OVERLAY.onKeyPressed(new NeoForgeKeyPressEvent(event));
    }

//    @SubscribeEvent
//    public void onMouseScroll(InputEvent.MouseScrollingEvent event) {
////        if (calculatorOverlay != null) calculatorOverlay.onMouseScroll(event.getScrollDelta(), event.getScrollDelta(), event.getScrollDelta());
//    }
}
