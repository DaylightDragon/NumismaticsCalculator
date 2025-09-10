package org.daylight.coinscalculator.ui.elements;

public class UIVerticalLayout extends UIAxisLayout {
    @Override
    protected boolean isVertical() {
        return true;
    }

    @Override
    protected int getMainSize(UIElement child) {
        return child.getPreferredHeight(); // main axis - height
    }

    @Override
    protected int getCrossSize(UIElement child) {
        return child.getPreferredWidth(); // second axis - width
    }

    @Override
    protected int alignCross(UIElement child, int availableCross) {
        int childWidth = child.getPreferredWidth();
        int cross = getCorrectedPadding();
        if (hAlign == HorizontalAlignment.CENTER) {
            cross += (availableCross - childWidth) / 2;
        } else if (hAlign == HorizontalAlignment.END) {
            cross += (availableCross - childWidth);
        }
        return cross;
    }
}

