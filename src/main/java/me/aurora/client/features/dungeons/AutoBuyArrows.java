package me.aurora.client.features.dungeons;

import me.aurora.client.Aurora;
import me.aurora.client.config.Config;
import me.aurora.client.features.Module;
import me.aurora.client.utils.BindUtils;
import me.aurora.client.utils.InventoryUtils;
import me.aurora.client.utils.MessageUtils;
import me.aurora.client.utils.conditions.ConditionUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import scala.reflect.internal.Names;

import java.util.Set;


public class AutoBuyArrows {
    private final Minecraft mc = Aurora.mc;
    public static boolean buying = false;

    public static boolean inBazaar = false;
    public static boolean inSlimeMenu = false;
    public static boolean inSlimeBuyingMenu = false;
    private int ticks;

    @SubscribeEvent
    public void onKeyPress(InputEvent.KeyInputEvent event) {
        if(BindUtils.isBindPressed("AutoBuyArrows")) {
            buying = true;
            mc.thePlayer.sendChatMessage("/bz");
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {

        if (buying && ConditionUtils.inSkyblock()) {

            if (++ticks < 200) return;
            ticks = 0;

            int[] shit = {18, 20, 10, 10, 14};

            if(inBazaar) {
                InventoryUtils.clickSlot(shit[0], 0, 1);
                InventoryUtils.clickSlot(shit[1], 0, 1);
            }
            if(inSlimeMenu) {
                InventoryUtils.clickSlot(shit[2], 0, 1);
            }
            if(inSlimeBuyingMenu) {
                InventoryUtils.clickSlot(shit[3], 0, 1);
                InventoryUtils.clickSlot(shit[4], 0, 1);
            }
        }
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText();
        if(message.contains("This server is restarting") || message.contains("This menu has been throttled") || message.contains("volatile market")) {
            new Thread(()-> {
                try {
                    buying = false;
                    mc.thePlayer.closeScreen();
                    MessageUtils.sendClientMessage("Detected interruption in buying, sending to island and trying again..");
                    Thread.sleep(500);
                    mc.thePlayer.sendChatMessage("/is");
                    Thread.sleep(5500);
                    buying = true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

        }
        if(message.contains("afford this!") || message.contains("occurred in your connection")) {
            MessageUtils.sendClientMessage("Ran out of money! Stopping..");
            mc.thePlayer.closeScreen();
            buying = false;
        }
    }
    
    public void buyInventory(int slotId1, int slotId2, int slotId3, int mouseButtonClicked, int mode) {

                for(int i = 0; i < Config.auto_arrow_inventories; i++) {
                    InventoryUtils.clickSlot(slotId1, mouseButtonClicked, mode);
                    InventoryUtils.clickSlot(slotId2, mouseButtonClicked, mode);
                    InventoryUtils.clickSlot(slotId3, mouseButtonClicked, mode);
                }
                if(Config.auto_arrow_inventories == 1) {
                    InventoryUtils.clickSlot(slotId1, mouseButtonClicked, mode);
                    InventoryUtils.clickSlot(slotId2, mouseButtonClicked, mode);
                    InventoryUtils.clickSlot(slotId3, mouseButtonClicked, mode);
                }
    }

    public void onBackgroundRender(GuiScreenEvent.BackgroundDrawnEvent event) {
        String chestName = InventoryUtils.getGuiName(event.gui);
        inBazaar = chestName.contains("Bazaar");
        inSlimeMenu = chestName.contains("Slime Drops");
        inSlimeBuyingMenu = chestName.contains("Slimeball");
    }
}
