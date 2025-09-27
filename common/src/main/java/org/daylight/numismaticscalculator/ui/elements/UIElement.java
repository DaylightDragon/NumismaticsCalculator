package org.daylight.numismaticscalculator.ui.elements;

import org.daylight.numismaticscalculator.replacements.IGuiGraphics;
import org.daylight.numismaticscalculator.replacements.IKeyPressEvent;
import org.daylight.numismaticscalculator.replacements.IRegisterListenersEvent;
import org.jetbrains.annotations.NotNull;

public abstract class UIElement {
    protected String id = "Unknown" + getClass().getSimpleName();
    protected int x, y, width, height; // change to protected later, DO NOT SET FROM OUTSIDE
    protected int minWidth = 0, maxWidth = Integer.MAX_VALUE;
    protected int minHeight = 0, maxHeight = Integer.MAX_VALUE;
    protected boolean isVisible = true;
    protected boolean isEnabled = true;
    private boolean crossAxisExcludedFromLayout = false;
    private boolean heightExcludedFromLayout = false;

    public void setCrossAxisExcludedFromLayout(boolean excluded) {
        this.crossAxisExcludedFromLayout = excluded;
    }

    public boolean isCrossAxisExcludedFromLayout() {
        return crossAxisExcludedFromLayout;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public boolean shouldBeRendered() {
        return isEnabled && isVisible;
    }

    public void setVisible(boolean visible) {
        this.isVisible = visible;
        updateInternalVisibility(isEnabled() && isVisible());
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
        updateInternalVisibility(isEnabled() && isVisible());
    }

    public void updateInternalVisibility(boolean value) {
//        System.out.println(getClass().getSimpleName() + " updateInternalVisibility: " + value);
    }

    public abstract int getPreferredWidth();
    public abstract int getPreferredHeight();

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void updateInternalValues() {}

    public void setMinWidth(int minWidth) {
        this.minWidth = minWidth;
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    public void setMinHeight(int minHeight) {
        this.minHeight = minHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
//        CoinsCalculator.LOGGER.info(getClass().getSimpleName() +
//                " setPosition -> x: " + x + ", y: " + y);
    }

    public void setBounds(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = Math.max(minWidth, Math.min(width, maxWidth));
        this.height = Math.max(minHeight, Math.min(height, maxHeight));
//        CoinsCalculator.LOGGER.info(getClass().getSimpleName() +
//                " setBounds -> x: " + x + ", y: " + y + ", width: " + width + ", height: " + height);
    }

    protected int clampWidth(int value) {
        return Math.max(minWidth, Math.min(value, maxWidth));
    }

    protected int clampHeight(int value) {
        return Math.max(minHeight, Math.min(value, maxHeight));
    }

    public void render(@NotNull IGuiGraphics g, int mouseX, int mouseY, float partialTick) {
        updateInternalValues();
    }

    public boolean onClick(double mouseX, double mouseY) {
        // true to stop checks
        return false;
    }

    protected boolean isMouseOver(double mx, double my) {
        return mx >= x && mx <= x + width && my >= y && my <= y + height;
    }

    public void relinkListeners(IRegisterListenersEvent event) {}

    public void keyPressed(IKeyPressEvent event) {}
}
