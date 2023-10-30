package com.walrusone.skywarsreloaded.nms.v1_16_R2;

import com.google.common.collect.Lists;
import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Collection;

public class NMSHandler extends com.walrusone.skywarsreloaded.nms.v1_16_R1.NMSHandler {

    private final Collection<org.bukkit.craftbukkit.v1_16_R2.scoreboard.CraftScoreboard> scoreboardCollection = Lists.newArrayList();

    public NMSHandler() {
        org.bukkit.craftbukkit.v1_16_R2.scoreboard.CraftScoreboardManager manager = (org.bukkit.craftbukkit.v1_16_R2.scoreboard.CraftScoreboardManager) Bukkit.getScoreboardManager();
        try {
            manager.getClass().getDeclaredField("scoreboards");
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
    }

    public boolean removeFromScoreboardCollection(Scoreboard scoreboard) {
        if (scoreboardCollection.contains((org.bukkit.craftbukkit.v1_16_R2.scoreboard.CraftScoreboard) scoreboard)) {
            scoreboardCollection.remove((org.bukkit.craftbukkit.v1_16_R2.scoreboard.CraftScoreboard) scoreboard);
            return true;
        }

        return false;
    }


    public void playChestAction(Block block, boolean open) {
        Location location = block.getLocation();
        net.minecraft.server.v1_16_R2.WorldServer world = ((org.bukkit.craftbukkit.v1_16_R2.CraftWorld) location.getWorld()).getHandle();
        net.minecraft.server.v1_16_R2.BlockPosition position = new net.minecraft.server.v1_16_R2.BlockPosition(location.getX(), location.getY(), location.getZ());
        net.minecraft.server.v1_16_R2.TileEntityEnderChest ec = (net.minecraft.server.v1_16_R2.TileEntityEnderChest) world.getTileEntity(position);
        assert (ec != null);
        world.playBlockAction(position, ec.getBlock().getBlock(), 1, open ? 1 : 0);
    }

    public void setEntityTarget(org.bukkit.entity.Entity ent, Player player) {
        net.minecraft.server.v1_16_R2.EntityCreature entity = (net.minecraft.server.v1_16_R2.EntityCreature) ((org.bukkit.craftbukkit.v1_16_R2.entity.CraftEntity) ent).getHandle();
        entity.setGoalTarget(((org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer) player).getHandle(), EntityTargetEvent.TargetReason.CLOSEST_PLAYER, true);
    }

    @Override
    public void applyTotemEffect(Player player) {
        PlayerInventory pInv = player.getInventory();
        ItemStack mainHand = pInv.getItemInMainHand();
        ItemStack offHand = pInv.getItemInOffHand();
        // Consume item
        if (mainHand.getType().equals(Material.TOTEM_OF_UNDYING)) {
            pInv.setItemInMainHand(new ItemStack(Material.AIR));
        } else if (offHand.getType().equals(Material.TOTEM_OF_UNDYING)) {
            pInv.setItemInOffHand(new ItemStack(Material.AIR));
        }
        // Apply potion effects (version specific)
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 40, 0, false, true));
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
