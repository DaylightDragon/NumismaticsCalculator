package org.daylight.numismaticscalculator.fabric.replacements.api;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.daylight.numismaticscalculator.replacements.*;
import org.jetbrains.annotations.NotNull;

public class FabricGuiGraphics implements IGuiGraphics {
    private final GuiGraphics delegate;

    public FabricGuiGraphics(GuiGraphics delegate) {
        this.delegate = delegate;
    }

    public GuiGraphics getDelegate() {
        return delegate;
    }

    @Override
    public void drawString(@NotNull IFont font, @NotNull String text, int x, int y, int color) {
        if (!(font instanceof FabricFont fabricFont)) throw new IllegalArgumentException();
        // DrawContext.drawText(Font, String, x, y, color, shadow)
        delegate.drawString(fabricFont.getDelegate(), text, x, y, color, false);
    }

    @Override
    public void drawTexture(@NotNull IResourceLocation texture, int x, int y, int width, int height) {
        if (!(texture instanceof FabricResourceLocation fabricTexture)) throw new IllegalArgumentException();
        // DrawContext.drawTexture(ResourceLocation, x, y, width, height, uOffset, vOffset, textureWidth, textureHeight)
        delegate.blitSprite(fabricTexture.getDelegate(), x, y, 0, 0, width, height, width, height);
    }

    @Override
    public void blit(IResourceLocation image, int x, int y, int xOffset, int yOffset, int width, int height, int textureWidth, int textureHeight) {
        if (!(image instanceof FabricResourceLocation fabricTexture)) throw new IllegalArgumentException();
        delegate.blitSprite(fabricTexture.getDelegate(), x, y, xOffset, yOffset, width, height, textureWidth, textureHeight);
    }

    @Override
    public void blit(IResourceLocation image, int x, int y, int xOffset, int yOffset, int width, int height) {
        if (!(image instanceof FabricResourceLocation fabricTexture)) throw new IllegalArgumentException();
        delegate.blitSprite(fabricTexture.getDelegate(), x, y, xOffset, yOffset, width, height, width, height);
    }

    @Override
    public void blit(int x, int y, int i, int width, int height, ITextureAtlasSprite sprite) {
        if (!(sprite instanceof FabricTextureAtlasSprite atlasSprite)) throw new IllegalArgumentException();
        TextureAtlasSprite s = atlasSprite.getDelegate();

        delegate.blitSprite(s.atlasLocation(), x, y, i, width, height);
    }

    @Override
    public Object pose() {
        return delegate.pose();
    }
}
