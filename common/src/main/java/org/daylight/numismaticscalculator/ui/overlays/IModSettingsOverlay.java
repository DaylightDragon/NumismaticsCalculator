package org.daylight.numismaticscalculator.ui.overlays;

import org.daylight.numismaticscalculator.replacements.*;
import org.daylight.numismaticscalculator.ui.elements.*;
import org.daylight.numismaticscalculator.util.tuples.Quartet;
import org.jetbrains.annotations.NotNull;

public abstract class IModSettingsOverlay implements IOverlay {
    private UIPanel rootPanel;

    public boolean shouldRenderOnScreen(IScreen screen) {
        return (screen.isModSettingsScreen());
    }

    public void render(@NotNull IGuiGraphics guiGraphics, float partialTick, Integer mouseX, Integer mouseY) {
        if(shouldRenderOnScreen(SingletonInstances.MINECRAFT_UTILS.getScreen())) {
            IModSettingsScreen screen = SingletonInstances.MINECRAFT_UTILS.getScreen().getAsModSettingsScreen();
            if (rootPanel == null) init(screen);
            rootPanel.render(guiGraphics, SingletonInstances.INPUT_UTILS.getMouseX(), SingletonInstances.INPUT_UTILS.getMouseY(), partialTick);
        }
    }

    private UIPanel createOptionRow(String name, IConfigValue<?> value) {
        IFont font = SingletonInstances.MINECRAFT_UTILS.getMinecraftFont();
        UIHorizontalLayout optionRow = new UIHorizontalLayout();
        optionRow.setPadding(5);
        optionRow.setMinWidth(280);
        if(SingletonInstances.MINECRAFT_UTILS.getScreen() != null) optionRow.setMinWidth((int) (SingletonInstances.MINECRAFT_UTILS.getScreen().width() * 0.65f));
        optionRow.setMainDistribution(MainDistribution.SPACE_BETWEEN);
        optionRow.setCrossAlignment(CrossAlignment.CENTER);
        optionRow.setBackgroundVisible(true);
        optionRow.setBackgroundColor(0xAA36302c);
        optionRow.setOutlineWidth(1);
        optionRow.setOutlineColor(0xBBbf7947);
        optionRow.addElement(new UIText(name, font, 1.0f, 0xFFFFFF));
        if(value instanceof IBooleanConfigValue booleanValue) {
            UIButton btn = new UIButton(booleanValue.get() ? "True" : "False", font, 1.0f, () -> {}) {
                @Override
                public boolean onClick(double mouseX, double mouseY) {
                    boolean result = super.onClick(mouseX, mouseY);
                    if(!result) return false;
                    booleanValue.set(!booleanValue.get());
                    setLabel(booleanValue.get() ? "True" : "False");
                    setTextColor(booleanValue.get() ? 0xb9ff8a : 0xff8a8a);
//                    System.out.println(width);
//                    System.out.println("CLICK");
//                        booleanValue.save();
//                    System.out.println(optionRow.getWidth());
                    return true;
                }
            };
            btn.setTextColor(booleanValue.get() ? 0xb9ff8a : 0xff6e6e);
            btn.setImagePosition(UIButton.ImagePosition.IMAGE_RIGHT_KINDA);
            btn.setSpacing(0);
            btn.setMinWidth(37);
            optionRow.addElement(btn);
        } else if(value instanceof IIntConfigValue intValue) {
            UIEditBox editBox = new UIEditBox(font, 40, 37);
            editBox.setText(String.valueOf(intValue.get()));
            editBox.setOnValueChange(text -> {
                if(text.isBlank()) text = "0";
                int userValue;
                try {
                    userValue = Integer.parseInt(text);
                } catch (NumberFormatException e) {
                    return;
                }
                intValue.set(userValue);
            });
            optionRow.addElement(editBox);
        }
//        optionRow.setBounds(optionRow.getX(), optionRow.getY(), optionRow.getWidth(), optionRow.getHeight());
        return optionRow;
    }

    protected abstract IBooleanConfigValue getConfigRequireShiftForTotalTooltip(); // ConfigData.requireShiftForTotalTooltip
    protected abstract IBooleanConfigValue getConfigShowControlPanel(); // ConfigData.showControlPanel
    protected abstract IBooleanConfigValue getConfigOverlayAnimationEnabled(); // ConfigData.overlayAnimationEnabled
    protected abstract IIntConfigValue getConfigOverlayAnimationDuration(); // ConfigData.overlayAnimationDuration

    public void init(IModSettingsScreen screen) {
        if(rootPanel != null) return;
        rootPanel = new UIVerticalLayout();

        UIVerticalLayout verticalLayout = new UIVerticalLayout();
        verticalLayout.setPadding(8);
        verticalLayout.setBackgroundVisible(true);
        verticalLayout.setBackgroundColor(0x884a423d);
        verticalLayout.setOutlineColor(0xBBab8974);
        verticalLayout.setOutlineWidth(1);

        verticalLayout.addElement(createOptionRow("Require \"Shift\" For Total Value Tooltip", getConfigRequireShiftForTotalTooltip()));
        verticalLayout.addElement(createOptionRow("Show Control Panel Buttons", getConfigShowControlPanel()));
        verticalLayout.addElement(createOptionRow("Overlay Animation Enabled", getConfigOverlayAnimationEnabled()));
        verticalLayout.addElement(createOptionRow("Overlay Animation Duration", getConfigOverlayAnimationDuration()));

        rootPanel.addElement(verticalLayout);

        rootPanel.layoutElements();
        rootPanel.updateInternalValues();

        Quartet<Integer, Integer, Integer, Integer> bounds = getOverlayBoundsForScreen(screen);
        rootPanel.setBounds(bounds.getA(), bounds.getB(), bounds.getC(), bounds.getD());
    }

    public Quartet<Integer, Integer, Integer, Integer> getOverlayBoundsForScreen(IModSettingsScreen screen) {
        int prefWGlobal = rootPanel.getPreferredWidth();
        int prefHGlobal = rootPanel.getPreferredHeight();

        return new Quartet<>((screen.width() - prefWGlobal) / 2, (screen.height() - prefHGlobal) / 2, prefWGlobal, prefHGlobal);
    }

    public void relinkListeners(IRegisterListenersEvent event) {
        if(event.isModSettingsScreen()) {
            if (rootPanel == null) init(event.getAsModSettingsScreen());
        }
        if(rootPanel != null) rootPanel.relinkListeners(event);
    }

    @Override
    public boolean onMouseClick(double mouseX, double mouseY, int button, IScreen screenOrig) {
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
    public void onKeyPressed(IKeyPressEvent event) {
//        System.out.println("IModSettingsScreen.onKeyPressed: " + event.getKey());
        if(rootPanel != null) rootPanel.keyPressed(event);
    }

    public void updateOverlayPosition(IScreen screen) {
        if(rootPanel == null) return;
        if(screen.isModSettingsScreen()) {
            Quartet<Integer, Integer, Integer, Integer> bounds = getOverlayBoundsForScreen(screen.getAsModSettingsScreen());
            rootPanel.setBounds(bounds.getA(), bounds.getB(), bounds.getC(), bounds.getD());
        }
    }
}
