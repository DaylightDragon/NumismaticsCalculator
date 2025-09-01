package org.daylight.coinscalculator;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.daylight.coinscalculator.ui.CoinsOverlay;
import org.daylight.coinscalculator.ui.elements.*;

import java.util.List;

public class MainEvents {
    private static final Component holdShiftHintComponent = Component.literal("Hold ").withStyle(ChatFormatting.GRAY)
            .append(Component.literal("SHIFT").withStyle(ChatFormatting.GOLD).withStyle(ChatFormatting.BOLD))
            .append(Component.literal(" to view total value").withStyle(ChatFormatting.GRAY));

    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();

        Integer value = CoinValues.ITEM_TO_VALUE.get(stack.getItem());
        if (value != null && value > 0) {
            if(!Screen.hasShiftDown()) {
                event.getToolTip().add(holdShiftHintComponent);
            } else {
                Component newLine = Component.literal("Value (Total): ").withStyle(ChatFormatting.WHITE)
                                .append(Component.literal(value * stack.getCount() + "¤").withStyle(ChatFormatting.GOLD)); //.withStyle(ChatFormatting.BOLD))
//                                .append(Component.literal("").withStyle(ChatFormatting.WHITE))

                List<Component> tooltip = event.getToolTip();
                for(int i = 0; i < tooltip.size(); i++) {
                    Component component = tooltip.get(i);
                    if(component.getString().contains("Value:")) {
                        tooltip.set(i, newLine);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onClientSetup(FMLClientSetupEvent event) {

    }

    @SubscribeEvent
    public static void onRegisterGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("coins_overlay", new CoinsOverlay());
        CoinsCalculator.LOGGER.info("Registering CoinsCalculator overlay");
    }

    private static final int PANEL_WIDTH = 80;
    private static final int PANEL_HEIGHT = 120;
    private UIPanel pagesPanel;
    private UIPanel page1VLayout;
    private UIPanel page2VLayout;

    @SubscribeEvent
    public void onScreenRender(ScreenEvent.Render.Post event) {
        if (event.getScreen() instanceof InventoryScreen screen) {
            if(pagesPanel != null) pagesPanel.render(event.getGuiGraphics(), event.getMouseX(), event.getMouseY(), event.getPartialTick());
        } else {}
    }

    private void togglePanelPages() {
        if(pagesPanel != null) {
            if(page1VLayout.isEnabled()) {
                page1VLayout.setEnabled(false);
                page2VLayout.setEnabled(true);
            } else {
                page1VLayout.setEnabled(true);
                page2VLayout.setEnabled(false);
            }
            pagesPanel.layoutElements();
        }
    }

    @SubscribeEvent
    public void onScreenInit(ScreenEvent.Init.Post event) {
        if (event.getScreen() instanceof InventoryScreen screen) {
            int invLeft = screen.getGuiLeft();
            Font font = screen.getMinecraft().font;

            pagesPanel = new UIVerticalLayout();
            pagesPanel.setId("Main Pages Panel");
            pagesPanel.setPosition(invLeft - 120, screen.getGuiTop());
            pagesPanel.setBackgroundVisible(true);

            pagesPanel.addElement(new UIButton("Change mode", font, this::togglePanelPages));

            page1VLayout = new UIVerticalLayout();
            page1VLayout.setId("Page 1");
            page1VLayout.addElement(new UIText("Total Available: 124 ¤", font));
            page1VLayout.addElement(new UiSpace(0, 5 ));
            page1VLayout.addElement(new UIText("Selected: 50 ¤", font));
            page1VLayout.addElement(new UIButton("Select", font, () -> System.out.println("Selecting...")));
            pagesPanel.addElement(page1VLayout);

            page2VLayout = new UIVerticalLayout();
            page2VLayout.setId("Page 2");
            page2VLayout.setEnabled(false);
            page2VLayout.addElement(new UIText("Convert ¤ to coins", font));

            UIEditBox conversionInput = new UIEditBox(font);
            event.addListener(conversionInput.getEditBox());
            page2VLayout.addElement(conversionInput);

            page2VLayout.addElement(new UIText("2 Cog", font));
            page2VLayout.addElement(new UIText("5 Sprocket", font));
            page2VLayout.addElement(new UIText("1 Bevel", font));
            pagesPanel.addElement(page2VLayout);

//            int prefWPage1 = pagesPanel.getPreferredWidth();
//            int prefHPage1 = pagesPanel.getPreferredHeight();
//
//            int prefWPage2 = pagesPanel.getPreferredWidth();
//            int prefHPage2 = pagesPanel.getPreferredHeight();

            // Math.max(0,

//            page1VLayout.setBounds(invLeft - prefWPage1 - 10, screen.getGuiTop(), prefWPage1, prefHPage1);
//            page2VLayout.setBounds(invLeft - prefWPage2 - 10, screen.getGuiTop(), prefWPage2, prefHPage2);

//            pagesPanel.setBounds(invLeft - prefWGlobal - 10, screen.getGuiTop(), prefWGlobal, prefHGlobal);

//            CoinsCalculator.LOGGER.info("Before layout: panel width=" + page1VLayout.width + ", height=" + page1VLayout.height);
//            page1VLayout.layoutElements();
            pagesPanel.layoutElements();
//            CoinsCalculator.LOGGER.info("After layout: panel width=" + page1VLayout.width + ", height=" + page1VLayout.height);

            int prefWGlobal = pagesPanel.getPreferredWidth();
            int prefHGlobal = pagesPanel.getPreferredHeight();
            pagesPanel.setBounds(invLeft - prefWGlobal - 10, (int) (screen.getGuiTop() + prefHGlobal * 0.4), prefWGlobal, prefHGlobal);

        }
    }

    @SubscribeEvent
    public void onMouseClick(ScreenEvent.MouseButtonPressed.Pre event) {
        if (event.getScreen() instanceof InventoryScreen screen) {
            if (pagesPanel != null) {
//                System.out.println("General click");
                pagesPanel.onClick(event.getMouseX(), event.getMouseY());
//                event.setCanceled(true);
            }
        }
    }

    // ScreenEvent.Render.Post
    @SubscribeEvent
    public void onRenderOverlay(RenderGuiOverlayEvent.Post event) {
//        Minecraft mc = Minecraft.getInstance();
//        if(mc.screen == null) return;
////        if (event.getOverlay() != VanillaGuiOverlay.ALL) return;
//
////        CoinsCalculator.LOGGER.info(mc.screen.getClass().getSimpleName());
//        if (!(mc.screen instanceof InventoryScreen || mc.screen instanceof CreativeModeInventoryScreen)) return;
////        CoinsCalculator.LOGGER.info("INVENTORY");
//
//        int width = event.getWindow().getGuiScaledWidth();
//        int height = event.getWindow().getGuiScaledHeight();
//
//        int x = width / 2 + 10;  // пример позиции справа от игрока
//        int y = height / 2 - 20; // пример позиции выше центра
//
//        MainWidget.drawWidget(mc, event, x, y);
    }
}
