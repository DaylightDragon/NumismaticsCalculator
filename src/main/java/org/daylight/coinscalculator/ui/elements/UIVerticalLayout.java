package org.daylight.coinscalculator.ui.elements;

public class UIVerticalLayout extends UIPanel {
    private int spacing = 4;
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
        return maxWidth + padding * 2;
    }

    @Override
    public int getPreferredHeight() {
        int totalHeight = padding * 2;
        for (UIElement child : children) {
            if(!child.isEnabled()) continue;
//            System.out.println(getId() + " child height: " + child.getPreferredHeight());
            totalHeight += child.getPreferredHeight() + spacing;
        }
        if (!children.isEmpty()) totalHeight -= spacing;
//        System.out.println(getId() + " getPreferredHeight: " + totalHeight);
        return totalHeight;
    }

    @Override
    public void layoutElements() {
        super.layoutElements();
//        CoinsCalculator.LOGGER.info(getClass().getSimpleName() +
//                " layoutElements -> panel x: " + x + ", y: " + y + ", width: " + width + ", height: " + height);

        int totalHeight = getPreferredHeight() - padding * 2;
        int availableHeight = height - padding * 2;
        int currentY = y + padding;

        // Выравнивание по вертикали
        if (vAlign == VerticalAlignment.MIDDLE) {
            currentY += (availableHeight - (totalHeight - padding * 2)) / 2;
        } else if (vAlign == VerticalAlignment.BOTTOM) {
            currentY += (availableHeight - (totalHeight - padding * 2));
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

            int childX = x + padding;
            if (hAlign == HorizontalAlignment.CENTER) {
                childX += (width - padding * 2 - childWidth) / 2;
            } else if (hAlign == HorizontalAlignment.RIGHT) {
                childX += (width - padding * 2 - childWidth);
            }

            child.setBounds(childX, currentY, childWidth, childHeight);
//            child.setBounds(child.getX(), child.getY(), child.getWidth(), child.getPreferredHeight()); // may be important
            child.updateInternalVisibility(true);
            currentY += childHeight + spacing;
        }

        height = getPreferredHeight();
    }
}

