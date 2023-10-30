package com.walrusone.skywarsreloaded.nms.v1_11_R1;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class NMSHandler extends com.walrusone.skywarsreloaded.nms.v1_10_R1.NMSHandler {

    public void sendTitle(Player player, int fadein, int stay, int fadeout, String title, String subtitle) {
        if (subtitle != null) {
            subtitle = subtitle.replaceAll("%player%", player.getDisplayName());
            subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
        }
        if (title != null) {
            title = title.replaceAll("%player%", player.getDisplayName());
            title = ChatColor.translateAlternateColorCodes('&', title);
        }
        player.sendTitle(title, subtitle, fadein, stay, fadeout);
    }

    public void playGameSound(Location loc, String paramEnumName, String paramCategory, float paramVolume, float paramPitch, boolean paramIsCustom) {
        if (loc.getWorld() == null) return;
        SoundCategory soundCateg = paramCategory == null ? SoundCategory.MASTER : SoundCategory.valueOf(paramCategory);
        if (paramIsCustom) {
            loc.getWorld().playSound(loc, paramEnumName, soundCateg, paramVolume, paramPitch);
        } else {
            loc.getWorld().playSound(loc, Sound.valueOf(paramEnumName), soundCateg, paramVolume, paramPitch);
        }
    }

    public void playChestAction(Block block, boolean open) {
        Location location = block.getLocation();
        net.minecraft.server.v1_11_R1.WorldServer world = ((org.bukkit.craftbukkit.v1_11_R1.CraftWorld) location.getWorld()).getHandle();
        net.minecraft.server.v1_11_R1.BlockPosition position = new net.minecraft.server.v1_11_R1.BlockPosition(location.getX(), location.getY(), location.getZ());
        net.minecraft.server.v1_11_R1.TileEntityEnderChest ec = (net.minecraft.server.v1_11_R1.TileEntityEnderChest) world.getTileEntity(position);
        assert ec != null;
        world.playBlockAction(position, ec.getBlock(), 1, open ? 1 : 0);
    }

    public void setEntityTarget(Entity ent, Player player) {
        net.minecraft.server.v1_11_R1.EntityCreature entity = (net.minecraft.server.v1_11_R1.EntityCreature) ((org.bukkit.craftbukkit.v1_11_R1.entity.CraftEntity) ent).getHandle();
        entity.setGoalTarget(((org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer) player).getHandle(), null, false);
    }

    public ChunkGenerator getChunkGenerator() {
        return new ChunkGenerator() {
            @Override
            public List<BlockPopulator> getDefaultPopulators(World world) {
                return Collections.emptyList();
            }

            @Override
            public boolean canSpawn(World world, int x, int z) {
                return true;
            }

            @Override
            public byte[] generate(World world, Random random, int x, int z) {
                return new byte[32768];
            }

            @Override
            public Location getFixedSpawnLocation(World world, Random random) {
                return new Location(world, 0.0D, 64.0D, 0.0D);
            }
        };
    }

    public int getVersion() {
        return 11;
    }

    @Override
    public boolean isHoldingTotem(Player player) {
        return player.getInventory().getItemInMainHand().getType().equals(Material.TOTEM) ||
                player.getInventory().getItemInOffHand().getType().equals(Material.TOTEM);
    }

    @Override
    public void applyTotemEffect(Player player) {
        org.bukkit.inventory.PlayerInventory pInv = player.getInventory();
        org.bukkit.inventory.ItemStack mainHand = pInv.getItemInMainHand();
        org.bukkit.inventory.ItemStack offHand = pInv.getItemInOffHand();
        // Consume item
        if (mainHand.getType().equals(Material.TOTEM)) {
            pInv.setItemInMainHand(new org.bukkit.inventory.ItemStack(Material.AIR));
        } else if (offHand.getType().equals(Material.TOTEM)) {
            pInv.setItemInOffHand(new ItemStack(Material.AIR));
        }
        // On screen effect - doesn't exist without packets in this version
        net.minecraft.server.v1_11_R1.EntityPlayer ep = ((org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer) player).getHandle();
        net.minecraft.server.v1_11_R1.PacketPlayOutEntityStatus status = new net.minecraft.server.v1_11_R1.PacketPlayOutEntityStatus(ep, (byte) 35);
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
