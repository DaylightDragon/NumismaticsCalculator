package org.daylight.coinscalculator.replacements;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.resources.ResourceLocation;
import org.daylight.coinscalculator.replacements.IRenderSystem;

public class ForgeRenderSystem implements IRenderSystem {
    @Override
    public void setShaderTexture(int slot, String resourceLocation) {
        RenderSystem.setShaderTexture(slot, ResourceLocation.parse(resourceLocation));
    }
}
