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

    private NMSImpl_11_1 nmsImpl;

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
        if (nmsImpl == null) nmsImpl = new NMSImpl_11_1();
        nmsImpl.playChestAction(block, open);
    }

    public void setEntityTarget(Entity ent, Player player) {
        if (nmsImpl == null) nmsImpl = new NMSImpl_11_1();
        nmsImpl.setEntityTarget(ent, player);
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
        if (nmsImpl == null) nmsImpl = new NMSImpl_11_1();
        nmsImpl.applyTotemEffect(player);
    }
}
