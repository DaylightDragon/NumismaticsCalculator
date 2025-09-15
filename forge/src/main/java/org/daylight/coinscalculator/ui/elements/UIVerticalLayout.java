package org.daylight.coinscalculator.ui.elements;

public class UIVerticalLayout extends UIAxisLayout {
    @Override
    protected boolean isVertical() {
        return true;
    }

    @Override
    protected int getMainSize(UIElement child) {
        return child.getPreferredHeight();
    }

    @Override
    protected int getCrossSize(UIElement child) {
        return child.getPreferredWidth();
    }
}
