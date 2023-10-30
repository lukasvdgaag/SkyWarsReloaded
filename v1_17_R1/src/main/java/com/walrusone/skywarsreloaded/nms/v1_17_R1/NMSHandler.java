package com.walrusone.skywarsreloaded.nms.v1_17_R1;

import com.google.common.collect.Lists;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.minecraft.core.BlockPosition;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.level.block.entity.TileEntityEnderChest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Random;

public class NMSHandler extends com.walrusone.skywarsreloaded.nms.v1_16_R3.NMSHandler {

    private final Collection<org.bukkit.craftbukkit.v1_17_R1.scoreboard.CraftScoreboard> scoreboardCollection = Lists.newArrayList();

    public NMSHandler() {
        org.bukkit.craftbukkit.v1_17_R1.scoreboard.CraftScoreboardManager manager = (org.bukkit.craftbukkit.v1_17_R1.scoreboard.CraftScoreboardManager) Bukkit.getScoreboardManager();
        try {
            manager.getClass().getDeclaredField("scoreboards");
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
    }

    public boolean removeFromScoreboardCollection(Scoreboard scoreboard) {
        if (scoreboardCollection.contains((org.bukkit.craftbukkit.v1_17_R1.scoreboard.CraftScoreboard) scoreboard)) {
            scoreboardCollection.remove((org.bukkit.craftbukkit.v1_17_R1.scoreboard.CraftScoreboard) scoreboard);
            return true;
        }

        return false;
    }

    public void sendActionBar(Player player, String msg) {
        msg = ChatColor.translateAlternateColorCodes('&', msg);
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(msg).create());
    }

    public String getItemName(ItemStack item) {
        return item.getItemMeta() == null ? null : item.getItemMeta().getDisplayName();
    }

    public void setMaxHealth(Player player, int health) {
        AttributeInstance attr = player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH);
        assert attr != null;
        attr.setBaseValue(health);
    }

    public void playChestAction(Block block, boolean open) {
        Location location = block.getLocation();
        if (location.getWorld() == null) return;
        WorldServer world = ((org.bukkit.craftbukkit.v1_17_R1.CraftWorld) location.getWorld()).getHandle();
        BlockPosition position = new BlockPosition(location.getX(), location.getY(), location.getZ());
        TileEntityEnderChest ec = (TileEntityEnderChest) world.getTileEntity(position);
        assert (ec != null);
        world.playBlockAction(position, ec.getBlock().getBlock(), 1, open ? 1 : 0);
    }

    public void setEntityTarget(org.bukkit.entity.Entity ent, Player player) {
        EntityCreature entity = (EntityCreature) ((org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity) ent).getHandle();
        entity.setGoalTarget(((org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer) player).getHandle(), EntityTargetEvent.TargetReason.CLOSEST_PLAYER, true);
    }

    public ChunkGenerator getChunkGenerator() {
        return new ChunkGenerator() {
            @Override
            public ChunkData generateChunkData(@NotNull World world, @NotNull Random random, int x, int z, @NotNull BiomeGrid chunkGenerator) {
                ChunkData chunkData = createChunkData(world);
                for (int i = 0; i < 16; i++) {
                    for (int j = 0; j < 16; j++) {
                        chunkGenerator.setBiome(i, j, Biome.THE_VOID);
                    }
                }
                return chunkData;
            }
        };
    }


    public int getVersion() {
        return 17;
    }

}
