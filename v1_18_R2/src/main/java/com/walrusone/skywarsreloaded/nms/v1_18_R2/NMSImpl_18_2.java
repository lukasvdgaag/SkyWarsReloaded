package com.walrusone.skywarsreloaded.nms.v1_18_R2;

import net.minecraft.core.BlockPosition;
import net.minecraft.server.level.WorldServer;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.EnderChest;
import org.bukkit.craftbukkit.v1_18_R2.block.CraftEnderChest;

public class NMSImpl_18_2 {

    public void playChestAction(Block block, boolean open) {
        Location location = block.getLocation();
        if (location.getWorld() == null) return;

        WorldServer world = ((org.bukkit.craftbukkit.v1_18_R2.CraftWorld) location.getWorld()).getHandle();
        BlockPosition position = new BlockPosition(location.getX(), location.getY(), location.getZ());

        if (!(block.getState() instanceof EnderChest enderChest)) return;

        world.a(position, ((CraftEnderChest) enderChest).getHandle(), 1, open ? 1 : 0);
    }

}
