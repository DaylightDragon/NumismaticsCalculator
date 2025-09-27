package org.daylight.numismaticscalculator.ui.elements;

public class UIHorizontalLayout extends UIAxisLayout {
    @Override
    protected boolean isVertical() {
        return false;
    }

    @Override
    protected int getMainSize(UIElement child) {
        return child.getPreferredWidth();
    }

    @Override
    protected int getCrossSize(UIElement child) {
        return child.getPreferredHeight();
    }
}
