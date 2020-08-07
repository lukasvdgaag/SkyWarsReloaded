package com.walrusone.skywarsreloaded.game;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.enums.MatchState;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.material.Directional;
import org.bukkit.material.MaterialData;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;

public class SWRSign {
    private String gameName;
    private org.bukkit.Location location;

    SWRSign(String name, org.bukkit.Location loc) {
        gameName = name;
        location = loc;
    }

    private static void setMaterial(GameMap gMap, Block attachedBlock) {
        attachedBlock.getWorld().loadChunk(attachedBlock.getChunk());
        if (gMap == null) {
            updateBlock(attachedBlock, "blockoffline");
        } else if (gMap.getMatchState().equals(MatchState.WAITINGSTART) || gMap.getMatchState().equals(MatchState.WAITINGLOBBY)) {
            updateBlock(attachedBlock, "blockwaiting");
        } else if (gMap.getMatchState().equals(MatchState.PLAYING)) {
            updateBlock(attachedBlock, "blockplaying");
        } else if (gMap.getMatchState().equals(MatchState.ENDING)) {
            updateBlock(attachedBlock, "blockending");
        } else if (gMap.getMatchState().equals(MatchState.OFFLINE)) {
            updateBlock(attachedBlock, "blockoffline");
        }
    }

    private static void updateBlock(Block block, String item) {
        block.setType(SkyWarsReloaded.getIM().getItem(item).getType());
        if ((SkyWarsReloaded.getNMS().getVersion() < 13) && (
                (SkyWarsReloaded.getIM().getItem(item).getType().equals(Material.valueOf("WOOL"))) || (SkyWarsReloaded.getIM().getItem(item).getType().equals(Material.valueOf("STAINED_GLASS"))) || (SkyWarsReloaded.getIM().getItem(item).getType().equals(Material.valueOf("STAINED_CLAY"))))) {
            SkyWarsReloaded.getNMS().setBlockWithColor(block.getWorld(), block.getX(), block.getY(), block.getZ(), SkyWarsReloaded.getIM().getItem(item).getType(), SkyWarsReloaded.getIM().getItem(item).getData().getData());
        }
    }

    public Block getAttachedBlock(final Block b) {
        final MaterialData m = b.getState().getData();
        BlockFace face = BlockFace.DOWN;
        if (m instanceof Directional) {
            face = ((Directional) m).getFacing().getOppositeFace();
        }
        return b.getRelative(face);
    }

    public void update() {
        GameMap gMap = GameMap.getMap(gameName);
        org.bukkit.Location loc = location;

        if (loc.getBlock().getType().name().contains("SIGN") || loc.add(0,1,0).getBlock().getType().name().contains("SIGN")) {
            Block attachedBlock;
            Sign sign = (Sign) loc.getBlock().getState();
            /*if (!loc.getBlock().getType().name().contains("WALL")) {
                sign =  (Sign) loc.add(0,1,0).getBlock().getState();
            }*/
            //attachedBlock = getAttachedBlock(loc.getBlock());

            if (loc.getBlock().getType().name().contains("WALL")) {
                if (Material.getMaterial("RED_WOOL") != null) {
                    try {
                        Class cls = Class.forName("org.bukkit.block.Block");
                        Method method = cls.getMethod("getBlockData");
                        BlockData blockdata = (BlockData) method.invoke(loc.getBlock());
                        attachedBlock = loc.getBlock().getRelative(((WallSign) blockdata).getFacing().getOppositeFace());
                    } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                        attachedBlock = loc.add(0, -1, 0).getBlock();
                    }
                } else {
                    attachedBlock = getAttachedBlock(loc.getBlock());
                }
            } else {
                attachedBlock = loc.add(0, -1, 0).getBlock();
                //attachedBlock = loc.getBlock();
            }

            /*if (sign.getType().name().contains("WALL")) {
                WallSign
                BlockFace bf = sign1.getFacing().getOppositeFace();
                attachedBlock = sign.getBlock().getRelative(bf);
            }
            else {
                org.bukkit.material.Sign sign1 = (org.bukkit.material.Sign) sign.getBlock().getState();
                BlockFace bf = sign1.getFacing().getOppositeFace();
                attachedBlock = sign.getBlock().getRelative(bf);
            }*/
            //Block attachedBlock = sign.getBlock().getRelative(sign.getBlock().getFace(sign.getBlock()).getOppositeFace());

            setMaterial(gMap, attachedBlock);
            String state = "";
            if ((gMap == null) || (gMap.getMatchState().equals(MatchState.OFFLINE))) {
                state = new Messaging.MessageFormatter().format("signs.offline");
            } else if (gMap.getMatchState().equals(MatchState.WAITINGSTART) || gMap.getMatchState().equals(MatchState.WAITINGLOBBY)) {
                state = new Messaging.MessageFormatter().format("signs.joinable");
            } else if (gMap.getMatchState().equals(MatchState.PLAYING)) {
                state = new Messaging.MessageFormatter().format("signs.playing");
            } else if (gMap.getMatchState().equals(MatchState.ENDING)) {
                state = new Messaging.MessageFormatter().format("signs.ending");
            }
            sign.getBlock().getChunk().load();
            if (gMap != null) {
                String team = "";
                if (gMap.getTeamSize() > 1) {
                    team = "team";
                }
                
                int playercount = 0;
                if (gMap.getMatchState() == MatchState.WAITINGLOBBY) {
                    playercount = gMap.getWaitingPlayers().size();
                }
                else {
                    playercount = gMap.getPlayerCount();
                }
                
                
                sign.setLine(0, new Messaging.MessageFormatter().setVariable("matchstate", state)
                        .setVariable("mapname", gMap.getDisplayName().toUpperCase())
                        .setVariable("playercount", "" + playercount)
                        .setVariable("maxplayers", "" + gMap.getMaxPlayers())
                        .setVariable("teamsize", "" + gMap.getTeamSize()).format("signs.line1" + team));
                sign.setLine(1, new Messaging.MessageFormatter().setVariable("matchstate", state)
                        .setVariable("mapname", gMap.getDisplayName().toUpperCase())
                        .setVariable("playercount", "" + playercount)
                        .setVariable("maxplayers", "" + gMap.getMaxPlayers())
                        .setVariable("teamsize", "" + gMap.getTeamSize()).format("signs.line2" + team));
                sign.setLine(2, new Messaging.MessageFormatter().setVariable("matchstate", state)
                        .setVariable("mapname", gMap.getDisplayName().toUpperCase())
                        .setVariable("playercount", "" + playercount)
                        .setVariable("maxplayers", "" + gMap.getMaxPlayers())
                        .setVariable("teamsize", "" + gMap.getTeamSize()).format("signs.line3" + team));
                sign.setLine(3, new Messaging.MessageFormatter().setVariable("matchstate", state)
                        .setVariable("mapname", gMap.getDisplayName().toUpperCase())
                        .setVariable("playercount", "" + playercount)
                        .setVariable("maxplayers", "" + gMap.getMaxPlayers())
                        .setVariable("teamsize", "" + gMap.getTeamSize()).format("signs.line4" + team));
            }
            sign.update();
        }
    }

    public org.bukkit.Location getLocation() {
        return location;
    }

    public String getName() {
        return gameName;
    }
}
