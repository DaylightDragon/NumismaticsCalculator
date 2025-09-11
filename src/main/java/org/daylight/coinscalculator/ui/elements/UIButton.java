package org.daylight.coinscalculator.ui.elements;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.daylight.coinscalculator.ModColors;
import org.daylight.coinscalculator.util.DrawingUtils;
import org.jetbrains.annotations.NotNull;

public class UIButton extends UIElement {
    public enum ImagePosition {
        IMAGE_LEFT, IMAGE_RIGHT_KINDA
    }

    private final Font font;
    private final float fontScale;
    private final Runnable onClick;

    private int bgColorNormal = ModColors.uiButtonBg;
    private int bgColorHover = ModColors.uiButtonBgHovered;
    private int outlineWidth = 0;
    private int outlineColor = 0x00000000;
    private int textColor = 0xFFFFFFFF;

    protected String label;
    private ResourceLocation icon;
    private int iconWidth;
    private int iconHeight;
    private ImagePosition imagePosition = ImagePosition.IMAGE_LEFT;

    private int spacing = 4;     // расстояние между иконкой и текстом
    private int paddingX = 5;    // горизонтальные отступы
    private int paddingY = 6;    // вертикальные отступы

    public UIButton(String label, Font font, float fontScale, Runnable onClick) {
        this.label = label;
        this.font = font;
        this.fontScale = fontScale;
        this.onClick = onClick;
        recalcSize();
    }

    public void setLabel(String label) {
        this.label = label;
        recalcSize();
    }

    public void setIcon(ResourceLocation icon, int width, int height) {
        this.icon = icon;
        this.iconWidth = width;
        this.iconHeight = height;
        recalcSize();
    }

    public void setImagePosition(ImagePosition position) {
        this.imagePosition = position;
        recalcSize();
    }

    public void setSpacing(int spacing) {
        this.spacing = spacing;
        recalcSize();
    }

    public void setPadding(int paddingX, int paddingY) {
        this.paddingX = paddingX;
        this.paddingY = paddingY;
//        recalcSize();
    }

    public void setBgColorNormal(int bgColorNormal) {
        this.bgColorNormal = bgColorNormal;
    }

    public void setBgColorHover(int bgColorHover) {
        this.bgColorHover = bgColorHover;
    }

    public void setOutlineWidth(int outlineWidth) {
        this.outlineWidth = outlineWidth;
    }

    public void setOutlineColor(int outlineColor) {
        this.outlineColor = outlineColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    private void recalcSize() {
        // only without container
        if (width == 0) width = getPreferredWidth();
        if (height == 0) height = getPreferredHeight();
    }

    @Override
    public int getPreferredWidth() {
        int textWidth = (label != null && !label.isEmpty()) ? (int) (font.width(label) * fontScale) : 0;
        int totalWidth = textWidth + paddingX * 2;
        if (icon != null) {
            totalWidth += iconWidth;
            if (textWidth > 0) {
                totalWidth += spacing; // только если есть текст
            }
        }
        return clampWidth(totalWidth);
    }

    @Override
    public int getPreferredHeight() {
        int textHeight = (int) (font.lineHeight * fontScale);
        int contentHeight = Math.max(textHeight, icon != null ? iconHeight : 0);
        return clampHeight(contentHeight + paddingY * 2);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        if (!shouldBeRendered()) return;

        int bgColor = isMouseOver(mouseX, mouseY) ? bgColorHover : bgColorNormal;
        DrawingUtils.fill(graphics, x, y, x + width, y + height, bgColor, outlineWidth, outlineColor);
//        graphics.fill(x, y, x + width, y + height, bgColor);

        int contentX = x + paddingX;
        int contentY = y + (height - (icon != null ? Math.max(iconHeight, (int) (font.lineHeight * fontScale)) : (int) (font.lineHeight * fontScale))) / 2;

        // Рендерим иконку + текст в зависимости от imagePosition
        if (icon != null) {
            if (imagePosition == ImagePosition.IMAGE_LEFT) {
                graphics.blit(icon, contentX, contentY, 0, 0, iconWidth, iconHeight, iconWidth, iconHeight);
                contentX += iconWidth + (label != null && !label.isEmpty() ? spacing : 0);
            }
        }

        if (label != null && !label.isEmpty()) {
            UIText.drawScaledText(graphics, label, contentX, y + (height - font.lineHeight * fontScale) / 2, textColor, fontScale, true);
            if (icon != null && imagePosition == ImagePosition.IMAGE_RIGHT_KINDA) {
                contentX += (int) (font.width(label) * fontScale) + spacing;
                graphics.blit(icon, contentX, contentY, 0, 0, iconWidth, iconHeight, iconWidth, iconHeight);
            }
        } else if (icon != null && imagePosition == ImagePosition.IMAGE_RIGHT_KINDA) {
            // Если текста нет, просто центрируем иконку
            graphics.blit(icon, x + (width - iconWidth) / 2, y + (height - iconHeight) / 2, 0, 0, iconWidth, iconHeight, iconWidth, iconHeight);
        }
    }

    @Override
    public boolean onClick(double mouseX, double mouseY) {
        if (isEnabled() && isMouseOver(mouseX, mouseY)) {
            onClick.run();
            return true;
        }
        return false;
    }
}
