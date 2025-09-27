package org.daylight.numismaticscalculator.ui.elements;

import org.daylight.numismaticscalculator.replacements.IFont;
import org.daylight.numismaticscalculator.replacements.IGuiGraphics;
import org.daylight.numismaticscalculator.replacements.SingletonInstances;
import org.jetbrains.annotations.NotNull;

public class UIText extends UIElement {
    private String text;
    private final IFont font;
    private float scale;
    private int color;

    public UIText(String text, IFont font, float scale, int color) {
        this.text = text;
        this.font = font;
        this.scale = scale;
        this.color = color;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public int getPreferredWidth() {
        return clampWidth((int) (font.width(text) * scale + 10));
    }

    @Override
    public int getPreferredHeight() {
//        System.out.println(getId() + " getPreferredHeight: " + font.lineHeight);
        return clampHeight((int) (font.lineHeight())); //  * scale
    }

    @Override
    public int getWidth() {
        return (int) (width); //  * scale
    }

    @Override
    public int getHeight() {
        return (int) (height); //  * scale
    }

    @Override
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void render(@NotNull IGuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        if(!shouldBeRendered()) return;
//        graphics.drawString(font, text, x, y, 0xFFFFFF);
        SingletonInstances.DRAWING_UTILS.drawScaledText(graphics, text, x, y, color, scale, true);
    }

    @Override
    public void updateInternalValues() {
        super.updateInternalValues();
    }

    @Override
    public boolean onClick(double mouseX, double mouseY) {
        return false;
    }
}
