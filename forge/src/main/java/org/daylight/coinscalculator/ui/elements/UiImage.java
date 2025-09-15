package org.daylight.coinscalculator.ui.elements;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.jetbrains.annotations.NotNull;

public class UiImage extends UIElement {
    private TextureAtlasSprite image;
    private int width;
    private int height;

    public UiImage(TextureAtlasSprite image, int width, int height) {
        this.image = image;
        this.width = width;
        this.height = height;
    }

    @Override
    public int getPreferredWidth() {
        return clampWidth(width);
    }

    @Override
    public int getPreferredHeight() {
        return clampHeight(height);
    }

    @Override
    public void render(@NotNull GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        super.render(g, mouseX, mouseY, partialTick);
        if(image == null) return;
//        RenderSystem.setShaderTexture(0, image);
//        Material material = null;
//        g.blit(image, x, y, width, height, width, height);
        RenderSystem.setShaderTexture(0, image.atlasLocation());
        g.blit(x, y, 0, width, height, image);
//        Material
    }
}
