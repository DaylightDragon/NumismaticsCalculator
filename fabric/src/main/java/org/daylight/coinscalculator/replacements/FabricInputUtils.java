package org.daylight.coinscalculator.replacements;

import net.minecraft.client.MinecraftClient;

public class FabricInputUtils implements IInputUtils {
    @Override
    public int getMouseX() {
        MinecraftClient mc = MinecraftClient.getInstance();
        return (int) (mc.mouse.getX() * mc.getWindow().getScaledWidth() / mc.getWindow().getWidth());
    }

    @Override
    public int getMouseY() {
        MinecraftClient mc = MinecraftClient.getInstance();
        return (int) (mc.mouse.getY() * mc.getWindow().getScaledHeight() / mc.getWindow().getHeight());
    }
}
