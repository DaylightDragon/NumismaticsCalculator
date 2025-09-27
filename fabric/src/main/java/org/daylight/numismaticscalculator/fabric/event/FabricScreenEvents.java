package org.daylight.numismaticscalculator.fabric.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.daylight.numismaticscalculator.fabric.replacements.api.*;
import org.daylight.numismaticscalculator.fabric.ui.FabricModSettingsScreenImpl;
import org.daylight.numismaticscalculator.replacements.IGuiGraphics;
import org.daylight.numismaticscalculator.replacements.IScreen;
import org.daylight.numismaticscalculator.replacements.SingletonInstances;

import java.util.HashSet;
import java.util.Set;

public class FabricScreenEvents {
    private static final Set<Integer> pressedButtons = new HashSet<>();
    private static double lastMouseX = -1;
    private static double lastMouseY = -1;
    private static int lastWindowWidth = -1;
    private static int lastWindowHeight = -1;

    public static void initializeScreenEvents() {
        MinecraftClient mc = MinecraftClient.getInstance();

        // Mouse button events
        ScreenEvents.AFTER_INIT.register((minecraftClient, screen, width, height) -> { // sketchy
//            System.out.println(screen.getClass().getSimpleName() + " " + (screen instanceof HandledScreen<?>));
            if(screen instanceof HandledScreen<?>) {
                SingletonInstances.CALCULATOR_OVERLAY.init(new FabricAbstractContainerScreen<>((HandledScreen<?>) screen));
                SingletonInstances.GUI_MANAGER_OVERLAY.init(new FabricAbstractContainerScreen<>((HandledScreen<?>) screen));
            } else if(screen instanceof FabricModSettingsScreenImpl) {
                SingletonInstances.MOD_SETTINGS_OVERLAY.init(new FabricModSettingsScreen((FabricModSettingsScreenImpl) screen));
            }

            // Mouse click
            ScreenMouseEvents.beforeMouseClick(screen).register(FabricScreenEvents::onMouseClickUi);

            ScreenMouseEvents.allowMouseClick(screen).register((screen1, mouseX, mouseY, button) -> {
                boolean allow = !SingletonInstances.CALCULATOR_OVERLAY.shouldBlockClicks(new FabricScreen(screen), (int) mouseX, (int) mouseY);
                if(!allow) onMouseClickUi(screen1, mouseX, mouseY, button);
                return allow;
            });

            // Mouse release
            ScreenMouseEvents.beforeMouseRelease(screen).register((s, mouseX, mouseY, button) -> {
                pressedButtons.remove(button);
                if(SingletonInstances.CALCULATOR_OVERLAY != null) SingletonInstances.CALCULATOR_OVERLAY.onMouseRelease(mouseX, mouseY, button, new FabricScreen(screen));
                if(SingletonInstances.GUI_MANAGER_OVERLAY != null) SingletonInstances.GUI_MANAGER_OVERLAY.onMouseRelease(mouseX, mouseY, button, new FabricScreen(screen));
                if(SingletonInstances.MOD_SETTINGS_OVERLAY != null) SingletonInstances.MOD_SETTINGS_OVERLAY.onMouseRelease(mouseX, mouseY, button, new FabricScreen(screen));
            });

            // Key press // Commented to not have duplicates
//            ScreenKeyboardEvents.afterKeyPress(screen).register((s, key, scancode, modifiers) -> {
//                if(SingletonInstances.CALCULATOR_OVERLAY != null) {
//                    SingletonInstances.CALCULATOR_OVERLAY.onKeyPressed(new FabricKeyPressEvent(screen, key, scancode, modifiers));
//                }
//            });

            // -------- Init Events

            // Render
            ScreenEvents.afterRender(screen).register((screenArg, drawContext, mouseX, mouseY, tickDelta) -> {
                IGuiGraphics g = new FabricGuiGraphics(drawContext);
                IScreen abstractScreen = new FabricScreen(screenArg);
//                System.out.println("Render");
                if (SingletonInstances.CALCULATOR_OVERLAY.shouldRenderOnScreen(abstractScreen)) {
                    SingletonInstances.CALCULATOR_OVERLAY.render(g, tickDelta, mouseX, mouseY);
                }
                SingletonInstances.GUI_MANAGER_OVERLAY.render(g, tickDelta, mouseX, mouseY);
                SingletonInstances.MOD_SETTINGS_OVERLAY.render(g, tickDelta, mouseX, mouseY);
            });

            // Key press
            ScreenKeyboardEvents.afterKeyPress(screen).register((screenArg, key, scancode, modifiers) -> {
                if (screen instanceof HandledScreen<?>) {
                    if (SingletonInstances.CALCULATOR_OVERLAY != null) {
                        SingletonInstances.CALCULATOR_OVERLAY.onKeyPressed(new FabricKeyPressEvent(screenArg, key, scancode, modifiers));
                    }
                    // Gui manager overlay doesn't need this event yet
                } else if(screen instanceof FabricModSettingsScreenImpl) {
                    if (SingletonInstances.MOD_SETTINGS_OVERLAY != null) {
                        SingletonInstances.MOD_SETTINGS_OVERLAY.onKeyPressed(new FabricKeyPressEvent(screenArg, key, scancode, modifiers));
                    }
                }
            });
        });

        // Client tick
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            double mouseX = SingletonInstances.INPUT_UTILS.getMouseX();
            double mouseY = SingletonInstances.INPUT_UTILS.getMouseY();

//            System.out.println(pressedButtons);

            if (lastMouseX != -1 && lastMouseY != -1 && pressedButtons.contains(0)) {
                double dx = mouseX - lastMouseX;
                double dy = mouseY - lastMouseY;
//                System.out.println("a");
                if ((dx != 0 || dy != 0) && SingletonInstances.CALCULATOR_OVERLAY != null) {
                    SingletonInstances.CALCULATOR_OVERLAY.onMouseDrag(mouseX, mouseY, 0, new FabricScreen(mc.currentScreen));
                }
            }

            lastMouseX = mouseX;
            lastMouseY = mouseY;

            checkWindowResize();
        });

        // Render events
        HudRenderCallback.EVENT.register((graphics, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.currentScreen != null) return;
            SingletonInstances.CALCULATOR_OVERLAY.render(
                    new FabricGuiGraphics(graphics),
                    tickDelta,
                    SingletonInstances.INPUT_UTILS.getMouseX(),
                    SingletonInstances.INPUT_UTILS.getMouseY()
            );
        });

        // ------------------------ Init Events ------------------------

        registerInitEvents();
    }

    private static void onMouseClickUi(Screen screen, double mouseX, double mouseY, int button) {
        pressedButtons.add(button);
        if (SingletonInstances.CALCULATOR_OVERLAY != null) SingletonInstances.CALCULATOR_OVERLAY.onMouseClick(mouseX, mouseY, button, new FabricScreen(screen));
        if (SingletonInstances.GUI_MANAGER_OVERLAY != null) SingletonInstances.GUI_MANAGER_OVERLAY.onMouseClick(mouseX, mouseY, button, new FabricScreen(screen));
        if (SingletonInstances.MOD_SETTINGS_OVERLAY != null) SingletonInstances.MOD_SETTINGS_OVERLAY.onMouseClick(mouseX, mouseY, button, new FabricScreen(screen));
    }

    private static void registerInitEvents() {
        // После инициализации экрана (замена Init.Post)
        ScreenEvents.AFTER_INIT.register((client, screen, width, height) -> {
//            FabricRegisterListenersEvent abstractEvent = new FabricRegisterListenersEvent(screen); // TODO
            IScreen abstractScreen = new FabricScreen(screen);

            if (SingletonInstances.CALCULATOR_OVERLAY.shouldRenderOnScreen(abstractScreen)) {
//                SingletonInstances.CALCULATOR_OVERLAY.relinkListeners(abstractEvent);
                SingletonInstances.CALCULATOR_OVERLAY.updateOverlayPosition(abstractScreen);
            }

            if (SingletonInstances.GUI_MANAGER_OVERLAY.shouldRenderOnScreen(abstractScreen)) {
//                SingletonInstances.GUI_MANAGER_OVERLAY.relinkListeners(abstractEvent);
                SingletonInstances.GUI_MANAGER_OVERLAY.updateOverlayPosition(abstractScreen);
            }

            if (SingletonInstances.MOD_SETTINGS_OVERLAY.shouldRenderOnScreen(abstractScreen)) {
//                SingletonInstances.MOD_SETTINGS_OVERLAY.relinkListeners(abstractEvent);
                SingletonInstances.MOD_SETTINGS_OVERLAY.updateOverlayPosition(abstractScreen);
            }
        });

        // После открытия экрана
        ScreenEvents.AFTER_INIT.register((client, screenArg, width, height) -> {
            SingletonInstances.CALCULATOR_OVERLAY.onScreenChange(new FabricScreen(screenArg));
        });
    }

    private static void checkWindowResize() {
        var window = MinecraftClient.getInstance().getWindow();
        int w = window.getScaledWidth();
        int h = window.getScaledHeight();

        if (w != lastWindowWidth || h != lastWindowHeight) {
            lastWindowWidth = w;
            lastWindowHeight = h;
            onWindowResized(w, h);
        }
    }

    private static void onWindowResized(int width, int height) {
        if (SingletonInstances.CALCULATOR_OVERLAY != null)
            SingletonInstances.CALCULATOR_OVERLAY.replacePositionAnimationData();
    }
}
