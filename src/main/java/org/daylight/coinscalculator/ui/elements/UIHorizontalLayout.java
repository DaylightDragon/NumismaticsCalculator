package org.daylight.coinscalculator.ui.elements;

public class UIHorizontalLayout extends UIAxisLayout {
    @Override
    protected boolean isVertical() {
        return false;
    }

    @Override
    protected int getMainSize(UIElement child) {
        return child.getPreferredWidth(); // main axis - width
    }

    @Override
    protected int getCrossSize(UIElement child) {
        return child.getPreferredHeight(); // second axis - height
    }

    @Override
    protected int alignCross(UIElement child, int availableCross) {
        int childHeight = child.getPreferredHeight();
        int cross = getCorrectedPadding();
        if (vAlign == VerticalAlignment.MIDDLE) {
            cross += (availableCross - childHeight) / 2;
        } else if (vAlign == VerticalAlignment.END) {
            cross += (availableCross - childHeight);
        }
        return cross;
    }
}
