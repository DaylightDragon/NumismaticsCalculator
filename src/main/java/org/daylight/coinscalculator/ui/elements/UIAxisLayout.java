package org.daylight.coinscalculator.ui.elements;

import org.daylight.coinscalculator.UiState;

public abstract class UIAxisLayout extends UIPanel {
    protected float spacing = 4;
    protected HorizontalAlignment hAlign = HorizontalAlignment.START;
    protected VerticalAlignment vAlign = VerticalAlignment.START;

    protected int getCorrectedSpacing() {
        return (int) (spacing * UiState.globalPanelSpacingModifier);
    }

    public UIAxisLayout setSpacing(int spacing) {
        this.spacing = spacing;
        return this;
    }

    public UIAxisLayout setAlignment(HorizontalAlignment hAlign, VerticalAlignment vAlign) {
        this.hAlign = hAlign;
        this.vAlign = vAlign;
        return this;
    }

    protected abstract boolean isVertical();

    protected abstract int getMainSize(UIElement child);   // main axis size (height for vertical)
    protected abstract int getCrossSize(UIElement child);  // cross axis size (width for vertical)
    protected abstract int alignCross(UIElement child, int availableCross); // returns offset from padding (i.e. value to add to x or y + padding)

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        layoutElements();
    }

    @Override
    public int getPreferredWidth() {
        if (isVertical()) {
            int maxWidth = 0;
            for (UIElement child : children) {
                maxWidth = Math.max(maxWidth, child.getPreferredWidth());
            }
            return maxWidth + getCorrectedPadding() * 2;
        } else {
            int totalWidth = getCorrectedPadding() * 2;
            for (UIElement child : children) {
                if (!child.isEnabled()) continue;
                totalWidth += child.getPreferredWidth() + getCorrectedSpacing();
            }
            if (!children.isEmpty()) totalWidth -= getCorrectedSpacing();
            return totalWidth;
        }
    }

    @Override
    public int getPreferredHeight() {
        if (isVertical()) {
            int totalHeight = getCorrectedPadding() * 2;
            for (UIElement child : children) {
                if (!child.isEnabled()) continue;
                totalHeight += child.getPreferredHeight() + getCorrectedSpacing();
            }
            if (!children.isEmpty()) totalHeight -= getCorrectedSpacing();
            return totalHeight;
        } else {
            int maxHeight = 0;
            for (UIElement child : children) {
                maxHeight = Math.max(maxHeight, child.getPreferredHeight());
            }
            return maxHeight + getCorrectedPadding() * 2;
        }
    }

    @Override
    public void layoutElements() {
        super.layoutElements();

        int padding2 = getCorrectedPadding() * 2;
        int availableMain = (isVertical() ? height : width) - padding2;
        int totalMain = (isVertical() ? getPreferredHeight() : getPreferredWidth()) - padding2;
        int currentMain = (isVertical() ? y : x) + getCorrectedPadding();

        if (isVertical()) {
            if (vAlign == VerticalAlignment.MIDDLE) {
                currentMain += (availableMain - totalMain) / 2;
            } else if (vAlign == VerticalAlignment.END) {
                currentMain += (availableMain - totalMain);
            }
        } else {
            if (hAlign == HorizontalAlignment.CENTER) {
                currentMain += (availableMain - totalMain) / 2;
            } else if (hAlign == HorizontalAlignment.END) {
                currentMain += (availableMain - totalMain);
            }
        }

        if (isElementsCollapsed()) return;

        int availableCross = (isVertical() ? width : height) - padding2;

        for (UIElement child : children) {
            if (!child.isEnabled()) {
                child.updateInternalVisibility(false);
                continue;
            }
            if (child instanceof UIPanel) ((UIPanel) child).layoutElements();

            int childMainSize = getMainSize(child);
            int childCrossSize = getCrossSize(child);

            int childCross = alignCross(child, availableCross);

            if (isVertical()) {
                child.setBounds(x + childCross, currentMain, childCrossSize, childMainSize);
            } else {
                child.setBounds(currentMain, y + childCross, childMainSize, childCrossSize);
            }

            child.updateInternalVisibility(isEnabled() && isVisible());
            currentMain += childMainSize + getCorrectedSpacing();
        }

        if (isVertical()) {
            height = getPreferredHeight();
        } else {
            width = getPreferredWidth();
        }
    }
}
