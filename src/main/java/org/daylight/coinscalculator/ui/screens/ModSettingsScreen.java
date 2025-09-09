package org.daylight.coinscalculator.ui.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.ForgeConfigSpec;
import org.daylight.coinscalculator.ModResources;
import org.daylight.coinscalculator.UiState;
import org.daylight.coinscalculator.config.ConfigData;
import org.daylight.coinscalculator.config.ConfigHandler;
import org.daylight.coinscalculator.events.InputEvents;
import org.daylight.coinscalculator.ui.elements.*;
import org.daylight.coinscalculator.ui.overlays.CalculatorOverlay;
import org.daylight.coinscalculator.ui.overlays.GuiManagerOverlay;
import org.daylight.coinscalculator.ui.overlays.IOverlay;
import org.daylight.coinscalculator.util.tuples.Quartet;
import org.jetbrains.annotations.NotNull;

public class ModSettingsScreen extends Screen {


    private final Screen parent;

    public static void setAsScreen() {
        if(Minecraft.getInstance().screen instanceof ModSettingsScreen) return;
        Minecraft.getInstance().setScreen(new ModSettingsScreen(Minecraft.getInstance().screen));
    }

    public ModSettingsScreen(Screen parent) {
        super(Component.literal("Calculator Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.addRenderableWidget(Button.builder(Component.literal("Back"), (b) -> {
            Minecraft.getInstance().setScreen(parent);
            ConfigHandler.SPEC.save();
        }).bounds(this.width / 2 - 50, this.height - 40, 100, 20).build());
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose() {
        super.onClose();
        ConfigHandler.SPEC.save();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}