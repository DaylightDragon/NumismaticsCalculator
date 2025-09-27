package org.daylight.numismaticscalculator.events;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.daylight.numismaticscalculator.ModKeyBindings;
import org.daylight.numismaticscalculator.replacements.*;
import org.daylight.numismaticscalculator.replacements.api.ForgeGuiGraphics;
import org.daylight.numismaticscalculator.replacements.api.ForgeRegisterListenersEvent;
import org.daylight.numismaticscalculator.replacements.api.ForgeScreen;
import org.daylight.numismaticscalculator.ui.ForgeSelectionRenderer;
import org.daylight.numismaticscalculator.ui.screens.ForgeModSettingsScreenImpl;

public class ScreenEvents {
    @SubscribeEvent
    public void onClientSetup(FMLClientSetupEvent event) {

    }

    @SubscribeEvent
    public void onScreenRender(ScreenEvent.Render.Post event) {
        IGuiGraphics abstractGraphics = new ForgeGuiGraphics(event.getGuiGraphics());
        IScreen abstractScreen = new ForgeScreen(event.getScreen());

        if (SingletonInstances.CALCULATOR_OVERLAY.shouldRenderOnScreen(abstractScreen)) {
            SingletonInstances.CALCULATOR_OVERLAY.render(abstractGraphics, event.getPartialTick(), event.getMouseX(), event.getMouseY());
            ForgeSelectionRenderer.renderSelection(event.getGuiGraphics(), (AbstractContainerScreen<?>) event.getScreen());
        }
        SingletonInstances.GUI_MANAGER_OVERLAY.render(abstractGraphics, event.getPartialTick(), event.getMouseX(), event.getMouseY());
        SingletonInstances.MOD_SETTINGS_OVERLAY.render(abstractGraphics, event.getPartialTick(), event.getMouseX(), event.getMouseY());
    }

    @SubscribeEvent
    public void onScreenInit(ScreenEvent.Init.Post event) {
        IRegisterListenersEvent abstractEvent = new ForgeRegisterListenersEvent(event);
        IScreen abstractScreen = new ForgeScreen(event.getScreen());

        if (SingletonInstances.CALCULATOR_OVERLAY.shouldRenderOnScreen(abstractScreen)) {
//            System.out.println("Relink main");
            SingletonInstances.CALCULATOR_OVERLAY.relinkListeners(abstractEvent);
            SingletonInstances.CALCULATOR_OVERLAY.updateOverlayPosition(abstractScreen);
        }
        if(SingletonInstances.GUI_MANAGER_OVERLAY.shouldRenderOnScreen(abstractScreen)) {
            SingletonInstances.GUI_MANAGER_OVERLAY.relinkListeners(abstractEvent);
            SingletonInstances.GUI_MANAGER_OVERLAY.updateOverlayPosition(abstractScreen);
        }
        if(SingletonInstances.MOD_SETTINGS_OVERLAY.shouldRenderOnScreen(abstractScreen)) {
            SingletonInstances.MOD_SETTINGS_OVERLAY.relinkListeners(abstractEvent);
            SingletonInstances.MOD_SETTINGS_OVERLAY.updateOverlayPosition(abstractScreen);
        }
    }

    @SubscribeEvent
    public void onScreenOpen(ScreenEvent.Opening event) {
//        Screen newScreen = event.getScreen();
        SingletonInstances.CALCULATOR_OVERLAY.onScreenChange(new ForgeScreen(event.getScreen()));
    }

    @SubscribeEvent
    public void onScreenKey(ScreenEvent.KeyPressed.Post event) {
        if (event.getScreen() instanceof AbstractContainerScreen<?>) {
            InputConstants.Key key = InputConstants.getKey(event.getKeyCode(), event.getScanCode());
            if (ModKeyBindings.TOGGLE_GUI.isActiveAndMatches(key)) {
                SingletonInstances.GUI_MANAGER_OVERLAY.toggleMainOverlayState();
            }
            if (ModKeyBindings.MOD_SETTINGS.isActiveAndMatches(key)) {
                ForgeModSettingsScreenImpl.setAsScreen();
            }
        }
    }

    // Unused
    @SubscribeEvent
    public void onRenderOverlay(RenderGuiOverlayEvent.Post event) {
//        Minecraft mc = Minecraft.getInstance();
//        if(mc.screen == null) return;
////        if (event.getOverlay() != VanillaGuiOverlay.ALL) return;
//
////        CoinsCalculator.LOGGER.info(mc.screen.getClass().getSimpleName());
//        if (!(mc.screen instanceof InventoryScreen || mc.screen instanceof CreativeModeInventoryScreen)) return;
////        CoinsCalculator.LOGGER.info("INVENTORY");
//
//        int width = event.getWindow().getGuiScaledWidth();
//        int height = event.getWindow().getGuiScaledHeight();
//
//        int x = width / 2 + 10;  // пример позиции справа от игрока
//        int y = height / 2 - 20; // пример позиции выше центра
//
//        MainWidget.drawWidget(mc, event, x, y);
    }
}
