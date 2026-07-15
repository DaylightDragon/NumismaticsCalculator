package org.daylight.numismaticscalculator.fabric.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.daylight.numismaticscalculator.fabric.config.ConfigHandler;
import org.jetbrains.annotations.NotNull;

public class FabricModSettingsScreenImpl extends Screen {
    private final Screen parent;

    public static void setAsScreen() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen instanceof FabricModSettingsScreenImpl) return;
        mc.setScreen(new FabricModSettingsScreenImpl(mc.screen));
    }

    public FabricModSettingsScreenImpl(Screen parent) {
        super(Component.literal("Calculator Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.addRenderableWidget(Button.builder(Component.literal("Back"), b -> {
            Minecraft.getInstance().setScreen(parent);
            ConfigHandler.CONFIG.save();
        }).pos(this.width / 2 - 50, this.height - 40).size(100, 20).build());
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(graphics, mouseX, mouseY, delta);
        graphics.drawString(this.font, this.title, (this.width - this.font.width(this.title)) / 2, 20, 0xFFFFFF);
        super.render(graphics, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        super.onClose();
        ConfigHandler.CONFIG.save();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
