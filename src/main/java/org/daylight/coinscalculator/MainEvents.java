package org.daylight.coinscalculator;

import dev.ithundxr.createnumismatics.content.backend.Coin;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.daylight.coinscalculator.ui.CoinsOverlay;
import org.daylight.coinscalculator.ui.MainWidget;
import org.daylight.coinscalculator.ui.elements.UIButton;
import org.daylight.coinscalculator.ui.elements.UIEditBox;
import org.daylight.coinscalculator.ui.elements.UIPanel;
import org.daylight.coinscalculator.ui.elements.UIText;

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
    private UIPanel panel;

    @SubscribeEvent
    public void onScreenRender(ScreenEvent.Render.Post event) {
        if (event.getScreen() instanceof InventoryScreen screen) {
//            GuiGraphics graphics = event.getGuiGraphics();
//            int panelX = screen.getGuiLeft() / 2 - PANEL_WIDTH - 5;
//            int panelY = screen.getRectangle().height() / 2 - PANEL_HEIGHT / 2;
//
//            // Рисуем фон
//            graphics.fill(panelX, panelY, panelX + PANEL_WIDTH, panelY + PANEL_HEIGHT, 0x885c5c5c);
//            graphics.drawString(screen.getMinecraft().font, "Test Text", panelX + 5, panelY + 5, 0xFFFFFF);

            if(panel != null) panel.render(event.getGuiGraphics(), event.getMouseX(), event.getMouseY(), event.getPartialTick());
        } else {
//            CoinsCalculator.LOGGER.info("Rendering method 2 on non InventoryScreen");
//            GuiGraphics graphics = event.getGuiGraphics();
//            graphics.fill(20, 20, 40, 100, 0x885c5c5c);
        }
    }

    @SubscribeEvent
    public void onScreenInit(ScreenEvent.Init.Post event) {
//        if (event.getScreen() instanceof InventoryScreen screen) {
//            int panelX = screen.getGuiLeft() / 2 - PANEL_WIDTH - 5;
//            int panelY = screen.getRectangle().height() / 2 - PANEL_HEIGHT / 2;
//
//            // Добавляем кнопку
//            event.addListener(Button.builder(Component.literal("Click"), btn -> {
//                System.out.println("Clicked!");
//            }).bounds(panelX + 5, panelY + 20, 70, 20).build());
//        }
        if (event.getScreen() instanceof InventoryScreen screen) {
            int invLeft = screen.getGuiLeft();
            Font font = screen.getMinecraft().font;

            panel = new UIPanel();
            panel.setPosition(invLeft - 120, screen.getGuiTop());
            panel.addElement(new UIText("Coins: 123", font));
            panel.addElement(new UIButton("Click", font, () -> System.out.println("Clicked!")));
            panel.addElement(new UIButton("Meow", font, () -> System.out.println("Meow!")));
            UIEditBox input = new UIEditBox(font);
            panel.addElement(input);

            panel.layout();

            event.addListener(input.getEditBox()); // добавляем реальное поле
        }
    }

    @SubscribeEvent
    public void onMouseClick(ScreenEvent.MouseButtonPressed.Pre event) {
        if (event.getScreen() instanceof InventoryScreen screen) {
            if (panel != null) {
                panel.onClick(event.getMouseX(), event.getMouseY());
                event.setCanceled(true);
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
