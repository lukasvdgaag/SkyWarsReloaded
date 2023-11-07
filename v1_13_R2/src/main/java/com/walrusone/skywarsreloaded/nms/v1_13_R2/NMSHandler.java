package com.walrusone.skywarsreloaded.nms.v1_13_R2;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Random;
import java.util.UUID;


public class NMSHandler extends com.walrusone.skywarsreloaded.nms.v1_12_R1.NMSHandler {

    private NMSImpl_13_2 nmsImpl;

    public void updateSkull(Skull skull, UUID uuid) {
        if (skull.getType().equals(Material.SKELETON_SKULL)) {
            Block block = skull.getBlock();
            block.setType(Material.PLAYER_HEAD);
            Skull s = (Skull) block.getState();
            s.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
        } else {
            skull.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
        }
    }

    public Entity spawnFallingBlock(Location loc, Material mat, boolean damage) {
        if (loc.getWorld() == null) return null;
        FallingBlock block = loc.getWorld().spawnFallingBlock(loc, new org.bukkit.material.MaterialData(mat));
        block.setDropItem(false);
        block.setHurtEntities(damage);
        return block;
    }

    public void playChestAction(Block block, boolean open) {
        if (nmsImpl == null) nmsImpl = new NMSImpl_13_2();
        nmsImpl.playChestAction(block, open);
    }

    public void setEntityTarget(org.bukkit.entity.Entity ent, Player player) {
        if (nmsImpl == null) nmsImpl = new NMSImpl_13_2();
        nmsImpl.setEntityTarget(ent, player);
    }

    public void updateSkull(org.bukkit.inventory.meta.SkullMeta meta1, Player player) {
        meta1.setOwningPlayer(player);
    }

    public ChunkGenerator getChunkGenerator() {
        return new ChunkGenerator() {
            public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid chunkGererator) {
                ChunkData chunkData = createChunkData(world);
                for (int i = 0; i < 16; i++) {
                    for (int j = 0; j < 16; j++) {
                        chunkGererator.setBiome(i, j, org.bukkit.block.Biome.valueOf("THE_VOID"));
                    }
                }
                return chunkData;
            }
        };
    }

    public void setGameRule(World world, String ruleName, String value) {
        // Handle bools
        Boolean valueBool = null;
        if (value.equalsIgnoreCase("true")) valueBool = true;
        else if (value.equalsIgnoreCase("false")) valueBool = false;
        // Handle ints
        Integer valueInt = null;
        if (valueBool == null) {
            try {
                valueInt = Integer.parseInt(value);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        // Apply
        try {
            if (valueBool == null) {
                GameRule<Integer> gameRule = (GameRule<Integer>) GameRule.getByName(ruleName);
                if (gameRule == null || valueInt == null)
                    throw new Exception("Invalid GameRule or value provided: " + ruleName + " -> " + value);
                world.setGameRule(gameRule, valueInt);
            } else {
                GameRule<Boolean> gameRule = (GameRule<Boolean>) GameRule.getByName(ruleName);
                if (gameRule == null)
                    throw new Exception("Invalid GameRule: " + ruleName);
                world.setGameRule(gameRule, valueBool);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean headCheck(Block h1) {
        return (h1.getType() == Material.valueOf("PLAYER_WALL_HEAD")) || (h1.getType() == Material.valueOf("PLAYER_HEAD")) || (h1.getType() == Material.valueOf("SKELETON_SKULL"));
    }

    public ItemStack getBlankPlayerHead() {
        return new ItemStack(Material.PLAYER_HEAD, 1);
    }

    public int getVersion() {
        return 13;
    }

    public ItemStack getMaterial(String item) {
        if (item.equalsIgnoreCase("SKULL_ITEM"))
            return new ItemStack(Material.valueOf("SKELETON_SKULL"), 1);
        if (item.equalsIgnoreCase("ENDER_PORTAL_FRAME"))
            return new ItemStack(Material.valueOf("END_PORTAL_FRAME"), 1);
        if (item.equalsIgnoreCase("WORKBENCH"))
            return new ItemStack(Material.valueOf("CRAFTING_TABLE"), 1);
        if (item.equalsIgnoreCase("IRON_FENCE"))
            return new ItemStack(Material.valueOf("IRON_BARS"), 1);
        if (item.equalsIgnoreCase("REDSTONE_COMPARATOR"))
            return new ItemStack(Material.valueOf("COMPARATOR"));
        if (item.equalsIgnoreCase("SIGN_POST"))
            return new ItemStack(Material.valueOf("BIRCH_SIGN"));
        if (item.equalsIgnoreCase("STONE_PLATE"))
            return new ItemStack(Material.valueOf("STONE_PRESSURE_PLATE"));
        if (item.equalsIgnoreCase("IRON_PLATE"))
            return new ItemStack(Material.valueOf("HEAVY_WEIGHTED_PRESSURE_PLATE"));
        if (item.equalsIgnoreCase("GOLD_PLATE"))
            return new ItemStack(Material.valueOf("LIGHT_WEIGHTED_PRESSURE_PLATE"));
        if (item.equalsIgnoreCase("MOB_SPAWNER"))
            return new ItemStack(Material.valueOf("SPAWNER"));
        if (item.equalsIgnoreCase("SNOW_BALL")) {
            return new ItemStack(Material.valueOf("SNOWBALL"));
        }
        return new ItemStack(Material.AIR, 1);
    }


    public ItemStack getColorItem(String mat, byte color) {
        String col = getColorFromByte(color);
        if (mat.equalsIgnoreCase("wool"))
            return new ItemStack(Material.valueOf(col + "_WOOL"), 1);
        if (mat.equalsIgnoreCase("stained_glass"))
            return new ItemStack(Material.valueOf(col + "_STAINED_GLASS"), 1);
        if (mat.equalsIgnoreCase("banner")) {
            return new ItemStack(Material.valueOf(col + "_BANNER"), 1);
        }
        return new ItemStack(Material.valueOf(col + "_STAINED_GLASS"), 1);
    }

    protected String getColorFromByte(byte color) {
        switch (color) {
            case 0:
                return "WHITE";
            case 1:
                return "ORANGE";
            case 2:
                return "MAGENTA";
            case 3:
                return "LIGHT_BLUE";
            case 4:
                return "YELLOW";
            case 5:
                return "LIME";
            case 6:
                return "PINK";
            case 7:
                return "GRAY";
            case 8:
                return "LIGHT_GRAY";
            case 9:
                return "CYAN";
            case 10:
                return "PURPLE";
            case 11:
                return "BLUE";
            case 12:
                return "BROWN";
            case 13:
                return "GREEN";
            case 14:
                return "RED";
            case 15:
                return "BLACK";
        }
        return "WHITE";
    }


    public void setBlockWithColor(World world, int x, int y, int z, Material mat, byte cByte) {
        world.getBlockAt(x, y, z).setType(mat);
    }

    @Override
    public Objective getNewObjective(Scoreboard scoreboard, String criteria, String DisplayName) {
        return scoreboard.registerNewObjective(DisplayName, criteria, DisplayName);
    }

    @Override
    public boolean isHoldingTotem(Player player) {
        return player.getInventory().getItemInMainHand().getType().equals(Material.TOTEM_OF_UNDYING) ||
                player.getInventory().getItemInOffHand().getType().equals(Material.TOTEM_OF_UNDYING);
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
