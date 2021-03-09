package com.walrusone.skywarsreloaded.nms;

import com.walrusone.skywarsreloaded.game.signs.SWRSign;
import org.bukkit.*;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.List;
import java.util.UUID;

public abstract interface NMS {
    public abstract boolean removeFromScoreboardCollection(Scoreboard scoreboard);

    public abstract void respawnPlayer(Player paramPlayer);

    public abstract void sendParticles(World paramWorld, String paramString, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, int paramInt);

    public abstract FireworkEffect getFireworkEffect(Color paramColor1, Color paramColor2, Color paramColor3, Color paramColor4, Color paramColor5, Type paramType);

    public abstract void sendTitle(Player paramPlayer, int paramInt1, int paramInt2, int paramInt3, String paramString1, String paramString2);

    public abstract void sendActionBar(Player paramPlayer, String paramString);

    public abstract String getItemName(ItemStack paramItemStack);

    public abstract void playGameSound(Location paramLocation, String paramString, float paramFloat1, float paramFloat2, boolean paramBoolean);

    public abstract ItemStack getMainHandItem(Player paramPlayer);

    public abstract ItemStack getOffHandItem(Player paramPlayer);

    public abstract ItemStack getItemStack(Material paramMaterial, List<String> paramList, String paramString);

    public abstract ItemStack getItemStack(ItemStack paramItemStack, List<String> paramList, String paramString);

    public abstract boolean isValueParticle(String paramString);

    public abstract void updateSkull(Skull paramSkull, UUID paramUUID);

    public abstract void setMaxHealth(Player paramPlayer, int paramInt);

    public abstract void spawnDragon(World paramWorld, Location paramLocation);

    public abstract Entity spawnFallingBlock(Location paramLocation, Material paramMaterial, boolean paramBoolean);

    public abstract void playEnderChestAction(Block paramBlock, boolean paramBoolean);

    public abstract void setEntityTarget(Entity paramEntity, Player paramPlayer);

    public abstract void updateSkull(SkullMeta paramSkullMeta, Player paramPlayer);

    public abstract ChunkGenerator getChunkGenerator();

    public abstract boolean checkMaterial(FallingBlock paramFallingBlock, Material paramMaterial);

    public abstract Objective getNewObjective(Scoreboard paramScoreboard, String paramString1, String paramString2);

    public abstract void setGameRule(World paramWorld, String paramString1, String paramString2);

    public abstract boolean headCheck(Block paramBlock);

    public abstract ItemStack getBlankPlayerHead();

    public abstract int getVersion();

    public abstract ItemStack getMaterial(String paramString);

    public abstract ItemStack getColorItem(String paramString, byte paramByte);

    public abstract void setBlockWithColor(World paramWorld, int paramInt1, int paramInt2, int paramInt3, Material paramMaterial, byte paramByte);

    public abstract void deleteCache();

    public abstract Block getHitBlock(ProjectileHitEvent paramProjectileHitEvent);

    public abstract void sendJSON(Player sender, String json);

    public abstract SWRSign createSWRSign(String name, Location location);


}
