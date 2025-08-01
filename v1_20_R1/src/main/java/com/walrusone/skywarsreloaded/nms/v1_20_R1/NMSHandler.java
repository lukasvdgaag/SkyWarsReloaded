package com.walrusone.skywarsreloaded.nms.v1_20_R1;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class NMSHandler extends com.walrusone.skywarsreloaded.nms.v1_19_R1.NMSHandler {

    public int getVersion() {
        return 20;
    }

    @Override
    public PotionEffectType getPotionEffectTypeByName(String... name) {
        for (String n : name) {
            try {
                PotionEffectType type = Registry.EFFECT.get(NamespacedKey.fromString(n.toLowerCase(Locale.ROOT)));
                if (type != null) {
                    return type;
                }
            } catch (Exception e) {
                // ignore
            }
        }
        return null;
    }

    @Override
    public Enchantment getEnchantmentByName(String... name) {
        for (String n : name) {
            try {
                Enchantment enchantment = Registry.ENCHANTMENT.get(NamespacedKey.fromString(n.toLowerCase(Locale.ROOT)));
                if (enchantment != null) {
                    return enchantment;
                }
            } catch (Exception e) {
                // ignore
            }
        }
        return null;
    }

    private final NamespacedKey itemIdKey = new NamespacedKey("skywarsreloaded", "item_id");

    @Override
    public ItemStack getItemStack(ItemStack item, List<String> lore, String displayName) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        // Convertimos el display name desde Component a String (usando colores legacy)
        String legacyDisplayName = LegacyComponentSerializer.legacySection().serialize(Component.text(displayName));
        List<String> legacyLore = lore.stream()
                .map(line -> LegacyComponentSerializer.legacySection().serialize(Component.text(line)))
                .collect(Collectors.toList());

        meta.setDisplayName(legacyDisplayName);
        meta.setLore(legacyLore);

        item.setItemMeta(meta);
        return item;
    }

    @Override
    public ItemStack getColorItem(String material, byte data) {
        Material mat = Material.matchMaterial(material.toUpperCase());
        if (mat == null) return new ItemStack(Material.BARRIER);
        return new ItemStack(mat);
    }

    // Nuevo: marca el item con un ID
    public ItemStack tagItemWithId(ItemStack item, String id) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;
        meta.getPersistentDataContainer().set(itemIdKey, PersistentDataType.STRING, id);
        item.setItemMeta(meta);
        return item;
    }

    // Nuevo: compara un ítem clicado con uno registrado por ID
    public boolean isItemWithId(ItemStack item, String id) {
        if (item == null || !item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        return id.equals(meta.getPersistentDataContainer().get(itemIdKey, PersistentDataType.STRING));
    }
}
