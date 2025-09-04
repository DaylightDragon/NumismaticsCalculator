package org.daylight.coinscalculator.ui.elements;

import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayList;
import java.util.List;

public abstract class UIPanel extends UIElement {
    public enum HorizontalAlignment {
        LEFT, CENTER, RIGHT
    }

    public enum VerticalAlignment {
        TOP, MIDDLE, BOTTOM
    }

    private int bgColor = 0x77444444;
    private boolean backgroundVisible = false;

    protected List<UIElement> children = new ArrayList<>();
    protected int padding = 4;

    protected boolean elementsCollapsed = false;

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
        return 100; // заглушка для базового контейнера
    }

    @Override
    public int getPreferredHeight() {
        return 100; // заглушка для базового контейнера
    }

    public void setBackgroundColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public void setBackgroundVisible(boolean backgroundVisible) {
        this.backgroundVisible = backgroundVisible;
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
            child.updateInternalVisibility(isVisible() && isEnabled());
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
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        super.render(g, mouseX, mouseY, partialTick);
//        System.out.println(shouldBeRendered() + " " + children.size());
        if(!shouldBeRendered()) return;

        if(backgroundVisible) {
            g.fill(x, y, x + width, y + height, bgColor);
        }
        for (UIElement child : children) {
//            CoinsCalculator.LOGGER.info("child: " + child.getClass().getSimpleName() + " x: " + child.x + ", y: " + child.y + ", width: " + child.width + ", height: " + child.height);
            child.render(g, mouseX, mouseY, partialTick);
        }
    }

    @Override
    public boolean onClick(double mouseX, double mouseY) {
        for (UIElement child : children) {
            if(child.onClick(mouseX, mouseY)) return true;
        }
        return false;
    }
}
