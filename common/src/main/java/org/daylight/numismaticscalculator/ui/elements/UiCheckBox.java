package org.daylight.numismaticscalculator.ui.elements;

import org.daylight.numismaticscalculator.replacements.IFont;
import org.daylight.numismaticscalculator.replacements.IModResources;
import org.daylight.numismaticscalculator.replacements.SingletonInstances;

public class UiCheckBox extends UIButton {
    protected boolean selected = false;

    public UiCheckBox(String label, IFont font, float fontScale, Runnable onClick) {
        super(label, font, fontScale, onClick);
        setIcon(SingletonInstances.MOD_RESOURCES.getResourceLocation(IModResources.Type.CHECKBOX_NORMAL), 14, 14);
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
        if(selected) setIcon(SingletonInstances.MOD_RESOURCES.getResourceLocation(IModResources.Type.CHECKBOX_SELECTED), 14, 14);
        else setIcon(SingletonInstances.MOD_RESOURCES.getResourceLocation(IModResources.Type.CHECKBOX_NORMAL), 14, 14);
        return true;
    }
}
