package org.daylight.numismaticscalculator.fabric.ui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.daylight.numismaticscalculator.fabric.config.ConfigHandler;
import org.jetbrains.annotations.NotNull;

public class FabricModSettingsScreenImpl extends Screen {
    private final Screen parent;

    public static void setAsScreen() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.currentScreen instanceof FabricModSettingsScreenImpl) return;
        mc.setScreen(new FabricModSettingsScreenImpl(mc.currentScreen));
    }

    public FabricModSettingsScreenImpl(Screen parent) {
        super(Text.literal("Calculator Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Back"), b -> {
            MinecraftClient.getInstance().setScreenAndRender(parent);
            ConfigHandler.CONFIG.save();
        }).position(this.width / 2 - 50, this.height - 40).size(100, 20).build());
    }

    @Override
    public void render(@NotNull DrawContext graphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(graphics);
        graphics.drawTextWithShadow(this.textRenderer, this.title, (this.width - this.textRenderer.getWidth(this.title)) / 2, 20, 0xFFFFFF);
        super.render(graphics, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        super.close();
        ConfigHandler.CONFIG.save();
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
