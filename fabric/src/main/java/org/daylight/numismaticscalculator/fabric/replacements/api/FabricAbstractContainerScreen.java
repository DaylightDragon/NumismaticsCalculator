package org.daylight.numismaticscalculator.fabric.replacements.api;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.daylight.numismaticscalculator.fabric.mixins.HandledScreenAccessor;
import org.daylight.numismaticscalculator.replacements.*;

public class FabricAbstractContainerScreen<T> implements IAbstractContainerScreen<T> {
    private HandledScreen<?> delegate;

    public FabricAbstractContainerScreen(HandledScreen<?> delegate) {
        this.delegate = delegate;
    }

    public HandledScreen<?> getDelegate() {
        return delegate;
    }

    @Override
    public int getGuiLeft() {
        if(delegate instanceof HandledScreenAccessor handledScreenAccessor) {
//            System.out.println("handledScreenAccessor " + handledScreenAccessor.getGuiLeft() + " " + handledScreenAccessor.getGuiTop());
            return handledScreenAccessor.getGuiLeft();
        }
        return delegate.getNavigationFocus().getLeft();
    }

    @Override
    public int getGuiTop() {
        if(delegate instanceof HandledScreenAccessor handledScreenAccessor) {
            return handledScreenAccessor.getGuiTop();
        }
        return delegate.getNavigationFocus().getTop();
    }

    @Override
    public int countSlots() {
        return delegate.getScreenHandler().slots.size();
    }

    @Override
    public IAbstractContainerMenu getMenu() {
        return new FabricAbstractContainerMenu(delegate.getScreenHandler());
    }

    @Override
    public ISlot getSlotUnderMouse() {
        if(delegate instanceof HandledScreenAccessor handledScreenAccessor) {
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
        return new FabricAbstractContainerScreen<>((HandledScreen<?>) delegate);
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
        return delegate instanceof HandledScreen<?>;
    }
}
