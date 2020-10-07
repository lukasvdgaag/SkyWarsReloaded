package com.walrusone.skywarsreloaded.managers;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.enums.ChestType;
import com.walrusone.skywarsreloaded.enums.Vote;
import com.walrusone.skywarsreloaded.utilities.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

public class ChestManager {

    private final Map<Integer, Inventory> chestItemList = new HashMap<>();
    private final Map<Integer, Inventory> opChestItemList = new HashMap<>();
    private final Map<Integer, Inventory> basicChestItemList = new HashMap<>();
    private final Map<Integer, Inventory> centerChestItemList = new HashMap<>();
    private final Map<Integer, Inventory> basicCenterChestItemList = new HashMap<>();
    private final Map<Integer, Inventory> opCenterChestItemList = new HashMap<>();
    private final Map<Integer, Inventory> crateItemList = new HashMap<>();

    private final Random random = new Random();

    private List<Integer> randomLoc = new ArrayList<>();
    private List<Integer> randomDLoc = new ArrayList<>();


    public ChestManager() {
        load(chestItemList, "chest.yml");
        load(opChestItemList, "opchest.yml");
        load(basicChestItemList, "basicchest.yml");
        load(centerChestItemList, "centerchest.yml");
        load(opCenterChestItemList, "opcenterchest.yml");
        load(basicCenterChestItemList, "basiccenterchest.yml");
        load(crateItemList, "crates.yml");
        for (int i = 0; i < 27; i++) {
            randomLoc.add(i);
        }
        for (int i = 0; i < 54; i++) {
            randomDLoc.add(i);
        }
    }

    public void addItems(List<ItemStack> items, ChestType ct, int percent) {
        Map<Integer, Inventory> toAddTo = getItemMap(ct);
        String fileName = getFileName(ct);
        if (!toAddTo.containsKey(percent)) {
            toAddTo.put(percent, Bukkit.createInventory(null, 54, fileName + " " + percent));
        }

        for (ItemStack iStack : items) {
            toAddTo.get(percent).addItem(iStack);
        }
        save(toAddTo, ct);
    }

    public void load(Map<Integer, Inventory> itemList, String fileName) {
        itemList.clear();
        File chestFile = new File(SkyWarsReloaded.get().getDataFolder(), fileName);

        if (!chestFile.exists()) {
            SkyWarsReloaded.get().saveResource(fileName, false);
        }

        if (chestFile.exists()) {
            FileConfiguration storage = YamlConfiguration.loadConfiguration(chestFile);

            if (storage.getConfigurationSection("chestItems") != null) {
                for (String key : storage.getConfigurationSection("chestItems").getKeys(false)) {
                    if (Util.get().isInteger(key)) {
                        int percent = Integer.parseInt(key);
                        List<ItemStack> items = (List<ItemStack>) storage.getList("chestItems." + key + ".items");
                        if (!itemList.containsKey(percent)) {
                            itemList.put(percent, Bukkit.createInventory(null, 54, fileName + " " + percent));
                        }
                        for (ItemStack iStack : items) {
                            if (iStack != null) itemList.get(percent).addItem(iStack);
                        }
                    }
                }
            }
        }
    }

    public void save(String title) {
        String parts[] = title.split(" ", 2);
        ChestType ct = getChestType(ChatColor.stripColor(parts[0]));
        if (ct != null) {
            Map<Integer, Inventory> toSave = getItemMap(ct);
            save(toSave, ct);
        }
    }

    private void save(Map<Integer, Inventory> chestList, ChestType ct) {
        String fileName = getFileName(ct);
        File chestFile = new File(SkyWarsReloaded.get().getDataFolder(), fileName);

        if (!chestFile.exists()) {
            SkyWarsReloaded.get().saveResource(fileName, false);
        }

        if (chestFile.exists()) {
            try {
                FileConfiguration storage = YamlConfiguration.loadConfiguration(chestFile);
                for (int percent : chestList.keySet()) {
                    List<ItemStack> items = new ArrayList<>();
                    for (ItemStack item : chestList.get(percent).getContents()) {
                        if (item != null && !item.getType().equals(Material.AIR)) {
                            items.add(item);
                        }
                    }
                    storage.set("chestItems." + percent + ".items", items);
                }
                storage.save(chestFile);
            } catch (IOException ioException) {
                System.out.println("Failed to save chestfile " + fileName + ": " + ioException.getMessage());
            }
        }
    }

