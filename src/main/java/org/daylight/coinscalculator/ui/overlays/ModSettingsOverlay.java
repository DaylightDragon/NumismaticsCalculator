package org.daylight.coinscalculator.ui.overlays;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.ForgeConfigSpec;
import org.daylight.coinscalculator.UiState;
import org.daylight.coinscalculator.config.ConfigData;
import org.daylight.coinscalculator.events.InputEvents;
import org.daylight.coinscalculator.ui.elements.*;
import org.daylight.coinscalculator.ui.screens.ModSettingsScreen;
import org.daylight.coinscalculator.util.tuples.Quartet;
import org.jetbrains.annotations.NotNull;

public class ModSettingsOverlay implements IOverlay {
    private static ModSettingsOverlay instance;
    public static ModSettingsOverlay getInstance() {
        if(instance == null) instance = new ModSettingsOverlay();
        return instance;
    }

    private UIPanel rootPanel;

    public static boolean shouldRenderOnScreen(Screen screen) {
        return (screen instanceof ModSettingsScreen);
    }

    public void render(@NotNull GuiGraphics guiGraphics, float partialTick, Integer mouseX, Integer mouseY) {
        if(shouldRenderOnScreen(Minecraft.getInstance().screen)) {
            ModSettingsScreen screen = (ModSettingsScreen) Minecraft.getInstance().screen;
            if (rootPanel == null) init(screen);
            rootPanel.render(guiGraphics, InputEvents.getMouseX(), InputEvents.getMouseY(), partialTick);
        }
    }

    private UIPanel createOptionRow(String name, ForgeConfigSpec.ConfigValue<?> value) {
        Font font = Minecraft.getInstance().font;
        UIHorizontalLayout optionRow = new UIHorizontalLayout();
        optionRow.setPadding(5);
        optionRow.setCrossAlignment(CrossAlignment.CENTER);
        optionRow.setBackgroundVisible(true);
        optionRow.setBackgroundColor(0xAA36302c);
        optionRow.setOutlineWidth(1);
        optionRow.setOutlineColor(0xBBbf7947);
        optionRow.addElement(new UIText(name, font, 1.0f, 0xFFFFFF));
        if(value instanceof ForgeConfigSpec.BooleanValue booleanValue) {
            UIButton btn = new UIButton(booleanValue.get() ? "True" : "False", font, 1.0f, () -> {}) {
                @Override
                public boolean onClick(double mouseX, double mouseY) {
                    boolean result = super.onClick(mouseX, mouseY);
                    if(!result) return false;
                    booleanValue.set(!booleanValue.get());
                    setLabel(booleanValue.get() ? "True" : "False");
                    setTextColor(booleanValue.get() ? 0xb9ff8a : 0xff8a8a);
//                    System.out.println(width);
//                    System.out.println("CLICK");
//                        booleanValue.save();
                    return true;
                }
            };
            btn.setTextColor(booleanValue.get() ? 0xb9ff8a : 0xff6e6e);
            btn.setImagePosition(UIButton.ImagePosition.IMAGE_RIGHT_KINDA);
            btn.setSpacing(0);
            btn.setMinWidth(37);
            optionRow.addElement(btn);
        }
        return optionRow;
    }

    public void init(ModSettingsScreen screen) {
        rootPanel = new UIVerticalLayout();

        UIVerticalLayout verticalLayout = new UIVerticalLayout();
        verticalLayout.setPadding(0);

        verticalLayout.addElement(createOptionRow("Require \"Shift\" For Total Value Tooltip", ConfigData.requireShiftForTotalTooltip));
        verticalLayout.addElement(createOptionRow("Show Control Panel Buttons", ConfigData.showControlPanel));

        rootPanel.addElement(verticalLayout);

        rootPanel.layoutElements();
        rootPanel.updateInternalValues();

        Quartet<Integer, Integer, Integer, Integer> bounds = getOverlayBoundsForScreen(screen);
        rootPanel.setBounds(bounds.getA(), bounds.getB(), bounds.getC(), bounds.getD());
    }

    public Quartet<Integer, Integer, Integer, Integer> getOverlayBoundsForScreen(ModSettingsScreen screen) {
        int prefWGlobal = rootPanel.getPreferredWidth();
        int prefHGlobal = rootPanel.getPreferredHeight();

        return new Quartet<>((screen.width - prefWGlobal) / 2, (screen.height - prefHGlobal) / 2, prefWGlobal, prefHGlobal);
    }

    public void relinkListeners(ScreenEvent.Init.Post event) {
        if(event.getScreen() instanceof ModSettingsScreen modSettingsScreen) {
            if (rootPanel == null) init(modSettingsScreen);
        }
        rootPanel.relinkListeners(event);
    }

    public boolean onMouseClick(double mouseX, double mouseY, int button, Screen screenOrig) {
        boolean result = false;
        if (shouldRenderOnScreen(screenOrig)) {
            if (rootPanel != null) rootPanel.onClick(mouseX, mouseY);
        }
        return result;
    }

    public void updateOverlayPosition(Screen screen) {
        if(rootPanel == null) return;
        if(screen instanceof ModSettingsScreen modSettingsScreen) {
            Quartet<Integer, Integer, Integer, Integer> bounds = getOverlayBoundsForScreen(modSettingsScreen);
            rootPanel.setBounds(bounds.getA(), bounds.getB(), bounds.getC(), bounds.getD());
        }
    }
}
