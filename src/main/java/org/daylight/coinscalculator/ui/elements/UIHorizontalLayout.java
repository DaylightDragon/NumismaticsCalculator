package org.daylight.coinscalculator.ui.elements;

public class UIHorizontalLayout extends UIPanel {
    private int spacing = 4;
    private HorizontalAlignment hAlign = HorizontalAlignment.LEFT;
    private VerticalAlignment vAlign = VerticalAlignment.TOP;

    public UIHorizontalLayout setSpacing(int spacing) {
        this.spacing = spacing;
        return this;
    }

    public UIHorizontalLayout setAlignment(HorizontalAlignment hAlign, VerticalAlignment vAlign) {
        this.hAlign = hAlign;
        this.vAlign = vAlign;
        return this;
    }

    @Override
    public int getPreferredWidth() {
        int totalWidth = padding * 2;
        for (UIElement child : children) {
            totalWidth += child.getPreferredWidth() + spacing;
        }
        if (!children.isEmpty()) totalWidth -= spacing; // убираем последний gap
        return totalWidth;
    }

    @Override
    public int getPreferredHeight() {
        int maxHeight = 0;
        for (UIElement child : children) {
            maxHeight = Math.max(maxHeight, child.getPreferredHeight());
        }
        return maxHeight + padding * 2;
    }

    @Override
    public void layoutElements() {
        int totalWidth = getPreferredWidth() - padding * 2;
        int availableWidth = width - padding * 2;
        int currentX = x + padding;

        // Выравнивание по горизонтали
        if (hAlign == HorizontalAlignment.CENTER) {
            currentX += (availableWidth - (totalWidth - padding * 2)) / 2;
        } else if (hAlign == HorizontalAlignment.RIGHT) {
            currentX += (availableWidth - (totalWidth - padding * 2));
        }

        for (UIElement child : children) {
            int childWidth = child.getPreferredWidth();
            int childHeight = child.getPreferredHeight();

            int childY = y + padding;
            if (vAlign == VerticalAlignment.MIDDLE) {
                childY += (height - padding * 2 - childHeight) / 2;
            } else if (vAlign == VerticalAlignment.BOTTOM) {
                childY += (height - padding * 2 - childHeight);
            }

            child.setBounds(currentX, childY, childWidth, childHeight);
            currentX += childWidth + spacing;
        }
    }
}
