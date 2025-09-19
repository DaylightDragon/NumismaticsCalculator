package org.daylight.coinscalculator.replacements;

import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraftforge.client.event.ScreenEvent;
import org.daylight.coinscalculator.ui.screens.ForgeModSettingsScreen;
import org.daylight.coinscalculator.ui.screens.ModSettingsScreen;

public class ForgeRegisterListenersEvent implements IRegisterListenersEvent {
    private ScreenEvent.Init.Post delegate;

    public ForgeRegisterListenersEvent(ScreenEvent.Init.Post delegate) {
        this.delegate = delegate;
    }

    public ScreenEvent.Init.Post getDelegate() {
        return delegate;
    }


    @Override
    public boolean containsListener(Object listener) {
        if(listener instanceof ForgeEditBox forgeEditBox) {
            return delegate.getListenersList().contains(forgeEditBox.getDelegate());
        }

        if(!(listener instanceof GuiEventListener)) throw new IllegalArgumentException();
        return delegate.getListenersList().contains(listener);
    }

    @Override
    public void addListener(Object listener) {
        if(listener instanceof ForgeEditBox forgeEditBox) {
            delegate.addListener(forgeEditBox.getDelegate());
            return;
        }

        if(!(listener instanceof GuiEventListener)) throw new IllegalArgumentException();
        delegate.addListener((GuiEventListener) listener);
    }

    @Override
    public boolean isAbstractContainerScreen() {
        return delegate.getScreen() instanceof AbstractContainerScreen<?>;
    }

    @Override
    public boolean isModSettingsScreen() {
        return delegate.getScreen() instanceof ModSettingsScreen;
    }

    @Override
    public IScreen getScreen() {
        return new ForgeScreen(delegate.getScreen());
    }

    @Override
    public IAbstractContainerScreen<?> getAsAbstractContainerScreen() {
        return new ForgeAbstractContainerScreen((AbstractContainerScreen<?>) delegate.getScreen());
    }

    @Override
    public IModSettingsScreen getAsModSettingsScreen() {
        return new ForgeModSettingsScreen((ModSettingsScreen) delegate.getScreen());
    }
}
