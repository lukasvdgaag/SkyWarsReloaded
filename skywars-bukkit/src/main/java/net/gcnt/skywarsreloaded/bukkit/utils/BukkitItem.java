package net.gcnt.skywarsreloaded.bukkit.utils;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.utils.AbstractItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;

public class BukkitItem extends AbstractItem {

    private final SkyWarsReloaded plugin;
    private ItemStack itemStack;

    public BukkitItem(SkyWarsReloaded plugin, String material) {
        super(material);
        this.plugin = plugin;
        this.itemStack = null;
    }

    public static BukkitItem fromBukkit(SkyWarsReloaded plugin, ItemStack itemStack) {
        if (itemStack == null) return null;

        BukkitItem item = new BukkitItem(plugin, itemStack.getType().name());

        if (itemStack.hasItemMeta()) {
            ItemMeta meta = itemStack.getItemMeta();
            item.setLore(meta.getLore());

            final List<String> flags = new ArrayList<>();
            meta.getItemFlags().forEach(itemFlag -> flags.add(itemFlag.name()));
            item.setItemFlags(flags);

            final List<String> enchantments = new ArrayList<>();
            meta.getEnchants().forEach((enchantment, integer) -> enchantments.add(enchantment.getName() + ":" + integer));
            item.setEnchantments(enchantments);

            item.setDisplayName(meta.getDisplayName());
        }
        item.setDurability(item.getDurability());
        item.setDamage(item.getDamage());
        item.setAmount(item.getAmount());

        item.setItemStack(itemStack);

        return item;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public void cacheItem() {
        getBukkitItem();
    }

    @Override
    public void clearCachedItem() {
        this.itemStack = null;
    }

    public ItemStack getBukkitItem() {
        try {
            if (itemStack != null) return itemStack;

            final int serverVersion = plugin.getUtils().getServerVersion();

            Material mat = Material.valueOf(material.toUpperCase());
            MaterialData data = serverVersion > 12 ? new MaterialData(mat) : new MaterialData(mat, damage);
            ItemStack item = data.toItemStack(amount);

            if (durability != -1) {
                item.setDurability(durability);
            }

            if (item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();

                if (displayName != null) {
                    meta.setDisplayName(plugin.getUtils().colorize(displayName));
                }
                getEnchantments().forEach(ench -> {
                    try {
                        if (ench.contains(":")) {
                            final String[] split = ench.split(":");
                            meta.addEnchant(Enchantment.getByName(split[0].toUpperCase()), Integer.parseInt(split[1]), true);
                        } else {
                            meta.addEnchant(Enchantment.getByName(ench.toUpperCase()), 1, true);
                        }
                    } catch (Exception e) {
                        plugin.getLogger().severe(String.format("Failed to add enchantment %s to material %s. Ignoring it. (%s)", ench, material, e.getClass().getName() + ": " + e.getLocalizedMessage()));
                    }
                });

                getItemFlags().forEach(flag -> {
                    try {
                        meta.addItemFlags(ItemFlag.valueOf(flag.toUpperCase()));
                    } catch (Exception e) {
                        plugin.getLogger().severe(String.format("Failed to add enchantment %s to material %s. Ignoring it. (%s)", flag, material, e.getClass().getName() + ": " + e.getLocalizedMessage()));
                    }
                });

                List<String> lore = new ArrayList<>();
                getLore().forEach(line -> lore.add(plugin.getUtils().colorize(line)));
                meta.setLore(lore);

                item.setItemMeta(meta);
            }

            setItemStack(item);
            return item;
        } catch (Exception e) {
            plugin.getLogger().severe(String.format("Failed to load item with material %s. Ignoring it. (%s)", material, e.getClass().getName() + ": " + e.getLocalizedMessage()));
        }
        return null;
    }

}