    public void populateChest(Object chest, Vote cVote, boolean center) {
        if (cVote == Vote.CHESTOP) {
            if (center) {
                fillChest(chest, opCenterChestItemList);
            } else {
                fillChest(chest, opChestItemList);
            }
        } else if (cVote == Vote.CHESTBASIC) {
            if (center) {
                fillChest(chest, basicCenterChestItemList);
            } else {
                fillChest(chest, basicChestItemList);
            }
        } else if (cVote == Vote.CHESTNORMAL) {
            if (center) {
                fillChest(chest, centerChestItemList);
            } else {
                fillChest(chest, chestItemList);
            }
        } else {
            Inventory inventory = null;
            if (chest instanceof DoubleChest) {
                inventory = ((DoubleChest) chest).getInventory();
            } else if (chest instanceof Chest) {
                inventory = ((Chest) chest).getInventory();
            }
            if (inventory != null) {
                inventory.clear();
            }
        }
    }

    private void fillChest(Object chest, Map<Integer, Inventory> fill) {
        Inventory inventory = null;
        if (chest instanceof Chest) {
            inventory = ((Chest) chest).getInventory();
        } else if (chest instanceof DoubleChest) {
            inventory = ((DoubleChest) chest).getInventory();
        }
        if (inventory != null) {
            inventory.clear();
            int added = 0;
            Collections.shuffle(randomLoc);
            Collections.shuffle(randomDLoc);
            adding:
            {
                for (int chance : fill.keySet()) {
                    for (ItemStack item : fill.get(chance)) {
                        if (item != null && !item.getType().equals(Material.AIR)) {
                            if (chest instanceof Chest) {
                                if (random.nextInt(100) + 1 <= chance) {
                                    inventory.setItem(randomLoc.get(added), item);
                                    added++;
                                    if (added >= inventory.getSize() - 1 || added >= SkyWarsReloaded.getCfg().getMaxChest()) {
                                        break adding;
                                    }
                                }
                            }
                            if (chest instanceof DoubleChest) {
                                if (random.nextInt(100) + 1 <= chance) {
                                    inventory.setItem(randomDLoc.get(added), item);
                                    added++;
                                    if (added >= inventory.getSize() - 1 || added >= SkyWarsReloaded.getCfg().getMaxDoubleChest()) {
                                        break adding;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void fillCrate(Inventory inventory, int max) {
        if (inventory != null) {
            inventory.clear();
            int added = 0;
            Collections.shuffle(randomLoc);

            for (int chance : crateItemList.keySet()) {
                for (ItemStack item : crateItemList.get(chance)) {
                    if (item != null && !item.getType().equals(Material.AIR)) {
                        if (random.nextInt(100) + 1 <= chance) {
                            inventory.setItem(randomLoc.get(added), item);
                            added++;
                            if (added >= inventory.getSize() - 1 || added >= max) {
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    public void editChest(ChestType ct, int percent, Player player) {
        Map<Integer, Inventory> toEdit = getItemMap(ct);
        String fileName = getFileName(ct);
        if (!toEdit.containsKey(percent)) {
            toEdit.put(percent, Bukkit.createInventory(null, 54, fileName + " " + percent));
        }
        player.openInventory(toEdit.get(percent));
    }

    private String getFileName(ChestType ct) {
        if (ct == ChestType.BASIC) {
            return "basicchest.yml";
        } else if (ct == ChestType.BASICCENTER) {
            return "basiccenterchest.yml";
        } else if (ct == ChestType.OP) {
            return "opchest.yml";
        } else if (ct == ChestType.OPCENTER) {
            return "opcenterchest.yml";
        } else if (ct == ChestType.NORMALCENTER) {
            return "centerchest.yml";
        } else if (ct == ChestType.CRATE) {
            return "crates.yml";
        }
        else {
            return "chest.yml";
        }
    }

    private ChestType getChestType(String fileName) {
        if (fileName.equalsIgnoreCase("basicchest.yml")) {
            return ChestType.BASIC;
        } else if (fileName.equalsIgnoreCase("basiccenterchest.yml")) {
            return ChestType.BASICCENTER;
        } else if (fileName.equalsIgnoreCase("opchest.yml")) {
            return ChestType.OP;
        } else if (fileName.equalsIgnoreCase("opcenterchest.yml")) {
            return ChestType.OPCENTER;
        } else if (fileName.equalsIgnoreCase("centerchest.yml")) {
            return ChestType.NORMALCENTER;
        } else if (fileName.equalsIgnoreCase("chest.yml")) {
            return ChestType.NORMAL;
        } else if (fileName.equalsIgnoreCase("crates.yml")) {
            return ChestType.CRATE;
        }
        return null;
    }

    private Map<Integer, Inventory> getItemMap(ChestType ct) {
        if (ct == ChestType.BASIC) {
            return basicChestItemList;
        } else if (ct == ChestType.OP) {
            return opChestItemList;
        } else if (ct == ChestType.NORMAL) {
            return chestItemList;
        } else if (ct == ChestType.BASICCENTER) {
            return basicCenterChestItemList;
        } else if (ct == ChestType.OPCENTER) {
            return opCenterChestItemList;
        } else if (ct == ChestType.NORMALCENTER) {
            return centerChestItemList;
        } else {
            return crateItemList;
        }
    }


}