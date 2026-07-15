package org.daylight.numismaticscalculator.fabric.replacements.api;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.daylight.numismaticscalculator.fabric.mixins.AbstractContainerScreenAccessor;
import org.daylight.numismaticscalculator.replacements.*;

public class FabricAbstractContainerScreen<T> implements IAbstractContainerScreen<T> {
    private AbstractContainerScreen<?> delegate;

    public FabricAbstractContainerScreen(AbstractContainerScreen<?> delegate) {
        this.delegate = delegate;
    }

    public AbstractContainerScreen<?> getDelegate() {
        return delegate;
    }

    @Override
    public int getGuiLeft() {
        if(delegate instanceof AbstractContainerScreenAccessor handledScreenAccessor) {
//            System.out.println("handledScreenAccessor " + handledScreenAccessor.getGuiLeft() + " " + handledScreenAccessor.getGuiTop());
            return handledScreenAccessor.getGuiLeft();
        }
        return delegate.getRectangle().left();
    }

    @Override
    public int getGuiTop() {
        if(delegate instanceof AbstractContainerScreenAccessor handledScreenAccessor) {
            return handledScreenAccessor.getGuiTop();
        }
        return delegate.getRectangle().top();
    }

    @Override
    public int countSlots() {
        return delegate.getMenu().slots.size();
    }

    @Override
    public IAbstractContainerMenu getMenu() {
        return new FabricAbstractContainerMenu(delegate.getMenu());
    }

    @Override
    public ISlot getSlotUnderMouse() {
        if(delegate instanceof AbstractContainerScreenAccessor handledScreenAccessor) {
            return new FabricSlot(handledScreenAccessor.invokeGetSlotAt(SingletonInstances.INPUT_UTILS.getMouseX(), SingletonInstances.INPUT_UTILS.getMouseY()));
        }
        return null;
    }

    @Override
    public int width() {
        return delegate.width;
    }

    @Override
    public int height() {
        return delegate.height;
    }

    @Override
    public IAbstractContainerScreen<?> getAsAbstractContainerScreen() {
        return new FabricAbstractContainerScreen<>(delegate);
    }

    @Override
    public boolean isModSettingsScreen() {
        return false;
    }

    @Override
    public IModSettingsScreen getAsModSettingsScreen() {
        throw new UnsupportedOperationException("This is not a ModSettingsScreen");
    }

    @Override
    public boolean isAbstractContainerScreen() {
        return delegate instanceof AbstractContainerScreen<?>;
    }
}
