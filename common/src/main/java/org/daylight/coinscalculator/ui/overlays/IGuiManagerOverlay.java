package org.daylight.coinscalculator.ui.overlays;

import org.daylight.coinscalculator.UiState;
import org.daylight.coinscalculator.replacements.*;
import org.daylight.coinscalculator.ui.elements.UIButton;
import org.daylight.coinscalculator.ui.elements.UIHorizontalLayout;
import org.daylight.coinscalculator.ui.elements.UIPanel;
import org.daylight.coinscalculator.ui.elements.UIVerticalLayout;
import org.daylight.coinscalculator.util.tuples.Quartet;
import org.jetbrains.annotations.NotNull;

public abstract class IGuiManagerOverlay implements IOverlay {
    private UIPanel rootPanel;

    public abstract boolean shouldRenderOnScreen(IScreen screen);

    protected abstract boolean shouldShowControlPanel(); // ConfigData.showControlPanel.get()

    public void render(@NotNull IGuiGraphics guiGraphics, float partialTick, Integer mouseX, Integer mouseY) {
        if(!shouldShowControlPanel()) return;
        if(shouldRenderOnScreen(SingletonInstances.MINECRAFT_UTILS.getScreen())) {
            IAbstractContainerScreen<?> screen = SingletonInstances.MINECRAFT_UTILS.getScreen().getAsAbstractContainerScreen();
            if (rootPanel == null) init(screen);
            rootPanel.render(guiGraphics, SingletonInstances.INPUT_UTILS.getMouseX(), SingletonInstances.INPUT_UTILS.getMouseY(), partialTick);
        }
    }

    public void toggleMainOverlayState() {
        if(!SingletonInstances.CALCULATOR_OVERLAY.isInitialized() || !SingletonInstances.CALCULATOR_OVERLAY.shouldRenderOnScreen(SingletonInstances.MINECRAFT_UTILS.getScreen())) return;
        UiState.coinCalculatorOverlayActive = !UiState.coinCalculatorOverlayActive;
        SingletonInstances.CALCULATOR_OVERLAY.setOverlayActive(UiState.coinCalculatorOverlayActive); // bad way kinda
        SingletonInstances.CALCULATOR_OVERLAY.updateLayout();
    }

    protected abstract void onOpenSettingsBtnClicked();

    public void init(IAbstractContainerScreen<?> screen) {
//        System.out.println("INIT GuiManagerOverlay");
        IFont font = SingletonInstances.MINECRAFT_UTILS.getMinecraftFont();

        rootPanel = new UIVerticalLayout();

        UIHorizontalLayout horizontalLayout = new UIHorizontalLayout();
        horizontalLayout.setPadding(0);

        UIButton toggleUiBtn = new UIButton("", font, 1.0f, this::toggleMainOverlayState);
        toggleUiBtn.setIcon(SingletonInstances.MOD_RESOURCES.getResourceLocation(IModResources.Type.CURRENCY_ICON), 12, 12);
        toggleUiBtn.setBgColorNormal(0x8878594c);
        toggleUiBtn.setPadding(2, 2);
        horizontalLayout.addElement(toggleUiBtn);

        UIButton openSettingsBtn = new UIButton("", font, 1.0f, this::onOpenSettingsBtnClicked);
        openSettingsBtn.setIcon(SingletonInstances.MOD_RESOURCES.getResourceLocation(IModResources.Type.GEAR_ICON), 12, 12);
        openSettingsBtn.setBgColorNormal(0x8878594c);
        openSettingsBtn.setPadding(2, 2);
        horizontalLayout.addElement(openSettingsBtn);

        rootPanel.addElement(horizontalLayout);

        rootPanel.layoutElements();
        rootPanel.updateInternalValues();

        Quartet<Integer, Integer, Integer, Integer> bounds = getOverlayBoundsForScreen(screen);
        rootPanel.setBounds(bounds.getA(), bounds.getB(), bounds.getC(), bounds.getD());
    }

    public Quartet<Integer, Integer, Integer, Integer> getOverlayBoundsForScreen(IAbstractContainerScreen<?> screen) {
        int prefWGlobal = rootPanel.getPreferredWidth();
        int prefHGlobal = rootPanel.getPreferredHeight();

        Quartet<Integer, Integer, Integer, Integer> mainOverlayBounds = SingletonInstances.CALCULATOR_OVERLAY.getLastOverlayPosition();
//        System.out.println("Main overlay bounds: " + mainOverlayBounds);
        int y = mainOverlayBounds.getB() + mainOverlayBounds.getD() + 10;

        if(y > screen.height() - 5 - prefHGlobal) {
            y = screen.height() - 5 - prefHGlobal;
        }

        return new Quartet<>(5, y, prefWGlobal, prefHGlobal); // (int) (screen.getGuiTop() + screen.getYSize() * 0.8)
    }

    public void relinkListeners(IRegisterListenersEvent event) {
        if(event.isAbstractContainerScreen()) {
            if (rootPanel == null) init(event.getAsAbstractContainerScreen());
        }
        if(rootPanel != null) rootPanel.relinkListeners(event);
    }

    @Override
    public boolean onMouseClick(double mouseX, double mouseY, int button, IScreen screenOrig) {
        if(!shouldShowControlPanel()) return false;
        boolean result = false;
        if (shouldRenderOnScreen(screenOrig)) {
            if (rootPanel != null) rootPanel.onClick(mouseX, mouseY);
        }
        return result;
    }

    @Override
    public boolean onMouseDrag(double mouseX, double mouseY, int button, IScreen screenOrig) {
        return false;
    }

    @Override
    public void onMouseRelease(double mouseX, double mouseY, int button, IScreen screenOrig) {}

    @Override
    public void onKeyPressed(IKeyPressEvent event) {}

    public void updateOverlayPosition(IScreen screen) {
        if(rootPanel == null) return;
        if(screen instanceof IAbstractContainerScreen<?> abstractContainerScreen) {
            Quartet<Integer, Integer, Integer, Integer> bounds = getOverlayBoundsForScreen(abstractContainerScreen);
            rootPanel.setBounds(bounds.getA(), bounds.getB(), bounds.getC(), bounds.getD());
//            rootPanel.layoutElements();
//            System.out.println("set " + bounds);
        }
    }
}
