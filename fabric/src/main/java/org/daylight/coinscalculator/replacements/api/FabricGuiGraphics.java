package org.daylight.coinscalculator.replacements.api;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.texture.Sprite;
import net.minecraft.item.Item;
import org.daylight.coinscalculator.replacements.*;
import org.jetbrains.annotations.NotNull;

public class FabricGuiGraphics implements IGuiGraphics {
    private final DrawContext delegate;

    public FabricGuiGraphics(DrawContext delegate) {
        this.delegate = delegate;
    }

    public DrawContext getDelegate() {
        return delegate;
    }

    @Override
    public void drawString(@NotNull IFont font, @NotNull String text, int x, int y, int color) {
        if (!(font instanceof FabricFont fabricFont)) throw new IllegalArgumentException();
        // DrawContext.drawText(Font, String, x, y, color, shadow)
        delegate.drawText(fabricFont.getDelegate(), text, x, y, color, false);
    }

    @Override
    public void drawTexture(@NotNull IResourceLocation texture, int x, int y, int width, int height) {
        if (!(texture instanceof FabricResourceLocation fabricTexture)) throw new IllegalArgumentException();
        // DrawContext.drawTexture(ResourceLocation, x, y, width, height, uOffset, vOffset, textureWidth, textureHeight)
        delegate.drawTexture(fabricTexture.getDelegate(), x, y, 0, 0, width, height, width, height);
    }

    @Override
    public void blit(IResourceLocation image, int x, int y, int xOffset, int yOffset, int width, int height, int textureWidth, int textureHeight) {
        if (!(image instanceof FabricResourceLocation fabricTexture)) throw new IllegalArgumentException();
        delegate.drawTexture(fabricTexture.getDelegate(), x, y, xOffset, yOffset, width, height, textureWidth, textureHeight);
    }

    @Override
    public void blit(IResourceLocation image, int x, int y, int xOffset, int yOffset, int width, int height) {
        if (!(image instanceof FabricResourceLocation fabricTexture)) throw new IllegalArgumentException();
        delegate.drawTexture(fabricTexture.getDelegate(), x, y, xOffset, yOffset, width, height, width, height);
    }

    @Override
    public void blit(int x, int y, int i, int width, int height, ITextureAtlasSprite sprite) { // TODO test
        if (!(sprite instanceof FabricTextureAtlasSprite atlasSprite)) throw new IllegalArgumentException();
        Sprite s = atlasSprite.getDelegate();
        delegate.drawTexture(
                s.getAtlasId(),
                x, y,                    // screen coordinates
                (int)(s.getMinU() * s.getContents().getWidth()),
                (int)(s.getMinV() * s.getContents().getHeight()),
                width, height,           // size on screen
                s.getContents().getWidth(),
                s.getContents().getHeight()
        );
    }

    @Override
    public Object pose() {
        return delegate.getMatrices();
    }
}
