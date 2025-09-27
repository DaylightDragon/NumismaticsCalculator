package org.daylight.numismaticscalculator.forge.replacements.api;

import net.minecraft.client.gui.GuiGraphics;
import org.daylight.numismaticscalculator.replacements.IFont;
import org.daylight.numismaticscalculator.replacements.IGuiGraphics;
import org.daylight.numismaticscalculator.replacements.IResourceLocation;
import org.daylight.numismaticscalculator.replacements.ITextureAtlasSprite;

public class ForgeGuiGraphics implements IGuiGraphics {

    private final GuiGraphics delegate;

    public ForgeGuiGraphics(GuiGraphics delegate) {
        this.delegate = delegate;
    }

    public GuiGraphics getDelegate() {
        return delegate;
    }

    @Override
    public void drawString(IFont font, String text, int x, int y, int color) {
        if (!(font instanceof ForgeFont forgeFont)) throw new IllegalArgumentException();
        delegate.drawString(forgeFont.getDelegate(), text, x, y, color);
    }

    @Override
    public void drawTexture(IResourceLocation texture, int x, int y, int width, int height) {
        if (!(texture instanceof ForgeResourceLocation forgeTexture)) throw new IllegalArgumentException();
        delegate.blit(forgeTexture.getDelegate(), x, y, 0, 0, width, height, width, height);
    }

    @Override
    public void blit(IResourceLocation image, int x, int y, int xOffset, int yOffset, int width, int height, int textureWidth, int textureHeight) {
        if(!(image instanceof ForgeResourceLocation resourceLocation)) throw new IllegalArgumentException();
        delegate.blit(resourceLocation.getDelegate(), x, y, xOffset, yOffset, width, height, textureWidth, textureHeight);
    }

    @Override
    public void blit(IResourceLocation image, int x, int y, int xOffset, int yOffset, int width, int height) {
        if(!(image instanceof ForgeResourceLocation resourceLocation)) throw new IllegalArgumentException();
        delegate.blit(resourceLocation.getDelegate(), x, y, xOffset, yOffset, width, height);
    }

    @Override
    public void blit(int x, int y, int blitOffset, int width, int height, ITextureAtlasSprite image) {
        if (!(image instanceof ForgeTextureAtlasSprite textureAtlasSprite)) throw new IllegalArgumentException();
        delegate.blit(x, y, blitOffset, width, height, textureAtlasSprite.getDelegate());
    }

    @Override
    public Object pose() {
        return delegate.pose();
    }
}
