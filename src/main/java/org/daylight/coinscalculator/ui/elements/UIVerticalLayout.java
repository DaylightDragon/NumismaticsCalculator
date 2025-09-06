package org.daylight.coinscalculator.ui.elements;

import org.daylight.coinscalculator.UiState;

public class UIVerticalLayout extends UIPanel {
    private float spacing = 4;
    private int getCorrectedSpacing() {
        return (int) (spacing * UiState.globalPanelSpacingModifier);
    }

    private HorizontalAlignment hAlign = HorizontalAlignment.LEFT;
    private VerticalAlignment vAlign = VerticalAlignment.TOP;

    public UIVerticalLayout setSpacing(int spacing) {
        this.spacing = spacing;
        return this;
    }

    public UIVerticalLayout setAlignment(HorizontalAlignment hAlign, VerticalAlignment vAlign) {
        this.hAlign = hAlign;
        this.vAlign = vAlign;
        return this;
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        layoutElements();
    }

    @Override
    public int getPreferredWidth() {
        int maxWidth = 0;
        for (UIElement child : children) {
            maxWidth = Math.max(maxWidth, child.getPreferredWidth());
        }
        return maxWidth + getCorrectedPadding() * 2;
    }

    @Override
    public int getPreferredHeight() {
        int totalHeight = getCorrectedPadding() * 2;
        for (UIElement child : children) {
            if(!child.isEnabled()) continue;
//            System.out.println(getId() + " child height: " + child.getPreferredHeight());
            totalHeight += child.getPreferredHeight() + getCorrectedSpacing();
        }
        if (!children.isEmpty()) totalHeight -= getCorrectedSpacing();
//        System.out.println(getId() + " getPreferredHeight: " + totalHeight);
        return totalHeight;
    }

    @Override
    public void layoutElements() {
        super.layoutElements();
//        CoinsCalculator.LOGGER.info(getClass().getSimpleName() +
//                " layoutElements -> panel x: " + x + ", y: " + y + ", width: " + width + ", height: " + height);

        int totalHeight = getPreferredHeight() - getCorrectedPadding() * 2;
        int availableHeight = height - getCorrectedPadding() * 2;
        int currentY = y + getCorrectedPadding();

        // Выравнивание по вертикали
        if (vAlign == VerticalAlignment.MIDDLE) {
            currentY += (availableHeight - (totalHeight - getCorrectedPadding() * 2)) / 2;
        } else if (vAlign == VerticalAlignment.BOTTOM) {
            currentY += (availableHeight - (totalHeight - getCorrectedPadding() * 2));
        }

        if(isElementsCollapsed()) return;

        for (UIElement child : children) {
            if(!child.isEnabled()) {
                child.updateInternalVisibility(false);
                continue;
            }
//            System.out.println("child: " + child.getId());
            if(child instanceof UIPanel) ((UIPanel) child).layoutElements();

            int childWidth = child.getPreferredWidth();
            int childHeight = child.getPreferredHeight();

            int childX = x + getCorrectedPadding();
            if (hAlign == HorizontalAlignment.CENTER) {
                childX += (width - getCorrectedPadding() * 2 - childWidth) / 2;
            } else if (hAlign == HorizontalAlignment.RIGHT) {
                childX += (width - getCorrectedPadding() * 2 - childWidth);
            }

            child.setBounds(childX, currentY, childWidth, childHeight);
//            child.setBounds(child.getX(), child.getY(), child.getWidth(), child.getPreferredHeight()); // may be important
//            System.out.println("update child visibility");
            child.updateInternalVisibility(isEnabled() && isVisible()); // true
            currentY += childHeight + getCorrectedSpacing();
        }

        height = getPreferredHeight();
    }
}

