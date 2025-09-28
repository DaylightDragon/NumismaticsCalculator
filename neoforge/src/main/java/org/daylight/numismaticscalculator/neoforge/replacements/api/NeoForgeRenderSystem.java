package org.daylight.numismaticscalculator.neoforge.replacements.api;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.resources.ResourceLocation;
import org.daylight.numismaticscalculator.replacements.IRenderSystem;

public class NeoForgeRenderSystem implements IRenderSystem {
    @Override
    public void setShaderTexture(int slot, String resourceLocation) {
        RenderSystem.setShaderTexture(slot, ResourceLocation.parse(resourceLocation)); // ResourceLocation.parse
    }
}
