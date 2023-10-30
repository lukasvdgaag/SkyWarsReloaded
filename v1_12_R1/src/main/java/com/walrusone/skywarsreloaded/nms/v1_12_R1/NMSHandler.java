package com.walrusone.skywarsreloaded.nms.v1_12_R1;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class NMSHandler extends com.walrusone.skywarsreloaded.nms.v1_11_R1.NMSHandler {

    public String getItemName(ItemStack item) {
        return org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack.asNMSCopy(item).getName();
    }

    public void playChestAction(Block block, boolean open) {
        Location location = block.getLocation();
        net.minecraft.server.v1_12_R1.WorldServer world = ((org.bukkit.craftbukkit.v1_12_R1.CraftWorld) location.getWorld()).getHandle();
        net.minecraft.server.v1_12_R1.BlockPosition position = new net.minecraft.server.v1_12_R1.BlockPosition(location.getX(), location.getY(), location.getZ());
        net.minecraft.server.v1_12_R1.TileEntityEnderChest ec = (net.minecraft.server.v1_12_R1.TileEntityEnderChest) world.getTileEntity(position);
        if (ec != null) {
            world.playBlockAction(position, ec.getBlock(), 1, open ? 1 : 0);
        }
    }

    public void setEntityTarget(Entity ent, Player player) {
        net.minecraft.server.v1_12_R1.EntityCreature entity = (net.minecraft.server.v1_12_R1.EntityCreature) ((org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity) ent).getHandle();
        entity.setGoalTarget(((org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer) player).getHandle(), EntityTargetEvent.TargetReason.CLOSEST_PLAYER, true);
    }

    public ChunkGenerator getChunkGenerator() {
        return new ChunkGenerator() {
            public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid chunkGererator) {
                ChunkData chunkData = createChunkData(world);
                for (int i = 0; i < 16; i++) {
                    for (int j = 0; j < 16; j++) {
                        chunkGererator.setBiome(i, j, Biome.VOID);
                    }
                }
                return chunkData;
            }
        };
    }

    public int getVersion() {
        return 12;
    }

    public Block getHitBlock(ProjectileHitEvent event) {
        return event.getHitBlock();
    }

    @Override
    public void applyTotemEffect(Player player) {
        PlayerInventory pInv = player.getInventory();
        ItemStack mainHand = pInv.getItemInMainHand();
        ItemStack offHand = pInv.getItemInOffHand();
        // Consume item
        if (mainHand.getType().equals(Material.TOTEM)) {
            pInv.setItemInMainHand(new ItemStack(Material.AIR));
        } else if (offHand.getType().equals(Material.TOTEM)) {
            pInv.setItemInOffHand(new ItemStack(Material.AIR));
        }
        // On screen effect
        player.playEffect(EntityEffect.TOTEM_RESURRECT);
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
