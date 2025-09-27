package org.daylight.numismaticscalculator.replacements.api;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraftforge.client.event.InputEvent;
import org.daylight.numismaticscalculator.replacements.IKeyPressEvent;
import org.lwjgl.glfw.GLFW;

public class ForgeKeyPressEvent implements IKeyPressEvent {
    private InputEvent.Key delegate;

    public ForgeKeyPressEvent(InputEvent.Key delegate) {
        this.delegate = delegate;
    }

    public InputEvent.Key getDelegate() {
        return delegate;
    }

    @Override
    public int getKey() {
        return delegate.getKey();
    }

    @Override
    public int getScanCode() {
        return delegate.getScanCode();
    }

    @Override
    public int getAction() {
        return delegate.getAction();
    }

    @Override
    public boolean isActionPress() {
        return delegate.getAction() == InputConstants.PRESS;
    }

    @Override
    public boolean isActionRepeat() {
        return delegate.getAction() == InputConstants.REPEAT;
    }

    @Override
    public boolean isBackspacePressed() {
        return delegate.getKey() == GLFW.GLFW_KEY_BACKSPACE;
    }

    @Override
    public boolean isDeletePressed() {
        return delegate.getKey() == GLFW.GLFW_KEY_DELETE;
    }

    @Override
    public boolean isLeftPressed() {
        return delegate.getKey() == GLFW.GLFW_KEY_LEFT;
    }

    @Override
    public boolean isRightPressed() {
        return delegate.getKey() == GLFW.GLFW_KEY_RIGHT;
    }

    @Override
    public String getKeyName() {
        return GLFW.glfwGetKeyName(delegate.getKey(), delegate.getScanCode());
    }
}
