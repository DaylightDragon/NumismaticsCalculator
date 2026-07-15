package org.daylight.numismaticscalculator.neoforge.ui.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.daylight.numismaticscalculator.neoforge.config.ConfigHandler;
import org.jetbrains.annotations.NotNull;

public class NeoForgeModSettingsScreenImpl extends Screen {
    private final Screen parent;

    public static void setAsScreen() {
        if(Minecraft.getInstance().screen instanceof NeoForgeModSettingsScreenImpl) return;
        Minecraft.getInstance().setScreen(new NeoForgeModSettingsScreenImpl(Minecraft.getInstance().screen));
    }

    public NeoForgeModSettingsScreenImpl(Screen parent) {
        super(Component.translatable("gui.numismaticscalculator.settings.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.addRenderableWidget(Button.builder(Component.translatable("gui.numismaticscalculator.settings.back"), (b) -> {
            Minecraft.getInstance().setScreen(parent);
            ConfigHandler.SPEC.save();
        }).bounds(this.width / 2 - 50, this.height - 40, 100, 20).build());
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
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