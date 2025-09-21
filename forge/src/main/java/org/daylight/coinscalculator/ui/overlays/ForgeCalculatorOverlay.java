package org.daylight.coinscalculator.ui.overlays;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import org.daylight.coinscalculator.UiState;
import org.daylight.coinscalculator.config.ConfigData;
import org.daylight.coinscalculator.replacements.*;
import org.daylight.coinscalculator.replacements.api.ForgeAbstractContainerScreen;
import org.daylight.coinscalculator.replacements.api.ForgeGuiGraphics;
import org.daylight.coinscalculator.replacements.api.ForgeScreen;
import org.daylight.coinscalculator.replacements.api.ForgeSlot;
import org.daylight.coinscalculator.ui.ForgeSelectionRenderer;
import org.daylight.coinscalculator.util.tuples.Quartet;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class ForgeCalculatorOverlay extends ICalculatorOverlay {
    @Override
    public boolean shouldRenderOnScreen(IScreen screenOriginal) {
        if(!(screenOriginal instanceof ForgeScreen forgeScreen)) throw new IllegalArgumentException();
        Screen screen = forgeScreen.getDelegate();

        return screen instanceof AbstractContainerScreen;
    }

    @Override
    public void render(@NotNull IGuiGraphics guiGraphics, float partialTick, Integer mouseX, Integer mouseY) {
        if(!(guiGraphics instanceof ForgeGuiGraphics forgeGraphics)) throw new IllegalArgumentException();

        if(!UiState.coinCalculatorOverlayActive) return;
        if (shouldRenderOnScreen(new ForgeScreen(Minecraft.getInstance().screen))) {
            AbstractContainerScreen<?> screen = (AbstractContainerScreen<?>) Minecraft.getInstance().screen;
            if (mainFloatingPanel == null) {
                init(new ForgeAbstractContainerScreen<>(screen));
            }
            ForgeSelectionRenderer.renderSelection(forgeGraphics.getDelegate(), screen);
            runPositionAnimation(ConfigData.overlayAnimationDuration.get());

            if (mainFloatingPanel != null) {
                Minecraft mc = Minecraft.getInstance();
                if (mouseX == null)
                    mouseX = (int) (mc.mouseHandler.xpos() * mc.getWindow().getGuiScaledWidth() / mc.getWindow().getWidth());
                if (mouseY == null)
                    mouseY = (int) (mc.mouseHandler.ypos() * mc.getWindow().getGuiScaledHeight() / mc.getWindow().getHeight());

//            System.out.println("Rendering actual ui");
//            RenderSystem.disableDepthTest();
                if (mainFloatingPanel != null) mainFloatingPanel.render(guiGraphics, mouseX, mouseY, partialTick);
//            RenderSystem.enableDepthTest();
            }
        }
    }

    @Override
    public void replacePositionAnimationData() {
        if(!ConfigData.overlayAnimationEnabled.get()) return;
        if(Minecraft.getInstance().screen == null || !(Minecraft.getInstance().screen instanceof AbstractContainerScreen)) return;

        Quartet<Integer, Integer, Integer, Integer> lastOverlayPosition = getOverlayBoundsForScreen(new ForgeAbstractContainerScreen<>((AbstractContainerScreen<?>) Minecraft.getInstance().screen));
//        System.out.println(lastOverlayPosition + " " + mainFloatingPanel.getY());
        if (lastOverlayPosition != null && lastOverlayPosition.getB() != mainFloatingPanel.getY()) {
            positionAnimationStartY = mainFloatingPanel.getY();
            positionAnimationEndY = lastOverlayPosition.getB();
            positionAnimationActive = true;
            positionAnimationStartTime = Calendar.getInstance().getTimeInMillis();
//            System.out.println("start");

//            System.out.println("Start: " + positionAnimationStartY);
//            System.out.println("End: " + positionAnimationEndY);
        }
    }

    @Override
    protected boolean isSlotValidForSelection(ISlot slotOrig) {
        if(!(slotOrig instanceof ForgeSlot forgeSlot)) throw new IllegalArgumentException();
        Slot slot = forgeSlot.getDelegate();
        if(slot == null) return false;
        if(!(slot.container instanceof Inventory || slot.container instanceof SimpleContainer)) return false;
        if(slot.container instanceof Inventory && slot.getContainerSlot() >= 36) return false; // ?= 36 is wierd
        return true;
    }

    @Override
    public List<ISlot> getPlayerInventorySlots(IAbstractContainerScreen<?> screenOrig) {
        if(!(screenOrig instanceof ForgeAbstractContainerScreen<?> forgeAbstractContainerScreen)) throw new IllegalArgumentException();
        AbstractContainerScreen<?> screen = forgeAbstractContainerScreen.getDelegate();

        AbstractContainerMenu menu = screen.getMenu();
        List<ISlot> slots = new ArrayList<>(Collections.nCopies(Inventory.INVENTORY_SIZE, null));
        for(Slot slot : menu.slots) {
            if(slot.container instanceof Inventory inventory) {
                slots.set(inventory.items.indexOf(slot.getItem()), new ForgeSlot(slot));
            }
        }
        return slots;
    }

    @Override
    public int getRealSlotIndex(IAbstractContainerScreen<?> screenOrig, ISlot slotOrig, Class<?> overwriteTargetClass) {
        if(!(screenOrig instanceof ForgeAbstractContainerScreen<?> forgeAbstractContainerScreen)) throw new IllegalArgumentException();
        AbstractContainerScreen<?> screen = forgeAbstractContainerScreen.getDelegate();

        if(!(slotOrig instanceof ForgeSlot forgeSlot)) throw new IllegalArgumentException();
        Slot slot = forgeSlot.getDelegate();

        Class<?> targetContainerClass = UiState.selectionContainerClass;
        if(overwriteTargetClass != null) targetContainerClass = overwriteTargetClass;
        if(targetContainerClass == null) {
            if(screen instanceof InventoryScreen || screen instanceof CreativeModeInventoryScreen) targetContainerClass = Inventory.class;
        }

        for (Slot menuSlot : screen.getMenu().slots) {
            if (menuSlot.container.getClass().equals(targetContainerClass) && menuSlot.getSlotIndex() == slot.getSlotIndex()) {
                return menuSlot.getSlotIndex();
            }
        }

        return screen.getMenu().slots.indexOf(slot);
    }

    @Override
    public ISlot getRealInventorySlot(IAbstractContainerScreen<?> screenOrig, int slotIndex) {
        if(!(screenOrig instanceof ForgeAbstractContainerScreen<?> forgeAbstractContainerScreen)) throw new IllegalArgumentException();
        AbstractContainerScreen<?> screen = forgeAbstractContainerScreen.getDelegate();

        for (Slot slot : screen.getMenu().slots) {
            if (slot.container.getClass().equals(UiState.selectionContainerClass) && slot.getSlotIndex() == slotIndex) {
                return new ForgeSlot(slot);
            }
        }

        try {
            return new ForgeSlot(screen.getMenu().getSlot(slotIndex));
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public void requestInventorySnapshot() {
        SingletonInstances.MINECRAFT_UTILS.execute(() ->
                backgroundProcessor.submit(() ->
                        processInventory(SingletonInstances.MINECRAFT_UTILS.getInventoryItems())));
    }
}
