package org.daylight.coinscalculator.ui.elements;

import org.daylight.coinscalculator.UiState;
import org.daylight.coinscalculator.replacements.IGuiGraphics;
import org.daylight.coinscalculator.replacements.IKeyPressEvent;
import org.daylight.coinscalculator.replacements.IRegisterListenersEvent;
import org.daylight.coinscalculator.replacements.SingletonInstances;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class UIPanel extends UIElement {
    private int bgColor = 0x77444444;
    private boolean backgroundVisible = false;
    private int outlineWidth = 0;
    private int outlineColor = 0x00000000;

    protected List<UIElement> children = new ArrayList<>();
    protected float padding = 4;
    protected int getCorrectedPadding() {
        return (int) (padding * UiState.globalPanelPaddingModifier);
    }

    protected boolean elementsCollapsed = false;

    public float getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public boolean isElementsCollapsed() {
        return elementsCollapsed;
    }

    public void setElementsCollapsed(boolean elementsCollapsed) {
        this.elementsCollapsed = elementsCollapsed;
    }

    public void addElement(UIElement element) {
        children.add(element);
    }

    @Override
    public int getPreferredWidth() {
        return clampWidth(100); // заглушка для базового контейнера
    }

    @Override
    public int getPreferredHeight() {
        return clampHeight(100); // заглушка для базового контейнера
    }

    public void setBackgroundColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public void setBackgroundVisible(boolean backgroundVisible) {
        this.backgroundVisible = backgroundVisible;
    }

    public void setOutlineWidth(int outlineWidth) {
        this.outlineWidth = outlineWidth;
    }

    public void setOutlineColor(int outlineColor) {
        this.outlineColor = outlineColor;
    }

    public int getBackgroundColor() {
        return bgColor;
    }

    public boolean isBackgroundVisible() {
        return backgroundVisible;
    }

    public void layoutElements() {
//        System.out.println(getClass().getName() + " layoutElements: " + getId());
    }

//    @Override
//    public void setEnabled(boolean enabled) {
//        super.setEnabled(enabled);
//        for(UIElement child : children) {
//            child.setEnabled(enabled); // bad idea
//            if(child instanceof UIEditBox editBox) editBox.updateInternalVisibility();
//        }
//    }

    @Override
    public void updateInternalVisibility(boolean value) {
        super.updateInternalVisibility(value);
        for(UIElement child : children) {
            child.updateInternalVisibility(value && isVisible() && isEnabled());
        }
    }

    @Override
    public void updateInternalValues() {
        super.updateInternalValues();
        for(UIElement child : children) {
            child.updateInternalValues();
        }
    }

    @Override
    public void relinkListeners(IRegisterListenersEvent event) {
//        System.out.println("RelinkListeners panel");
        super.relinkListeners(event);
        for(UIElement child : children) {
            child.relinkListeners(event);
        }
    }

    @Override
    public void keyPressed(IKeyPressEvent event) {
        if(!isEnabled()) return;
        super.keyPressed(event);
        for(UIElement child : children) {
            child.keyPressed(event);
        }
    }

    @Override
    public void render(@NotNull IGuiGraphics g, int mouseX, int mouseY, float partialTick) {
        super.render(g, mouseX, mouseY, partialTick);
//        System.out.println(shouldBeRendered() + " " + children.size());
        if(!shouldBeRendered()) return;

        int bgColor = getBackgroundColor();
        if(!backgroundVisible) bgColor = 0;
        if(bgColor != 0 || (outlineColor != 0 && outlineWidth != 0)) SingletonInstances.DRAWING_UTILS.fill(g, x, y, x + width, y + height, bgColor, outlineWidth, outlineColor);
//        g.fill(x, y, x + width, y + height, bgColor);
        for (UIElement child : children) {
//            CoinsCalculator.LOGGER.info("child: " + child.getClass().getSimpleName() + " x: " + child.x + ", y: " + child.y + ", width: " + child.width + ", height: " + child.height);
            child.render(g, mouseX, mouseY, partialTick);
        }
    }

    @Override
    public boolean onClick(double mouseX, double mouseY) {
        if(!isEnabled()) return false;
        for (UIElement child : children) {
            if(child.onClick(mouseX, mouseY)) {
//                System.out.println("Clicked " + child.getId());
                return true;
            }
        }
        return false;
    }
}
