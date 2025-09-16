package org.daylight.coinscalculator.replacements;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.resources.ResourceLocation;

public class ForgeRenderSystem implements IRenderSystem {
    @Override
    public void setShaderTexture(int slot, String resourceLocation) {
        RenderSystem.setShaderTexture(slot, ResourceLocation.parse(resourceLocation));
    }
}
