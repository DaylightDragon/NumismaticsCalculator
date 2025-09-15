package org.daylight.coinscalculator.ui.overlays;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import org.daylight.coinscalculator.ModResources;
import org.daylight.coinscalculator.UiState;
import org.daylight.coinscalculator.config.ConfigData;
import org.daylight.coinscalculator.events.InputEvents;
import org.daylight.coinscalculator.ui.SelectionRenderer;
import org.daylight.coinscalculator.ui.elements.UIButton;
import org.daylight.coinscalculator.ui.elements.UIHorizontalLayout;
import org.daylight.coinscalculator.ui.elements.UIPanel;
import org.daylight.coinscalculator.ui.elements.UIVerticalLayout;
import org.daylight.coinscalculator.ui.screens.ModSettingsScreen;
import org.daylight.coinscalculator.util.tuples.Quartet;
import org.jetbrains.annotations.NotNull;

public class GuiManagerOverlay implements IOverlay {
    private static GuiManagerOverlay instance;
    public static GuiManagerOverlay getInstance() {
        if(instance == null) instance = new GuiManagerOverlay();
        return instance;
    }

    private UIPanel rootPanel;

    public static boolean shouldRenderOnScreen(Screen screen) {
        return (screen instanceof AbstractContainerScreen) || (screen instanceof CreativeModeInventoryScreen) || (screen instanceof InventoryScreen);
    }

    public void render(@NotNull GuiGraphics guiGraphics, float partialTick, Integer mouseX, Integer mouseY) {
        if(!ConfigData.showControlPanel.get()) return;
        if(shouldRenderOnScreen(Minecraft.getInstance().screen)) {
            AbstractContainerScreen<?> screen = (AbstractContainerScreen<?>) Minecraft.getInstance().screen;
            if (rootPanel == null) init(screen);
            rootPanel.render(guiGraphics, InputEvents.getMouseX(), InputEvents.getMouseY(), partialTick);
        }
    }

    public static void toggleMainOverlayState() {
        if(!CalculatorOverlay.getInstance().isInitialized() || !CalculatorOverlay.shouldRenderOnScreen(Minecraft.getInstance().screen)) return;
        UiState.coinCalculatorOverlayActive = !UiState.coinCalculatorOverlayActive;
        CalculatorOverlay.getInstance().setOverlayActive(UiState.coinCalculatorOverlayActive); // bad way kinda
        CalculatorOverlay.getInstance().updateLayout();
    }

    public void init(AbstractContainerScreen<?> screen) {
//        System.out.println("INIT GuiManagerOverlay");
        Font font = Minecraft.getInstance().font;

        rootPanel = new UIVerticalLayout();

        UIHorizontalLayout horizontalLayout = new UIHorizontalLayout();
        horizontalLayout.setPadding(0);

        UIButton toggleUiBtn = new UIButton("", font, 1.0f, GuiManagerOverlay::toggleMainOverlayState);
        toggleUiBtn.setIcon(ModResources.CURRENCY_ICON, 12, 12);
        toggleUiBtn.setBgColorNormal(0x8878594c);
        toggleUiBtn.setPadding(2, 2);
        horizontalLayout.addElement(toggleUiBtn);

        UIButton openSettingsBtn = new UIButton("", font, 1.0f, ModSettingsScreen::setAsScreen);
        openSettingsBtn.setIcon(ModResources.GEAR_ICON, 12, 12);
        openSettingsBtn.setBgColorNormal(0x8878594c);
        openSettingsBtn.setPadding(2, 2);
        horizontalLayout.addElement(openSettingsBtn);

        rootPanel.addElement(horizontalLayout);

        rootPanel.layoutElements();
        rootPanel.updateInternalValues();

        Quartet<Integer, Integer, Integer, Integer> bounds = getOverlayBoundsForScreen(screen);
        rootPanel.setBounds(bounds.getA(), bounds.getB(), bounds.getC(), bounds.getD());
    }

    public Quartet<Integer, Integer, Integer, Integer> getOverlayBoundsForScreen(AbstractContainerScreen<?> screen) {
        int prefWGlobal = rootPanel.getPreferredWidth();
        int prefHGlobal = rootPanel.getPreferredHeight();

        Quartet<Integer, Integer, Integer, Integer> mainOverlayBounds = CalculatorOverlay.getInstance().getLastOverlayPosition();
//        System.out.println("Main overlay bounds: " + mainOverlayBounds);
        int y = mainOverlayBounds.getB() + mainOverlayBounds.getD() + 10;

        if(y > screen.height - 5 - prefHGlobal) {
            y = screen.height - 5 - prefHGlobal;
        }

        return new Quartet<>(5, y, prefWGlobal, prefHGlobal); // (int) (screen.getGuiTop() + screen.getYSize() * 0.8)
    }

    public void relinkListeners(ScreenEvent.Init.Post event) {
        if(event.getScreen() instanceof AbstractContainerScreen<?> abstractContainerScreen) {
            if (rootPanel == null) init(abstractContainerScreen);
        }
        rootPanel.relinkListeners(event);
    }

    @Override
    public boolean onMouseClick(double mouseX, double mouseY, int button, Screen screenOrig) {
        if(!ConfigData.showControlPanel.get()) return false;
        boolean result = false;
        if (shouldRenderOnScreen(screenOrig)) {
            if (rootPanel != null) rootPanel.onClick(mouseX, mouseY);
        }
        return result;
    }

    @Override
    public boolean onMouseDrag(double mouseX, double mouseY, int button, Screen screenOrig) {
        return false;
    }

    @Override
    public void onMouseRelease(double mouseX, double mouseY, int button, Screen screenOrig) {}

    @Override
    public void onKeyPressed(InputEvent.Key event) {}

    public void updateOverlayPosition(Screen screen) {
        if(rootPanel == null) return;
        if(screen instanceof AbstractContainerScreen<?> abstractContainerScreen) {
            Quartet<Integer, Integer, Integer, Integer> bounds = getOverlayBoundsForScreen(abstractContainerScreen);
            rootPanel.setBounds(bounds.getA(), bounds.getB(), bounds.getC(), bounds.getD());
//            rootPanel.layoutElements();
//            System.out.println("set " + bounds);
        }
    }
}
