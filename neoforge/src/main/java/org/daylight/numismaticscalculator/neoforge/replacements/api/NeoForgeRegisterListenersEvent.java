package org.daylight.numismaticscalculator.neoforge.replacements.api;

import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.neoforged.neoforge.client.event.ScreenEvent;
import org.daylight.numismaticscalculator.neoforge.replacements.NeoForgeModSettingsScreen;
import org.daylight.numismaticscalculator.neoforge.ui.screens.NeoForgeModSettingsScreenImpl;
import org.daylight.numismaticscalculator.replacements.IAbstractContainerScreen;
import org.daylight.numismaticscalculator.replacements.IModSettingsScreen;
import org.daylight.numismaticscalculator.replacements.IRegisterListenersEvent;
import org.daylight.numismaticscalculator.replacements.IScreen;

public class NeoForgeRegisterListenersEvent implements IRegisterListenersEvent {
    private ScreenEvent.Init.Post delegate;

    public NeoForgeRegisterListenersEvent(ScreenEvent.Init.Post delegate) {
        this.delegate = delegate;
    }

    public ScreenEvent.Init.Post getDelegate() {
        return delegate;
    }


    @Override
    public boolean containsListener(Object listener) {
        if(listener instanceof NeoForgeEditBox NeoForgeEditBox) {
            return delegate.getListenersList().contains(NeoForgeEditBox.getDelegate());
        }

        if(!(listener instanceof GuiEventListener)) throw new IllegalArgumentException();
        return delegate.getListenersList().contains(listener);
    }

    @Override
    public void addListener(Object listener) {
        if(listener instanceof NeoForgeEditBox NeoForgeEditBox) {
            delegate.addListener(NeoForgeEditBox.getDelegate());
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
        return delegate.getScreen() instanceof NeoForgeModSettingsScreenImpl;
    }

    @Override
    public IScreen getScreen() {
        return new NeoForgeScreen(delegate.getScreen());
    }

    @Override
    public IAbstractContainerScreen<?> getAsAbstractContainerScreen() {
        return new NeoForgeAbstractContainerScreen((AbstractContainerScreen<?>) delegate.getScreen());
    }

    @Override
    public IModSettingsScreen getAsModSettingsScreen() {
        return new NeoForgeModSettingsScreen((NeoForgeModSettingsScreenImpl) delegate.getScreen());
    }
}
