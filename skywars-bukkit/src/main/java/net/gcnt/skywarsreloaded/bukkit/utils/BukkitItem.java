package net.gcnt.skywarsreloaded.bukkit.utils;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.utils.AbstractItem;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
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

        ItemMeta meta = itemStack.getItemMeta();
        if (plugin.getUtils().getServerVersion() <= 12) item.setDamage(itemStack.getData().getData());
        if (meta != null) {
            item.setLore(meta.getLore());

            final List<String> flags = new ArrayList<>();
            meta.getItemFlags().forEach(itemFlag -> flags.add(itemFlag.name()));
            item.setItemFlags(flags);

            final List<String> enchantments = new ArrayList<>();
            meta.getEnchants().forEach((enchantment, integer) -> enchantments.add(enchantment.getName() + ":" + integer));
            item.setEnchantments(enchantments);

            item.setDisplayName(meta.getDisplayName());

            if (meta instanceof SkullMeta skullMeta) {
                if (plugin.getUtils().getServerVersion() >= 12) {
                    if (skullMeta.getOwningPlayer() != null) {
                        item.setSkullOwner(skullMeta.getOwningPlayer().getUniqueId().toString());
                    } else {
                        item.setSkullOwner(skullMeta.getOwner());
                    }
                } else {
                    item.setSkullOwner(skullMeta.getOwner());
                }
            } else if (meta instanceof LeatherArmorMeta armorMeta) {
                item.setColor(new java.awt.Color(armorMeta.getColor().asRGB()));
            }
        }
        item.setDurability(itemStack.getDurability());
        item.setAmount(itemStack.getAmount());

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
            if (material == null || material.isEmpty()) return null;

            final int serverVersion = plugin.getUtils().getServerVersion();

            Material mat = Material.valueOf(material.toUpperCase());
            MaterialData data = serverVersion > 12 ? new MaterialData(mat) : new MaterialData(mat, damage);
            ItemStack item = data.toItemStack(amount);

            if (durability != -1) {
                item.setDurability(durability);
            }

            ItemMeta meta = item.getItemMeta();
            if (meta != null) {

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
                        plugin.getLogger().error(String.format("Failed to add enchantment %s to material %s. Ignoring it. (%s)", ench, material, e.getClass().getName() + ": " + e.getLocalizedMessage()));
                    }
                });

                getItemFlags().forEach(flag -> {
                    try {
                        meta.addItemFlags(ItemFlag.valueOf(flag.toUpperCase()));
                    } catch (Exception e) {
                        plugin.getLogger().error(String.format("Failed to add flag %s to material %s. Ignoring it. (%s)", flag, material, e.getClass().getName() + ": " + e.getLocalizedMessage()));
                    }
                });

                List<String> lore = new ArrayList<>();
                getLore().forEach(line -> lore.add(plugin.getUtils().colorize(line)));
                meta.setLore(lore);

                if (skullOwner != null) {
                    try {
                        SkullMeta skullMeta = (SkullMeta) meta;
                        if (plugin.getUtils().getServerVersion() >= 12) {
                            skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(skullOwner));
                        } else {
                            skullMeta.setOwner(skullOwner);
                        }
                        item.setItemMeta(skullMeta);
                    } catch (Exception ignored) {
                    }
                }

                if (color != null) {
                    try {
                        LeatherArmorMeta armorMeta = (LeatherArmorMeta) meta;
                        armorMeta.setColor(Color.fromRGB(color.getRed(), color.getGreen(), color.getBlue()));
                        item.setItemMeta(armorMeta);
                    } catch (Exception ignored) {
                        ignored.printStackTrace();
                    }
                } else item.setItemMeta(meta);
            }

            setItemStack(item);
            return item;
        } catch (Exception e) {
            plugin.getLogger().error(String.format("Failed to load item with material %s. Ignoring it. (%s)", material, e.getClass().getName() + ": " + e.getLocalizedMessage()));
        }
        return null;
    }

}
