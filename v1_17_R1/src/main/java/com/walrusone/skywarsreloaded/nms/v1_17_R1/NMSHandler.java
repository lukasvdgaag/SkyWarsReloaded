package com.walrusone.skywarsreloaded.nms.v1_17_R1;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class NMSHandler extends com.walrusone.skywarsreloaded.nms.v1_16_R3.NMSHandler {

    private NMSImpl_17_1 nmsImpl;

    public void playChestAction(Block block, boolean open) {
        if (nmsImpl == null) nmsImpl = new NMSImpl_17_1();
        nmsImpl.playChestAction(block, open);
    }

    public void setEntityTarget(org.bukkit.entity.Entity ent, Player player) {
        if (nmsImpl == null) nmsImpl = new NMSImpl_17_1();
        nmsImpl.setEntityTarget(ent, player);
    }

    public void sendActionBar(Player player, String msg) {
        msg = ChatColor.translateAlternateColorCodes('&', msg);
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(msg).create());
    }

    public void setMaxHealth(Player player, int health) {
        AttributeInstance attr = player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH);
        assert attr != null;
        attr.setBaseValue(health);
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
