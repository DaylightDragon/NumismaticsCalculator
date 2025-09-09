package org.daylight.coinscalculator.ui.elements;

import org.daylight.coinscalculator.UiState;

public class UIHorizontalLayout extends UIPanel {
    private float spacing = 4;
    private int getCorrectedSpacing() {
        return (int) (spacing * UiState.globalPanelSpacingModifier);
    }

    private HorizontalAlignment hAlign = HorizontalAlignment.START;
    private VerticalAlignment vAlign = VerticalAlignment.START;

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
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        layoutElements(); // как и в вертикальном
    }

    @Override
    public int getPreferredWidth() {
        // Сумма ширин enabled-детей + spacing между ними + padding*2
        int total = 0;
        int enabledCount = 0;
        for (UIElement child : children) {
            if (!child.isEnabled()) continue;
            total += child.getPreferredWidth();
            enabledCount++;
        }
        if (enabledCount > 1) total += getCorrectedSpacing() * (enabledCount - 1);
        int result = total + getCorrectedPadding() * 2;
//        System.out.println(getId() + " getPreferredWidth: " + result);
        return result;
    }

    @Override
    public int getPreferredHeight() {
        // Максимальная высота среди детей (как у вертикального maxWidth) + padding*2
        int maxH = 0;
        for (UIElement child : children) {
            maxH = Math.max(maxH, child.getPreferredHeight());
        }
        return maxH + getCorrectedPadding() * 2;
    }

    @Override
    public void layoutElements() {
        super.layoutElements();

        // Предварительно посчитаем ширину контента (только enabled), чтобы корректно выровнять по hAlign
        int contentW = 0;
        int enabledCount = 0;
        for (UIElement child : children) {
            if (!child.isEnabled()) continue;
            contentW += child.getPreferredWidth();
            enabledCount++;
        }
        if (enabledCount > 1) contentW += getCorrectedSpacing() * (enabledCount - 1);

        int availableW = Math.max(0, width - getCorrectedPadding() * 2);
        int currentX = x + getCorrectedPadding();

        // Горизонтальное выравнивание ряда
        if (hAlign == HorizontalAlignment.CENTER) {
            currentX += (availableW - contentW) / 2;
        } else if (hAlign == HorizontalAlignment.END) {
            currentX += (availableW - contentW);
        }

        if (isElementsCollapsed()) return;

        for (UIElement child : children) {
            if (!child.isEnabled()) {
                child.updateInternalVisibility(false);
                continue;
            }

            // Вложенные панели — сначала их внутренний layout (как у вертикального)
            if (child instanceof UIPanel) {
                ((UIPanel) child).layoutElements();
            }

            int childW = child.getPreferredWidth();
            int childH = child.getPreferredHeight();

            // Вертикальное выравнивание каждого элемента в своей "колонке"
            int childY = y + getCorrectedPadding();
            int innerH = Math.max(0, height - getCorrectedPadding() * 2);
            if (vAlign == VerticalAlignment.MIDDLE) {
                childY += (innerH - childH) / 2;
            } else if (vAlign == VerticalAlignment.END) {
                childY += (innerH - childH);
            }

            child.setBounds(currentX, childY, childW, childH);
            child.updateInternalVisibility(isEnabled() && isVisible());
            currentX += childW + getCorrectedSpacing();
        }

        // Как в вертикальном: фиксируем "гибкую" меру контейнера под контент
        width = getPreferredWidth();
    }
}
