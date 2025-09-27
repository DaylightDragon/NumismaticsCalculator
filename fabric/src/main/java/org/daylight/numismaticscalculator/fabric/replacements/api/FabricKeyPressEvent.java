package org.daylight.numismaticscalculator.fabric.replacements.api;

import net.minecraft.client.gui.screen.Screen;
import org.daylight.numismaticscalculator.replacements.IKeyPressEvent;
import org.lwjgl.glfw.GLFW;

public class FabricKeyPressEvent implements IKeyPressEvent {
    private Screen screen;
    private int key;
    private int scancode;
    private int modifiers;

    public FabricKeyPressEvent(Screen screen, int key, int scancode, int modifiers) {
        this.screen = screen;
        this.key = key;
        this.scancode = scancode;
    }

    @Override
    public int getKey() {
        return key;
    }

    @Override
    public int getScanCode() {
        return scancode;
    }

    @Override
    public int getAction() {
        return GLFW.GLFW_PRESS;
    }

    @Override
    public boolean isActionPress() {
        return getAction() == GLFW.GLFW_PRESS;
    }

    @Override
    public boolean isActionRepeat() {
        // Fabric API не различает repeat напрямую, всегда false
        return false;
    }

    @Override
    public boolean isBackspacePressed() {
        return key == GLFW.GLFW_KEY_BACKSPACE;
    }

    @Override
    public boolean isDeletePressed() {
        return key == GLFW.GLFW_KEY_DELETE;
    }

    @Override
    public boolean isLeftPressed() {
        return key == GLFW.GLFW_KEY_LEFT;
    }

    @Override
    public boolean isRightPressed() {
        return key == GLFW.GLFW_KEY_RIGHT;
    }

    @Override
    public String getKeyName() {
        return GLFW.glfwGetKeyName(key, scancode); // sketchy too
    }
}
