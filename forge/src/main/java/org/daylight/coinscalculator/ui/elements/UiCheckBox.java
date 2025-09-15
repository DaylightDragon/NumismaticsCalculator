package org.daylight.coinscalculator.ui.elements;

import net.minecraft.client.gui.Font;
import org.daylight.coinscalculator.ModResources;

public class UiCheckBox extends UIButton {
    protected boolean selected = false;

    public UiCheckBox(String label, Font font, float fontScale, Runnable onClick) {
        super(label, font, fontScale, onClick);
        setIcon(ModResources.CHECKBOX_NORMAL, 14, 14);
        setImagePosition(ImagePosition.IMAGE_LEFT);
        setPadding(0, 0);
        setBgColorNormal(0x00000000);
        setBgColorHover(0x00000000);
//        setBgColorHover(ModColors.uiButtonBgHovered);
    }

    @Override
    public boolean onClick(double mouseX, double mouseY) {
        boolean result = super.onClick(mouseX, mouseY);
        if(!result) return false;
        selected = !selected;
        if(selected) setIcon(ModResources.CHECKBOX_SELECTED, 14, 14);
        else setIcon(ModResources.CHECKBOX_NORMAL, 14, 14);
        return true;
    }
}
