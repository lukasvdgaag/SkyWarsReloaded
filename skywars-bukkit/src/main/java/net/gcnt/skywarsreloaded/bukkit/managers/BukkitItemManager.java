package net.gcnt.skywarsreloaded.bukkit.managers;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.utils.BukkitItem;
import net.gcnt.skywarsreloaded.manager.ItemManager;
import net.gcnt.skywarsreloaded.utils.Item;

import java.awt.*;
import java.util.List;
import java.util.*;

public class BukkitItemManager implements ItemManager {

    private final SkyWarsReloaded plugin;
    private final HashMap<String, Item> defaultItems;

    public BukkitItemManager(SkyWarsReloaded plugin) {
        this.plugin = plugin;
        this.defaultItems = new HashMap<>();
    }

    @Override
    public void loadDefaultItems() {
        this.defaultItems.clear();

        Set<String> groups = new HashSet<>();
        groups.addAll(plugin.getConfig().getKeys("items"));
        groups.addAll(plugin.getMessages().getKeys("items"));

        groups.forEach(itemGroup -> {
            Set<String> items = new HashSet<>();
            items.addAll(plugin.getConfig().getKeys("items." + itemGroup));
            items.addAll(plugin.getMessages().getKeys("items." + itemGroup));

            items.forEach(itemId -> {
                String fullPath = "items." + itemGroup + "." + itemId;
                loadDefaultItem(fullPath);
            });
        });
    }

    private void loadDefaultItem(String path) {
        this.defaultItems.put(path, getItemFromConfig(path));
    }

    private boolean isDefaultLoaded(String property) {
        return this.defaultItems.containsKey(property);
    }

    @Override
    public Item createItem(String material) {
        return new BukkitItem(plugin, material);
    }

    @Override
    public Item createItem(String material, int amount) {
        Item item = new BukkitItem(plugin, material);
        item.setAmount(amount);
        return item;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Item getItem(Map<String, Object> map) {
        if (!map.containsKey("material")) map.put("material", "STONE");

        try {
            Item item = createItem((String) map.get("material"));
            if (map.containsKey("amount")) item.setAmount((Integer) map.get("amount"));
            if (map.containsKey("enchantments")) item.setEnchantments((List<String>) map.get("enchantments"));
            if (map.containsKey("display-name")) item.setDisplayName((String) map.get("display-name"));
            if (map.containsKey("lore")) item.setLore((List<String>) map.get("lore"));
            if (map.containsKey("item-flags")) item.setItemFlags((List<String>) map.get("item-flags"));

            if (map.containsKey("owner")) item.setSkullOwner((String) map.get("owner"));

            Object val = map.get("color");
            if (val != null) {
                if (val instanceof String) {
                    String str = (String) val;
                    if (str.startsWith("#")) item.setColor(Color.decode(str));
                } else if (val instanceof Integer) {
                    item.setColor(new Color((Integer) val));
                }
            }

            if (map.containsKey("damage")) {
                try {
                    int rawNumber = (Integer) map.get("damage");
                    assert rawNumber >= -128 && rawNumber <= 127;
                    byte castedByte = (byte) rawNumber;
                    item.setDamage(castedByte);
                } catch (Exception ignored) {
                }
            }
            if (map.containsKey("durability")) {
                try {
                    int rawNumber = (Integer) map.get("durability");
                    assert rawNumber >= -128 && rawNumber <= 127;
                    byte castedByte = (byte) rawNumber;
                    item.setDurability(castedByte);
                } catch (Exception ignored) {
                }
            }

            return item;
        } catch (Exception e) {
            plugin.getLogger().error(String.format("Failed to load item with material %s. Ignoring it. (%s)",
                    map.get("material"), e.getClass().getName() + ": " + e.getLocalizedMessage()));
        }
        return null;
    }

    @Override
    public Item getItemFromConfig(String path) {
        if (isDefaultLoaded(path)) return this.defaultItems.get(path).clone();

        final Item item = plugin.getConfig().getItem(path);
        if (item != null) {
            item.withMessages(plugin.getMessages().getItem(path));
        }
        return item;
    }
}
