package com.walrusone.skywarsreloaded.nms.v1_16_R1;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Collection;

public class NMSHandler extends com.walrusone.skywarsreloaded.nms.v1_15_R1.NMSHandler {
    private final Collection<org.bukkit.craftbukkit.v1_16_R1.scoreboard.CraftScoreboard> scoreboardCollection = Lists.newArrayList();

    public NMSHandler() {
        org.bukkit.craftbukkit.v1_16_R1.scoreboard.CraftScoreboardManager manager = (org.bukkit.craftbukkit.v1_16_R1.scoreboard.CraftScoreboardManager) Bukkit.getScoreboardManager();
        try {
            manager.getClass().getDeclaredField("scoreboards");
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
    }

    public boolean removeFromScoreboardCollection(Scoreboard scoreboard) {
        if (scoreboardCollection.contains((org.bukkit.craftbukkit.v1_16_R1.scoreboard.CraftScoreboard) scoreboard)) {
            scoreboardCollection.remove((org.bukkit.craftbukkit.v1_16_R1.scoreboard.CraftScoreboard) scoreboard);
            return true;
        }

        return false;
    }

    public void playChestAction(Block block, boolean open) {
        Location location = block.getLocation();
        net.minecraft.server.v1_16_R1.WorldServer world = ((org.bukkit.craftbukkit.v1_16_R1.CraftWorld) location.getWorld()).getHandle();
        net.minecraft.server.v1_16_R1.BlockPosition position = new net.minecraft.server.v1_16_R1.BlockPosition(location.getX(), location.getY(), location.getZ());
        net.minecraft.server.v1_16_R1.TileEntityEnderChest ec = (net.minecraft.server.v1_16_R1.TileEntityEnderChest) world.getTileEntity(position);
        assert (ec != null);
        world.playBlockAction(position, ec.getBlock().getBlock(), 1, open ? 1 : 0);
    }

    public void setEntityTarget(org.bukkit.entity.Entity ent, Player player) {
        net.minecraft.server.v1_16_R1.EntityCreature entity = (net.minecraft.server.v1_16_R1.EntityCreature) ((org.bukkit.craftbukkit.v1_16_R1.entity.CraftEntity) ent).getHandle();
        entity.setGoalTarget(((org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer) player).getHandle(), EntityTargetEvent.TargetReason.CLOSEST_PLAYER, true);
    }


    public int getVersion() {
        return 16;
    }


    protected String getColorFromByte(byte color) {
        switch (color) {
            case 0:
                return "WHITE";
            case 1:
                return "ORANGE";
            case 2:
                return "MAGENTA";
            case 3:
                return "LIGHT_BLUE";
            case 4:
                return "YELLOW";
            case 5:
                return "LIME";
            case 6:
                return "PINK";
            case 7:
                return "GRAY";
            case 8:
                return "LIGHT_GRAY";
            case 9:
                return "CYAN";
            case 10:
                return "PURPLE";
            case 11:
                return "BLUE";
            case 12:
                return "BROWN";
            case 13:
                return "GREEN";
            case 14:
                return "RED";
            case 16:
                return "BLACK";
        }
        return "WHITE";
    }


}
