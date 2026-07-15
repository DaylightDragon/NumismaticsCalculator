package org.daylight.numismaticscalculator.fabric.replacements.api;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.resources.ResourceLocation;
import org.daylight.numismaticscalculator.replacements.IRenderSystem;

public class FabricRenderSystem implements IRenderSystem {
    @Override
    public void setShaderTexture(int slot, String resourceLocation) {
        RenderSystem.setShaderTexture(slot, ResourceLocation.tryParse(resourceLocation));
    }
}
