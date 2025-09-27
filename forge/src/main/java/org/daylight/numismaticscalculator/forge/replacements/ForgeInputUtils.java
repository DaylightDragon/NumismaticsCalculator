package org.daylight.numismaticscalculator.forge.replacements;

import net.minecraft.client.Minecraft;
import org.daylight.numismaticscalculator.replacements.IInputUtils;

public class ForgeInputUtils implements IInputUtils {
    @Override
    public int getMouseX() {
        Minecraft mc = Minecraft.getInstance();
        return (int) (mc.mouseHandler.xpos() * mc.getWindow().getGuiScaledWidth() / mc.getWindow().getWidth());
    }

    @Override
    public int getMouseY() {
        Minecraft mc = Minecraft.getInstance();
        return (int) (mc.mouseHandler.ypos() * mc.getWindow().getGuiScaledHeight() / mc.getWindow().getHeight());
    }
}
