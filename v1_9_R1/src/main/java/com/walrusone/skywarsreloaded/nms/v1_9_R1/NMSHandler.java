package com.walrusone.skywarsreloaded.nms.v1_9_R1;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class NMSHandler extends com.walrusone.skywarsreloaded.nms.v1_8_R3.NMSHandler {

    private NMSImpl_9_1 nmsImpl;

    public void sendParticles(World world, String type, float x, float y, float z, float offsetX, float offsetY, float offsetZ, float data, int amount) {
        world.spawnParticle(Particle.valueOf(type), x, y, z, amount, offsetX, offsetY, offsetZ, data);
    }

    public void sendTitle(Player player, int fadein, int stay, int fadeout, String title, String subtitle) {
        if (nmsImpl == null) nmsImpl = new NMSImpl_9_1();
        nmsImpl.sendTitle(player, fadein, stay, fadeout, title, subtitle);
    }

    public void sendActionBar(Player p, String msg) {
        p.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                ChatColor.translateAlternateColorCodes('&', msg)
        ));
    }

    public void playGameSound(Location loc, String paramEnumName, String paramCategory, float paramVolume, float paramPitch, boolean paramIsCustom) {
        if (paramIsCustom) {
            loc.getWorld().playSound(loc, paramEnumName, paramVolume, paramPitch);
        } else {
            loc.getWorld().playSound(loc, Sound.valueOf(paramEnumName), paramVolume, paramPitch);
        }
    }

    public ItemStack getMainHandItem(Player player) {
        return player.getInventory().getItemInMainHand();
    }

    public ItemStack getOffHandItem(Player player) {
        return player.getInventory().getItemInOffHand();
    }

    public boolean isValueParticle(String string) {
        try {
            Particle.valueOf(string);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    public void setMaxHealth(Player player, int health) {
        player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
    }

    public void playChestAction(Block block, boolean open) {
        if (nmsImpl == null) nmsImpl = new NMSImpl_9_1();
        nmsImpl.playChestAction(block, open);
    }

    public void setEntityTarget(Entity ent, Player player) {
        if (nmsImpl == null) nmsImpl = new NMSImpl_9_1();
        nmsImpl.setEntityTarget(ent, player);
    }

    public ChunkGenerator getChunkGenerator() {
        return new ChunkGenerator() {
            public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid chunkGererator) {
                ChunkData chunkData = createChunkData(world);
                for (int i = 0; i < 16; i++) {
                    for (int j = 0; j < 16; j++) {
                        chunkGererator.setBiome(i, j, org.bukkit.block.Biome.VOID);
                    }
                }
                return chunkData;
            }
        };
    }

    public int getVersion() {
        return 9;
    }

    @Override
    public void sendJSON(Player sender, String json) {
        sender.spigot().sendMessage(ChatMessageType.CHAT, ComponentSerializer.parse(ChatColor.translateAlternateColorCodes('&', json)));
    }

}
