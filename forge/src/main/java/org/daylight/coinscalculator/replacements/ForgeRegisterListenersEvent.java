package org.daylight.coinscalculator.replacements;

import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ScreenEvent;

import java.util.ArrayList;
import java.util.List;

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
        if(!(listener instanceof GuiEventListener)) throw new IllegalArgumentException();
        return delegate.getListenersList().contains(listener);
    }

    @Override
    public void addListener(Object listener) {

    }
}
