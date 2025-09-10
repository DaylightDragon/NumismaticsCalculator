package org.daylight.coinscalculator.ui.elements;

import org.daylight.coinscalculator.UiState;

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

        int padding2 = getCorrectedPadding() * 2;
        int availableMain = (isVertical() ? height : width) - padding2;
        int totalMain = (isVertical() ? getPreferredHeight() : getPreferredWidth()) - padding2;

        if (isElementsCollapsed()) return;

        // count visible children
        int visibleCount = (int) children.stream().filter(UIElement::isEnabled).count();
        if (visibleCount == 0) return;

        // determine spacing and starting offset based on distribution
        int gap = getCorrectedSpacing();
        int startOffset = 0;

        switch (mainDistribution) {
            case START -> {
                startOffset = 0;
            }
            case CENTER -> {
                startOffset = (availableMain - totalMain) / 2;
            }
            case END -> {
                startOffset = (availableMain - totalMain);
            }
            case SPACE_BETWEEN -> {
                if (visibleCount > 1) {
                    gap = (availableMain - (totalMain - (visibleCount - 1) * getCorrectedSpacing()))
                            / (visibleCount - 1);
                }
            }
            case SPACE_AROUND -> {
                if (visibleCount > 0) {
                    gap = (availableMain - (totalMain - (visibleCount - 1) * getCorrectedSpacing()))
                            / visibleCount;
                    startOffset = gap / 2;
                }
            }
            case SPACE_EVENLY -> {
                gap = (availableMain - (totalMain - (visibleCount - 1) * getCorrectedSpacing()))
                        / (visibleCount + 1);
                startOffset = gap;
            }
            case FILL -> {
                int availableMainFill = (isVertical() ? height : width) - padding2;
                int visibleCountFill = (int) children.stream().filter(UIElement::isEnabled).count();
                if (visibleCountFill == 0) return;

                int childMainSize = (availableMainFill - (visibleCountFill - 1) * getCorrectedSpacing()) / visibleCountFill;
                int childCrossSize = (isVertical() ? width : height) - padding2;

                int currentMainFill = (isVertical() ? y : x) + getCorrectedPadding();
                for (UIElement child : children) {
                    if (!child.isEnabled()) continue;

                    if (isVertical()) {
                        child.setBounds(
                                x + getCorrectedPadding(),
                                currentMainFill,
                                childCrossSize,
                                childMainSize
                        );
                        currentMainFill += childMainSize + getCorrectedSpacing();
                    } else {
                        child.setBounds(
                                currentMainFill,
                                y + getCorrectedPadding(),
                                childMainSize,
                                childCrossSize
                        );
                        currentMainFill += childMainSize + getCorrectedSpacing();
                    }
                }
                return; // return because have done everything
            }
        }

        int currentMain = (isVertical() ? y : x) + getCorrectedPadding() + startOffset;
        int availableCross = (isVertical() ? width : height) - padding2;

        for (UIElement child : children) {
            if (!child.isEnabled()) {
                child.updateInternalVisibility(false);
                continue;
            }
            if (child instanceof UIPanel panel) panel.layoutElements();

            int childMainSize = getMainSize(child);
            int childCrossSize = getCrossSize(child);

            // --- handle cross alignment ---
            int childCross = getCorrectedPadding();
            if (crossAlign == CrossAlignment.CENTER) {
                childCross += (availableCross - childCrossSize) / 2;
            } else if (crossAlign == CrossAlignment.END) {
                childCross += (availableCross - childCrossSize);
            } else if (crossAlign == CrossAlignment.STRETCH) {
                childCrossSize = availableCross;
            }

            if (isVertical()) {
                child.setBounds(x + childCross, currentMain, childCrossSize, childMainSize);
            } else {
                child.setBounds(currentMain, y + childCross, childMainSize, childCrossSize);
            }

            child.updateInternalVisibility(isEnabled() && isVisible());
            currentMain += childMainSize + gap;
        }

        if (isVertical()) {
            height = getPreferredHeight();
        } else {
            width = getPreferredWidth();
        }
        if (isVertical()) {
            int maxChildWidth = 0;
            for (UIElement child : children) {
                if (!child.isEnabled()) continue;
                maxChildWidth = Math.max(maxChildWidth, child.getWidth());
            }
            width = getCorrectedPadding() * 2 + maxChildWidth;
        } else {
            int totalWidth = getCorrectedPadding() * 2;
            int visibleCount2 = 0;
            for (UIElement child : children) {
                if (!child.isEnabled()) continue;
                totalWidth += child.getWidth();
                visibleCount2++;
            }
            if (visibleCount2 > 1) totalWidth += getCorrectedSpacing() * (visibleCount2 - 1);
            width = totalWidth;
        }
    }
}
