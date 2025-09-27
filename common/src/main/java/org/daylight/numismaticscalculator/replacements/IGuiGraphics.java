package org.daylight.numismaticscalculator.replacements;

import org.jetbrains.annotations.NotNull;

public interface IGuiGraphics {
    void drawString(@NotNull IFont font, @NotNull String text, int x, int y, int color);
    void drawTexture(@NotNull IResourceLocation texture, int x, int y, int width, int height);
    void blit(IResourceLocation image, int x, int y, int xOffset, int yOffset, int width, int height, int textureWidth, int textureHeight);
    void blit(IResourceLocation image, int x, int y, int xOffset, int yOffset, int width, int height);
    void blit(int x, int y, int i, int width, int height, ITextureAtlasSprite image);

    Object pose();
}
