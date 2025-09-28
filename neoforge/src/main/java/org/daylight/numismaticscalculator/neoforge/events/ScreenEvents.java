package org.daylight.numismaticscalculator.neoforge.events;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import org.daylight.numismaticscalculator.neoforge.ModKeyBindings;
import org.daylight.numismaticscalculator.neoforge.replacements.api.NeoForgeGuiGraphics;
import org.daylight.numismaticscalculator.neoforge.replacements.api.NeoForgeRegisterListenersEvent;
import org.daylight.numismaticscalculator.neoforge.replacements.api.NeoForgeScreen;
import org.daylight.numismaticscalculator.neoforge.ui.NeoForgeSelectionRenderer;
import org.daylight.numismaticscalculator.neoforge.ui.screens.NeoForgeModSettingsScreenImpl;
import org.daylight.numismaticscalculator.replacements.IGuiGraphics;
import org.daylight.numismaticscalculator.replacements.IRegisterListenersEvent;
import org.daylight.numismaticscalculator.replacements.IScreen;
import org.daylight.numismaticscalculator.replacements.SingletonInstances;

public class ScreenEvents {
//    @SubscribeEvent
//    public void onClientSetup(FMLClientSetupEvent event) {
//
//    }

    @SubscribeEvent
    public void onScreenRender(ScreenEvent.Render.Post event) {
        IGuiGraphics abstractGraphics = new NeoForgeGuiGraphics(event.getGuiGraphics());
        IScreen abstractScreen = new NeoForgeScreen(event.getScreen());

        if (SingletonInstances.CALCULATOR_OVERLAY.shouldRenderOnScreen(abstractScreen)) {
            SingletonInstances.CALCULATOR_OVERLAY.render(abstractGraphics, event.getPartialTick(), event.getMouseX(), event.getMouseY());
            NeoForgeSelectionRenderer.renderSelection(event.getGuiGraphics(), (AbstractContainerScreen<?>) event.getScreen());
        }
        SingletonInstances.GUI_MANAGER_OVERLAY.render(abstractGraphics, event.getPartialTick(), event.getMouseX(), event.getMouseY());
        SingletonInstances.MOD_SETTINGS_OVERLAY.render(abstractGraphics, event.getPartialTick(), event.getMouseX(), event.getMouseY());
    }

    @SubscribeEvent
    public void onScreenInit(ScreenEvent.Init.Post event) {
        IRegisterListenersEvent abstractEvent = new NeoForgeRegisterListenersEvent(event);
        IScreen abstractScreen = new NeoForgeScreen(event.getScreen());

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
        SingletonInstances.CALCULATOR_OVERLAY.onScreenChange(new NeoForgeScreen(event.getScreen()));
    }

    @SubscribeEvent
    public void onScreenKey(ScreenEvent.KeyPressed.Post event) {
        if (event.getScreen() instanceof AbstractContainerScreen<?>) {
            InputConstants.Key key = InputConstants.getKey(event.getKeyCode(), event.getScanCode());
            if (ModKeyBindings.TOGGLE_GUI.isActiveAndMatches(key)) {
                SingletonInstances.GUI_MANAGER_OVERLAY.toggleMainOverlayState();
            }
            if (ModKeyBindings.MOD_SETTINGS.isActiveAndMatches(key)) {
                NeoForgeModSettingsScreenImpl.setAsScreen();
            }
        }
    }
}
