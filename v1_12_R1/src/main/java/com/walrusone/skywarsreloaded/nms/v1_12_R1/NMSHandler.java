package com.walrusone.skywarsreloaded.nms.v1_12_R1;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class NMSHandler extends com.walrusone.skywarsreloaded.nms.v1_11_R1.NMSHandler {

    private NMSImpl_12_1 nmsImpl;

    public void playChestAction(Block block, boolean open) {
        if (nmsImpl == null) nmsImpl = new NMSImpl_12_1();
        nmsImpl.playChestAction(block, open);
    }

    public void setEntityTarget(Entity ent, Player player) {
        if (nmsImpl == null) nmsImpl = new NMSImpl_12_1();
        nmsImpl.setEntityTarget(ent, player);
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
