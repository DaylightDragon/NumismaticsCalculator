package org.daylight.numismaticscalculator.replacements.api;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.resources.ResourceLocation;
import org.daylight.numismaticscalculator.replacements.IRenderSystem;

public class ForgeRenderSystem implements IRenderSystem {
    @Override
    public void setShaderTexture(int slot, String resourceLocation) {
        RenderSystem.setShaderTexture(slot, new ResourceLocation(resourceLocation)); // ResourceLocation.parse
    }
}
