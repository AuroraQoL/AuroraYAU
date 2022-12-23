package me.dailydungeons.client.features;

import com.mojang.realmsclient.dto.WorldDownload;
import com.mojang.realmsclient.util.RealmsTasks;
import me.dailydungeons.client.DailyDungeons;
import me.dailydungeons.client.config.Config;
import me.dailydungeons.client.events.DailyDungeonsMessages;
import me.dailydungeons.client.utils.SCUTIL;
import me.dailydungeons.client.utils.inDungeon;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.model.ForgeBlockStateV1;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static me.dailydungeons.client.config.Config.*;
import static me.dailydungeons.client.config.Config.scanrange;

/**
 * Heavily inspired by code from Danker's Skyblock Mod, used under GPL 3.0 license
 * <a href="https://github.com/bowser0000/SkyblockMod/blob/master/LICENSE">...</a>
 * @author bowser0000
 */

public class DungeonMap {
    private final HashMap<BlockPos, String> rooms = new HashMap<>();
    boolean ready = true;
    boolean used = false;
    Minecraft mc = Minecraft.getMinecraft();


    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        if ((((mc.theWorld.getTotalWorldTime()) % (4L * dungeonmapscan)) == 0) && DungeonMap) {
            if (ready && !used && mc.theWorld != null && mc.thePlayer != null) {
                ready = false;
                Thread scan = new Thread(() -> scanBlocks(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ), "SCANNER");
                scan.start();

            }
        }

    }

    public void scanBlocks(Double StartX, Double StartY, Double StartZ) {


        ready = false;
        Double extrascanner = (double) Config.extrascanner;
        for (int x = (int) (StartX - extrascanner); x < StartX + extrascanner; x++) {
            for (int y = (int) (StartY - extrascanner); y < StartY + extrascanner; y++) {
                for (int z = (int) (StartZ - extrascanner); z < StartZ + extrascanner; z++) {
                    Block tempPosOfRandomBlock = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                    //  mc.thePlayer.sendChatMessage("x");
                    String dungeon = checkForStructureOnBlock(x, y, z, tempPosOfRandomBlock);
                    if (!Objects.equals(dungeon, "null")) {
                        //              mc.thePlayer.sendChatMessage(kekeke);
                        rooms.put(new BlockPos(x, y, z), dungeon);
                    }
                }
            }
        }
        ready = true;

    }

    public String checkForStructureOnBlock(int x, int y, int z, Block tempPosOfRandomBlock) {
        if ((tempPosOfRandomBlock.getBlockState().getBlock() == Blocks.stone && (mc.theWorld.getBlockState(new BlockPos(x, y, z)).getValue(BlockStone.VARIANT) == BlockStone.EnumType.STONE))) {
            //       mc.thePlayer.sendChatMessage("x");
            if (mc.theWorld.getBlockState(new BlockPos(x, y + 1, z)).getBlock() == Blocks.stonebrick) {
                if (mc.theWorld.getBlockState(new BlockPos(x, y + 2, z)).getBlock() == Blocks.stone) {
                    if (mc.theWorld.getBlockState(new BlockPos(x, y + 3, z)).getBlock() == Blocks.stone) {
                        if (mc.theWorld.getBlockState(new BlockPos(x, y + 4, z)).getBlock() == Blocks.stonebrick) {
                                //      mc.thePlayer.sendChatMessage("found struct -> " + String.format("%s %s %s", x, y, z)  );
                            DailyDungeonsMessages.sendModMessage("Found Entry-A");
                                return "Entry-A";
                            }
                        }
                    }

                }

/*                        String bcolor = null;

                        if (bcolor != null){
                            betabtr.put(new BlockPos(x, y, z), bcolor);
                        }*/


        } else if (tempPosOfRandomBlock.getBlockState().getBlock() == Blocks.tripwire_hook) {
            if (mc.theWorld.getBlockState(new BlockPos(x, y + 1, z)).getBlock() == Blocks.stone && mc.theWorld.getBlockState(new BlockPos(x, y + 1, z)).getValue(BlockStone.VARIANT) == BlockStone.EnumType.DIORITE_SMOOTH) {
                if (mc.theWorld.getBlockState(new BlockPos(x, y + 2, z)).getBlock() == Blocks.stone && mc.theWorld.getBlockState(new BlockPos(x, y + 2, z)).getValue(BlockStone.VARIANT) == BlockStone.EnumType.DIORITE) {
                    if (mc.theWorld.getBlockState(new BlockPos(x, y + 3, z)).getBlock() == Blocks.stone && mc.theWorld.getBlockState(new BlockPos(x, y + 3, z)).getValue(BlockStone.VARIANT) == BlockStone.EnumType.DIORITE) {
                        if (mc.theWorld.getBlockState(new BlockPos(x, y + 4, z)).getBlock() == Blocks.stone && mc.theWorld.getBlockState(new BlockPos(x, y + 4, z)).getValue(BlockStone.VARIANT) == BlockStone.EnumType.DIORITE) {
                            if (mc.theWorld.getBlockState(new BlockPos(x, y + 5, z)).getBlock() == Blocks.stone && mc.theWorld.getBlockState(new BlockPos(x, y + 5, z)).getValue(BlockStone.VARIANT) == BlockStone.EnumType.DIORITE) {
                                if (mc.theWorld.getBlockState(new BlockPos(x, y + 6, z)).getBlock() == Blocks.stone && mc.theWorld.getBlockState(new BlockPos(x, y + 6, z)).getValue(BlockStone.VARIANT) == BlockStone.EnumType.DIORITE_SMOOTH) {
                                    if (mc.theWorld.getBlockState(new BlockPos(x, y + 7, z)).getBlock() == Blocks.stone && mc.theWorld.getBlockState(new BlockPos(x, y + 7, z)).getValue(BlockStone.VARIANT) == BlockStone.EnumType.ANDESITE_SMOOTH) {
                                        if (mc.theWorld.getBlockState(new BlockPos(x, y + 8, z)).getBlock() == Blocks.stone_brick_stairs) {
                                            return "Spiral-Pillar-A";
                                            // return "Throne-B";
                                            //     return "Throne-C";
                                        }                                               // return "Throne-B";
                                        //     return "Throne-C";
                                    }                                           // return "Throne-B";
                                    //     return "Throne-C";
                                }                                // return "Throne-B";
                                //     return "Throne-C";
                            }
                        }
                    }
                }
            }
        }
        return "null";

    }


    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (DungeonMap) {

            used = true;

            for (java.util.Map.Entry<BlockPos, String> aka : rooms.entrySet()) {

                SCUTIL.renderBeaconText(String.format("%s - %d %d %d", aka.getValue(), aka.getKey().getX(), aka.getKey().getY(), aka.getKey().getZ()), aka.getKey(), event.partialTicks);

            }

            used = false;
        }
    }

    @SubscribeEvent
    public void onWorldChange(WorldEvent.Load event) {
        used = true;
        rooms.clear();
        used = false;
    }


}
