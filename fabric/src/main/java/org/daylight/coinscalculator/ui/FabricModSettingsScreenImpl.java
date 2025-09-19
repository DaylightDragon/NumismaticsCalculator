package org.daylight.coinscalculator.ui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
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
        // Кнопка "Back"
        this.addDrawable(ButtonWidget.builder(Text.literal("Back"), b -> {
            MinecraftClient.getInstance().setScreen(parent);
//            ConfigHandler.SPEC.save();
        }).position(this.width / 2 - 50, this.height - 40).size(100, 20).build());
    }

    @Override
    public void render(@NotNull DrawContext graphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(graphics);
        graphics.drawTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        super.render(graphics, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        super.close();
//        ConfigHandler.SPEC.save();
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
