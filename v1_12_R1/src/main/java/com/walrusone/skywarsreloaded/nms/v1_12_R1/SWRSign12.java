package com.walrusone.skywarsreloaded.nms.v1_12_R1;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.enums.MatchState;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.material.Directional;
import org.bukkit.material.MaterialData;

public class SWRSign12 implements com.walrusone.skywarsreloaded.game.signs.SWRSign {

    private String gameName;
    private org.bukkit.Location location;

    public SWRSign12(String name, Location location) {
        this.gameName = name;
        this.location = location;
    }

    @Override
    public void setMaterial(GameMap gMap, Block attachedBlock) {
        if (attachedBlock == null) return;
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

    @Override
    public void updateBlock(Block block, String item) {
        if (block == null) return;
        block.setType(SkyWarsReloaded.getIM().getItem(item).getType());
        if ((SkyWarsReloaded.getNMS().getVersion() < 13) && (
                (SkyWarsReloaded.getIM().getItem(item).getType().equals(Material.valueOf("WOOL"))) || (SkyWarsReloaded.getIM().getItem(item).getType().equals(Material.valueOf("STAINED_GLASS"))) || (SkyWarsReloaded.getIM().getItem(item).getType().equals(Material.valueOf("STAINED_CLAY"))))) {
            SkyWarsReloaded.getNMS().setBlockWithColor(block.getWorld(), block.getX(), block.getY(), block.getZ(), SkyWarsReloaded.getIM().getItem(item).getType(), SkyWarsReloaded.getIM().getItem(item).getData().getData());
        }
    }

    @Override
    public Block getAttachedBlock(Block b) {
        final MaterialData m = b.getState().getData();
        BlockFace face = BlockFace.DOWN;
        if (m instanceof Directional) {
            face = ((Directional) m).getFacing().getOppositeFace();
        }
        return b.getRelative(face);
    }

    @Override
    public void update() {
        GameMap gMap = GameMap.getMap(gameName);
        org.bukkit.Location loc = location;

        if (loc.getBlock() == null) return;

        if (loc.getBlock().getType().name().contains("SIGN") || loc.add(0, 1, 0).getBlock().getType().name().contains("SIGN")) {
            Block attachedBlock;
            Sign sign = (Sign) loc.getBlock().getState();
            if (sign == null) return;

            if (loc.getBlock().getType().name().contains("WALL")) {
                attachedBlock = getAttachedBlock(loc.getBlock());
            } else {
                attachedBlock = loc.add(0, -1, 0).getBlock();
            }

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
                } else {
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

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public String getName() {
        return gameName;
    }

}
