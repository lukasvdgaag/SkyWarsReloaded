package com.walrusone.skywarsreloaded.nms.v1_11_R1;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import net.minecraft.server.v1_11_R1.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_11_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class NMSImpl_11_1 {

    public void playChestAction(Block block, boolean open) {
        Location location = block.getLocation();
        WorldServer world = ((CraftWorld) location.getWorld()).getHandle();
        BlockPosition position = new BlockPosition(location.getX(), location.getY(), location.getZ());
        TileEntityEnderChest ec = (TileEntityEnderChest) world.getTileEntity(position);
        assert ec != null;
        world.playBlockAction(position, ec.getBlock(), 1, open ? 1 : 0);
    }

    public void setEntityTarget(Entity ent, Player player) {
        EntityCreature entity = (EntityCreature) ((CraftEntity) ent).getHandle();
        entity.setGoalTarget(((CraftPlayer) player).getHandle(), null, false);
    }

    public void applyTotemEffect(Player player) {
        org.bukkit.inventory.PlayerInventory pInv = player.getInventory();
        ItemStack mainHand = pInv.getItemInMainHand();
        ItemStack offHand = pInv.getItemInOffHand();
        // Consume item
        if (mainHand.getType().equals(org.bukkit.Material.TOTEM)) {
            pInv.setItemInMainHand(new ItemStack(org.bukkit.Material.AIR));
        } else if (offHand.getType().equals(org.bukkit.Material.TOTEM)) {
            pInv.setItemInOffHand(new ItemStack(Material.AIR));
        }
        // On screen effect - doesn't exist without packets in this version
        EntityPlayer ep = ((CraftPlayer) player).getHandle();
        PacketPlayOutEntityStatus status = new PacketPlayOutEntityStatus(ep, (byte) 35);
        ep.playerConnection.sendPacket(status);
        // Particles
        new BukkitRunnable() {
            byte count = 0;

            @Override
            public void run() {
                if (count > 30) {
                    this.cancel();
                    return;
                } else {
                    count++;
                }
                player.getWorld().spawnParticle(Particle.TOTEM, player.getLocation(), 10, 0.1, 0.1, 0.1, 0.5);
            }
        }.runTaskTimer(SkyWarsReloaded.get(), 0, 1);
    }
}
