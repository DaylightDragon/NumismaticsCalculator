package org.daylight.coinscalculator.ui.elements;

import org.daylight.coinscalculator.UiState;

import java.util.List;

public abstract class UIAxisLayout extends UIPanel {
    protected float spacing = 4;

    // alignment along the cross axis (perpendicular to main)
    protected CrossAlignment crossAlign = CrossAlignment.START;

    // distribution along the main axis
    protected MainDistribution mainDistribution = MainDistribution.START;

    protected int getCorrectedSpacing() {
        return (int) (spacing * UiState.globalPanelSpacingModifier);
    }

    public UIAxisLayout setSpacing(int spacing) {
        this.spacing = spacing;
        return this;
    }

    public UIAxisLayout setCrossAlignment(CrossAlignment align) {
        this.crossAlign = align;
        return this;
    }

    public UIAxisLayout setMainDistribution(MainDistribution distribution) {
        this.mainDistribution = distribution;
        return this;
    }

    protected abstract boolean isVertical();

    protected abstract int getMainSize(UIElement child);   // main axis size (height for vertical)
    protected abstract int getCrossSize(UIElement child);  // cross axis size (width for vertical)

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

        if (isElementsCollapsed()) return;

        int padding2 = getCorrectedPadding() * 2;
        int availableMain = (isVertical() ? height : width) - padding2;
        int availableCross = (isVertical() ? width : height) - padding2;

        // collect visible children
        List<UIElement> visibleChildren = children.stream()
                .filter(UIElement::isEnabled)
                .toList();
        int count = visibleChildren.size();
        if (count == 0) return;

        // --- calculate gap and start offset depending on distribution ---
        int gap = getCorrectedSpacing();
        int startOffset = 0;

        int totalMainPreferred = 0;
        for (UIElement child : visibleChildren) {
            totalMainPreferred += getMainSize(child);
        }
        totalMainPreferred += (count - 1) * getCorrectedSpacing();

        switch (mainDistribution) {
            case START -> startOffset = 0;
            case CENTER -> startOffset = (availableMain - totalMainPreferred) / 2;
            case END -> startOffset = availableMain - totalMainPreferred;
            case SPACE_BETWEEN -> {
                if (count > 1) {
                    gap = (availableMain - (totalMainPreferred - (count - 1) * getCorrectedSpacing()))
                            / (count - 1);
                }
            }
            case SPACE_AROUND -> {
                gap = (availableMain - (totalMainPreferred - (count - 1) * getCorrectedSpacing())) / count;
                startOffset = gap / 2;
            }
            case SPACE_EVENLY -> {
                gap = (availableMain - (totalMainPreferred - (count - 1) * getCorrectedSpacing())) / (count + 1);
                startOffset = gap;
            }
            case FILL -> {
                // handled separately below
            }
        }

        int currentMain = (isVertical() ? y : x) + getCorrectedPadding() + startOffset;

        if (mainDistribution == MainDistribution.FILL) {
            int childMainSize = (availableMain - (count - 1) * getCorrectedSpacing()) / count;

            for (UIElement child : visibleChildren) {
                applyCrossAlignment(child, availableCross, currentMain, childMainSize);
                currentMain += childMainSize + getCorrectedSpacing();
            }
        } else {
            for (UIElement child : visibleChildren) {
                int childMainSize = getMainSize(child);
                applyCrossAlignment(child, availableCross, currentMain, childMainSize);
                currentMain += childMainSize + gap;
            }
        }

        // --- recalc panel size after layout ---
        if (isVertical()) {
            height = getPreferredHeight();
            int maxChildWidth = visibleChildren.stream()
                    .mapToInt(UIElement::getWidth)
                    .max()
                    .orElse(0);
            width = getCorrectedPadding() * 2 + maxChildWidth;
        } else {
            width = getPreferredWidth();
            int totalWidth = getCorrectedPadding() * 2
                    + visibleChildren.stream().mapToInt(UIElement::getWidth).sum()
                    + Math.max(0, (count - 1) * getCorrectedSpacing());
            width = totalWidth;
        }
    }

    // helper method, applies cross alignment and sets bounds
    private void applyCrossAlignment(UIElement child, int availableCross, int currentMain, int childMainSize) {
        int childCrossSize = getCrossSize(child);
        int crossPos = (isVertical() ? x : y) + getCorrectedPadding();

        switch (crossAlign) {
            case CENTER -> crossPos += (availableCross - childCrossSize) / 2;
            case END -> crossPos += (availableCross - childCrossSize);
            case STRETCH -> childCrossSize = availableCross;
        }

        if (isVertical()) {
            child.setBounds(crossPos, currentMain, childCrossSize, childMainSize);
        } else {
            child.setBounds(currentMain, crossPos, childMainSize, childCrossSize);
        }

        if (child instanceof UIPanel panel) {
            panel.layoutElements();
        }
        child.updateInternalVisibility(isEnabled() && isVisible());
    }
}
