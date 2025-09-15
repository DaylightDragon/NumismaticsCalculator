package org.daylight.coinscalculator.ui.elements;

public class UIStackLayout extends UIVerticalLayout {
    private int activeIndex = 0;

    @Override
    public void addElement(UIElement element) {
        super.addElement(element);
        if(activeIndex != children.size() - 1) {
            element.setEnabled(false);
        } else {
            element.setEnabled(true);
        }
    }

    public UIStackLayout setNextIndex() {
        int index = activeIndex + 1;
        if(index >= children.size()) index = 0;
        setActiveIndex(index);
        return this;
    }

    public UIStackLayout setPrevIndex() {
        int index = activeIndex - 1;
        if(index < 0) index = children.size() - 1;
        setActiveIndex(index);
        return this;
    }

    public UIStackLayout setActiveIndex(int index) {
        if (index < 0 || index >= children.size() || index == activeIndex) {
            return this;
        }

        children.get(activeIndex).setEnabled(false);
        this.activeIndex = index;
        children.get(activeIndex).setEnabled(true);

//        System.out.println(activeIndex);

//        layoutElements();
        return this;
    }

    public int getActiveIndex() {
        return activeIndex;
    }
}
