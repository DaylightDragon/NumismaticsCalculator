package org.daylight.numismaticscalculator.forge.ui.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.daylight.numismaticscalculator.forge.config.ConfigHandler;
import org.jetbrains.annotations.NotNull;

public class ForgeModSettingsScreenImpl extends Screen {
    private final Screen parent;

    public static void setAsScreen() {
        if(Minecraft.getInstance().screen instanceof ForgeModSettingsScreenImpl) return;
        Minecraft.getInstance().setScreen(new ForgeModSettingsScreenImpl(Minecraft.getInstance().screen));
    }

    public ForgeModSettingsScreenImpl(Screen parent) {
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