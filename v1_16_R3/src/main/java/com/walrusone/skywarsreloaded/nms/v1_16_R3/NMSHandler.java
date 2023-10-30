package com.walrusone.skywarsreloaded.nms.v1_16_R3;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Collection;

public class NMSHandler extends com.walrusone.skywarsreloaded.nms.v1_16_R2.NMSHandler {

    private final Collection<org.bukkit.craftbukkit.v1_16_R3.scoreboard.CraftScoreboard> scoreboardCollection = Lists.newArrayList();

    public NMSHandler() {
        org.bukkit.craftbukkit.v1_16_R3.scoreboard.CraftScoreboardManager manager = (org.bukkit.craftbukkit.v1_16_R3.scoreboard.CraftScoreboardManager) Bukkit.getScoreboardManager();
        try {
            manager.getClass().getDeclaredField("scoreboards");
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
    }

    public boolean removeFromScoreboardCollection(Scoreboard scoreboard) {
        if (scoreboardCollection.contains((org.bukkit.craftbukkit.v1_16_R3.scoreboard.CraftScoreboard) scoreboard)) {
            scoreboardCollection.remove((org.bukkit.craftbukkit.v1_16_R3.scoreboard.CraftScoreboard) scoreboard);
            return true;
        }

        return false;
    }


    public void playChestAction(Block block, boolean open) {
        Location location = block.getLocation();
        net.minecraft.server.v1_16_R3.WorldServer world = ((org.bukkit.craftbukkit.v1_16_R3.CraftWorld) location.getWorld()).getHandle();
        net.minecraft.server.v1_16_R3.BlockPosition position = new net.minecraft.server.v1_16_R3.BlockPosition(location.getX(), location.getY(), location.getZ());
        net.minecraft.server.v1_16_R3.TileEntityEnderChest ec = (net.minecraft.server.v1_16_R3.TileEntityEnderChest) world.getTileEntity(position);
        assert (ec != null);
        world.playBlockAction(position, ec.getBlock().getBlock(), 1, open ? 1 : 0);
    }

    public void setEntityTarget(org.bukkit.entity.Entity ent, Player player) {
        net.minecraft.server.v1_16_R3.EntityCreature entity = (net.minecraft.server.v1_16_R3.EntityCreature) ((org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity) ent).getHandle();
        entity.setGoalTarget(((org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer) player).getHandle(), EntityTargetEvent.TargetReason.CLOSEST_PLAYER, true);
    }


    @Override
    public void applyTotemEffect(Player player) {

    }
}
