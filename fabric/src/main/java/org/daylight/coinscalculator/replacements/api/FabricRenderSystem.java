package org.daylight.coinscalculator.replacements.api;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.util.Identifier;
import org.daylight.coinscalculator.replacements.IRenderSystem;

public class FabricRenderSystem implements IRenderSystem {
    @Override
    public void setShaderTexture(int slot, String resourceLocation) {
        RenderSystem.setShaderTexture(slot, new Identifier(resourceLocation));
    }
}
