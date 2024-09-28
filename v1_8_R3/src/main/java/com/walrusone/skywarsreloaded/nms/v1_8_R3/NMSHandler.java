package com.walrusone.skywarsreloaded.nms.v1_8_R3;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.game.signs.SWRSign;
import com.walrusone.skywarsreloaded.nms.NMS;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.BlockIterator;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class NMSHandler implements NMS {

    private NMSImpl_8_3 nmsImpl;

    public NMSHandler() {
    }

    @Override
    public SWRSign createSWRSign(String name, org.bukkit.Location location) {
        return new SWRSign83(name, location);
    }

    public boolean removeFromScoreboardCollection(Scoreboard scoreboard) {
        return false;
    }

    public void respawnPlayer(Player player) {
        player.spigot().respawn();
    }

    public void sendParticles(World world, String type, float x, float y, float z, float offsetX, float offsetY, float offsetZ, float data, int amount) {
        if (nmsImpl == null) nmsImpl = new NMSImpl_8_3();
        nmsImpl.sendParticles(world, type, x, y, z, offsetX, offsetY, offsetZ, data, amount);
    }

    public FireworkEffect getFireworkEffect(Color one, Color two, Color three, Color four, Color five, FireworkEffect.Type type) {
        return FireworkEffect.builder().flicker(false).withColor(one, two, three, four).withFade(five).with(type).trail(true).build();
    }

    public void sendTitle(Player player, int fadein, int stay, int fadeout, String title, String subtitle) {
        if (nmsImpl == null) nmsImpl = new NMSImpl_8_3();
        nmsImpl.sendTitle(player, fadein, stay, fadeout, title, subtitle);
    }

    public void playGameSound(Location loc, String paramEnumName, String paramCategory, float paramVolume, float paramPitch, boolean paramIsCustom) {
        paramEnumName = this.getSoundTranslation(paramEnumName);
        if (!paramIsCustom) {
            loc.getWorld().playSound(loc, Sound.valueOf(paramEnumName), paramVolume, paramPitch);
        }
    }

    private String getSoundTranslation(String paramEnumName) {
        if (paramEnumName.equals("ENTITY_PLAYER_DEATH")) {
            return "HURT_FLESH";
        }
        return paramEnumName;
    }

    public void sendActionBar(Player p, String msg) {
        if (nmsImpl == null) nmsImpl = new NMSImpl_8_3();
        nmsImpl.sendActionBar(p, msg);
    }

    public String getItemName(org.bukkit.inventory.ItemStack item) {
        if (item != null) {
            ItemMeta meta = item.getItemMeta();
            if (meta.hasDisplayName()) {
                return meta.getDisplayName();
            }
            return item.getType().name();
        }
        return "";
    }

    public ItemStack getMainHandItem(Player player) {
        return player.getInventory().getItemInHand();
    }

    public ItemStack getOffHandItem(Player player) {
        return null;
    }

    public org.bukkit.inventory.ItemStack getItemStack(Material material, List<String> lore, String message) {
        org.bukkit.inventory.ItemStack addItem = new org.bukkit.inventory.ItemStack(material, 1);
        ItemMeta addItemMeta = addItem.getItemMeta();
        addItemMeta.setDisplayName(message);
        addItemMeta.setLore(lore);
        addItemMeta.addItemFlags(ItemFlag.values());
        addItem.setItemMeta(addItemMeta);
        return addItem;
    }

    public org.bukkit.inventory.ItemStack getItemStack(org.bukkit.inventory.ItemStack item, List<String> lore, String message) {
        org.bukkit.inventory.ItemStack addItem = item.clone();
        ItemMeta addItemMeta = addItem.getItemMeta();
        addItemMeta.setDisplayName(message);
        addItemMeta.setLore(lore);

        if (SkyWarsReloaded.getNMS().getVersion() < 21) {
            addItemMeta.addItemFlags(ItemFlag.values());
        } else {
            addItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.valueOf("HIDE_ADDITIONAL_TOOLTIP"));
        }
        addItem.setItemMeta(addItemMeta);
        return addItem;
    }

    public boolean isValueParticle(String string) {
        return true;
    }

    public void updateSkull(Skull skull, UUID uuid) {
        skull.setSkullType(SkullType.PLAYER);
        skull.setOwner(Bukkit.getOfflinePlayer(uuid).getName());
    }

    public void setMaxHealth(Player player, int health) {
        player.setMaxHealth(health);
    }

    public void spawnDragon(World world, Location loc) {
        world.spawnEntity(loc, EntityType.ENDER_DRAGON);
    }


    public Entity spawnFallingBlock(Location loc, Material mat, boolean damage) {
        if (loc.getWorld() == null) return null;
        FallingBlock block = loc.getWorld().spawnFallingBlock(loc, mat, (byte) 0);
        block.setDropItem(false);
        block.setHurtEntities(damage);
        return block;
    }

    public void playChestAction(Block block, boolean open) {
        if (nmsImpl == null) nmsImpl = new NMSImpl_8_3();
        nmsImpl.playChestAction(block, open);
    }

    public void setEntityTarget(Entity ent, Player player) {
        if (nmsImpl == null) nmsImpl = new NMSImpl_8_3();
        nmsImpl.setEntityTarget(ent, player);
    }

    public void updateSkull(SkullMeta meta1, Player player) {
        meta1.setOwner(player.getName());
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

    public boolean checkMaterial(FallingBlock fb, Material mat) {
        return fb.getMaterial().equals(mat);
    }

    public Objective getNewObjective(Scoreboard scoreboard, String criteria, String DisplayName) {
        return scoreboard.registerNewObjective(DisplayName, criteria);
    }

    public void setGameRule(World world, String rule, String bool) {
        world.setGameRuleValue(rule, bool);
    }

    public boolean headCheck(Block h1) {
        return h1.getType() == Material.SKULL;
    }

    public org.bukkit.inventory.ItemStack getBlankPlayerHead() {
        return new org.bukkit.inventory.ItemStack(Material.SKULL_ITEM, 1, (short) 3);
    }

    public int getVersion() {
        return 8;
    }

    public org.bukkit.inventory.ItemStack getMaterial(String item) {
        if (item.equalsIgnoreCase("SKULL_ITEM")) {
            return new org.bukkit.inventory.ItemStack(Material.SKULL_ITEM, 1, (short) 1);
        }
        return new org.bukkit.inventory.ItemStack(Material.valueOf(item), 1);
    }


    public org.bukkit.inventory.ItemStack getColorItem(String mat, byte color) {
        if (mat.equalsIgnoreCase("wool"))
            return new org.bukkit.inventory.ItemStack(Material.WOOL, 1, color);
        if (mat.equalsIgnoreCase("glass"))
            return new org.bukkit.inventory.ItemStack(Material.STAINED_GLASS, 1, color);
        if (mat.equalsIgnoreCase("banner")) {
            return new org.bukkit.inventory.ItemStack(Material.BANNER, 1, color);
        }
        return new org.bukkit.inventory.ItemStack(Material.STAINED_GLASS, 1, color);
    }


    public void setBlockWithColor(World world, int x, int y, int z, Material mat, byte cByte) {
        world.getBlockAt(x, y, z).setType(mat);
        world.getBlockAt(x, y, z).setData(cByte);
    }


    public void deleteCache() {
    }


    public Block getHitBlock(ProjectileHitEvent event) {
        BlockIterator iterator = new BlockIterator(event.getEntity().getWorld(), event.getEntity().getLocation().toVector(), event.getEntity().getVelocity().normalize(), 0.0D, 4);
        Block hitBlock = null;
        while (iterator.hasNext()) {
            hitBlock = iterator.next();
            if (hitBlock.getType() != Material.AIR) {
                break;
            }
        }
        return hitBlock;
    }

    @Override
    public void sendJSON(Player sender, String json) {
        if (nmsImpl == null) nmsImpl = new NMSImpl_8_3();
        nmsImpl.sendJSON(sender, json);
    }

    @Override
    public boolean isHoldingTotem(Player player) {
        return false;
    }

    @Override
    public void applyTotemEffect(Player player) {

    }

    @Override
    public PotionEffectType getPotionEffectTypeByName(String... name) {
        for (String s : name) {
            PotionEffectType type = PotionEffectType.getByName(s);
            if (type != null) {
                return type;
            }
        }
        return null;
    }

    @Override
    public Enchantment getEnchantmentByName(String... name) {
        for (String s : name) {
            Enchantment enchantment = Enchantment.getByName(s);
            if (enchantment != null) {
                return enchantment;
            }
        }
        return null;
    }
}
