package com.walrusone.skywarsreloaded.nms;

import com.walrusone.skywarsreloaded.game.signs.SWRSign;
import org.bukkit.*;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.List;
import java.util.UUID;

public interface NMS {

    boolean removeFromScoreboardCollection(Scoreboard scoreboard);

    void respawnPlayer(Player paramPlayer);

    void sendParticles(World paramWorld, String paramString, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, int paramInt);

    FireworkEffect getFireworkEffect(Color paramColor1, Color paramColor2, Color paramColor3, Color paramColor4, Color paramColor5, Type paramType);

    void sendTitle(Player paramPlayer, int paramInt1, int paramInt2, int paramInt3, String paramString1, String paramString2);

    void sendActionBar(Player paramPlayer, String paramString);

    String getItemName(ItemStack paramItemStack);

    void playGameSound(Location paramLocation, String paramEnumName, String paramCategory, float paramVolume, float paramPitch, boolean paramIsCustom);

    ItemStack getMainHandItem(Player paramPlayer);

    ItemStack getOffHandItem(Player paramPlayer);

    ItemStack getItemStack(Material paramMaterial, List<String> paramList, String paramString);

    ItemStack getItemStack(ItemStack paramItemStack, List<String> paramList, String paramString);

    boolean isValueParticle(String paramString);

    void updateSkull(Skull paramSkull, UUID paramUUID);

    void setMaxHealth(Player paramPlayer, int paramInt);

    void spawnDragon(World paramWorld, Location paramLocation);

    Entity spawnFallingBlock(Location paramLocation, Material paramMaterial, boolean paramBoolean);

    /**
     * Send a chest action to players
     *
     * @param paramBlock the chest block
     * @param open       true to open, false to close
     */
    void playChestAction(Block paramBlock, boolean open);

    void setEntityTarget(Entity paramEntity, Player paramPlayer);

    void updateSkull(SkullMeta paramSkullMeta, Player paramPlayer);

    ChunkGenerator getChunkGenerator();

    boolean checkMaterial(FallingBlock paramFallingBlock, Material paramMaterial);

    Objective getNewObjective(Scoreboard paramScoreboard, String paramString1, String paramString2);

    void setGameRule(World paramWorld, String paramString1, String paramString2);

    boolean headCheck(Block paramBlock);

    ItemStack getBlankPlayerHead();

    int getVersion();

    ItemStack getMaterial(String paramString);

    ItemStack getColorItem(String paramString, byte paramByte);

    void setBlockWithColor(World paramWorld, int paramInt1, int paramInt2, int paramInt3, Material paramMaterial, byte paramByte);

    void deleteCache();

    Block getHitBlock(ProjectileHitEvent paramProjectileHitEvent);

    void sendJSON(Player sender, String json);

    SWRSign createSWRSign(String name, Location location);

    boolean isHoldingTotem(Player player);

    void applyTotemEffect(Player player);

    /**
     * Get a potion effect type by its name.
     * To allow for easy backwards compatibility, this method accepts multiple names to check for.
     * The first name that matches a potion effect type will be returned.
     *
     * @param names list of names to check for
     * @return the potion effect type, or null if not found
     */
    PotionEffectType getPotionEffectTypeByName(String... names);

    /**
     * Get an enchantment by its name.
     * To allow for easy backwards compatibility, this method accepts multiple names to check for.
     * The first name that matches an enchantment will be returned.
     *
     * @param names list of names to check for
     * @return the enchantment, or null if not found
     */
    Enchantment getEnchantmentByName(String... names);

}
