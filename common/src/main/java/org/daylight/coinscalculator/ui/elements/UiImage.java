package org.daylight.coinscalculator.ui.elements;

import org.daylight.coinscalculator.replacements.IGuiGraphics;
import org.daylight.coinscalculator.replacements.ITextureAtlasSprite;
import org.daylight.coinscalculator.replacements.SingletonInstances;
import org.jetbrains.annotations.NotNull;

public class UiImage extends UIElement {
    private ITextureAtlasSprite image;
    private int width;
    private int height;

    public UiImage(ITextureAtlasSprite image, int width, int height) {
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
    public void render(@NotNull IGuiGraphics g, int mouseX, int mouseY, float partialTick) {
        super.render(g, mouseX, mouseY, partialTick);
        if(image == null) return;
//        RenderSystem.setShaderTexture(0, image);
//        Material material = null;
//        g.blit(image, x, y, width, height, width, height);
        SingletonInstances.RENDER_SYSTEM.setShaderTexture(0, image.asAtlasLocation());
        g.blit(x, y, 0, width, height, image);
//        Material
    }
}
