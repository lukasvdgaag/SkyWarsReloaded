package net.gcnt.skywarsreloaded.bukkit.utils;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.wrapper.item.BukkitSWEnchantmentType;
import net.gcnt.skywarsreloaded.utils.AbstractItem;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BukkitItem extends AbstractItem {

    private final SkyWarsReloaded plugin;
    private ItemStack itemStack;

    public BukkitItem(SkyWarsReloaded plugin, String material) {
        this.plugin = plugin;
        this.itemStack = new ItemStack(validateMaterial(material));
    }

    public BukkitItem(SkyWarsReloaded plugin, ItemStack itemStack) {
        this.plugin = plugin;
        this.itemStack = itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public String getMaterial() {
        return this.itemStack.getType().toString();
    }

    @Override
    public void setMaterial(String material) {
        try {
            this.itemStack.setType(Material.valueOf(material));
        } catch (Exception ex) {
            this.plugin.getLogger().warn("Attempted to use a material that doesn't exist! \"" + material + "\"");
            ex.printStackTrace();
            this.itemStack.setType(Material.STONE);
        }
    }

    @Override
    public int getAmount() {
        return this.itemStack.getAmount();
    }

    @Override
    public void setAmount(int amount) {
        this.itemStack.setAmount(amount);
    }

    @Override
    public List<String> getEnchantments() {
        // ENCHANTMENT:LEVEL
        return this.itemStack.getEnchantments().entrySet().stream()
                .map((enchantType) -> enchantType.getKey().getKey().getKey() + ":" + enchantType.getValue().toString())
                .collect(Collectors.toList());
    }

    @Override
    public void setEnchantments(List<String> enchantments) {
        ItemMeta meta = itemStack.getItemMeta();
        enchantments.forEach(enchantment -> {
            String[] split = enchantment.split(":");

            String type = split[0];
            int level = 1;
            if (split.length > 1 && plugin.getUtils().isInt(split[1])) {
                level = Integer.parseInt(split[1]);
            }

            try {
                Enchantment ench = ((BukkitSWEnchantmentType) plugin.getNMSManager().getNMS().getEnchantment(type)).getEnchantment();
                assert meta != null;
                meta.addEnchant(ench, level, true);
            } catch (Exception e) {
                plugin.getLogger().error("Enchantment with name '" + type + "' could not be resolved for item " + itemStack.getType().name() + ". Ignoring it.");
            }
        });
        itemStack.setItemMeta(meta);
    }

    @Override
    public List<String> getLore() {
        return itemStack.hasItemMeta() ? itemStack.getItemMeta().getLore() : null;
    }

    @Override
    public void setLore(List<String> lore) {
        if (lore == null) lore = new ArrayList<>();
        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;

        lore.replaceAll(s -> plugin.getUtils().colorize(s));
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
    }

    @Override
    public String getDisplayName() {
        return itemStack.getItemMeta() == null ? "" : itemStack.getItemMeta().getDisplayName();
    }

    @Override
    public void setDisplayName(String displayName) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return;

        meta.setDisplayName(plugin.getUtils().colorize(displayName));
        itemStack.setItemMeta(meta);
    }

    @Override
    public List<String> getItemFlags() {
        return itemStack.getItemMeta().getItemFlags().stream()
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public void setItemFlags(List<String> itemFlags) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return;

        meta.removeItemFlags(meta.getItemFlags().toArray(ItemFlag[]::new));

        itemFlags.forEach(flag -> {
            try {
                meta.addItemFlags(ItemFlag.valueOf(flag));
            } catch (Exception e) {
                plugin.getLogger().error("Flag with name '" + flag + "' could not be resolved for item " + itemStack.getType().name() + ". Ignoring it.");
            }
        });
        itemStack.setItemMeta(meta);
    }

    @Override
    public void addItemFlag(String flag) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return;

        try {
            meta.addItemFlags(ItemFlag.valueOf(flag));
        } catch (Exception e) {
            plugin.getLogger().error("Flag with name '" + flag + "' could not be resolved for item " + itemStack.getType().name() + ". Ignoring it.");
        }
        itemStack.setItemMeta(meta);
    }

    @Override
    public void addAllItemFlags() {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return;

        meta.addItemFlags(ItemFlag.values());
        itemStack.setItemMeta(meta);
    }

    @Override
    public short getDurability() {
        if (itemStack instanceof Damageable) {
            return (short) ((Damageable) itemStack).getDamage();
        }

        return 0;
    }

    @Override
    public void setDurability(short durability) {
        if (itemStack instanceof Damageable) {
            ((Damageable) itemStack).setDamage(durability);
        }
    }

    @Override
    public byte getDamage() {
        return itemStack.getData().getData();
    }

    @Override
    public void setDamage(byte damage) {
        itemStack.getData().setData(damage);
    }

    @Override
    public java.awt.Color getColor() {
        Color color = null;
        if (itemStack.getItemMeta() instanceof LeatherArmorMeta) {
            color = ((LeatherArmorMeta) itemStack.getItemMeta()).getColor();
        } else if (itemStack.getItemMeta() instanceof PotionMeta) {
            color = ((PotionMeta) itemStack.getItemMeta()).getColor();
        }

        return color == null ? null : new java.awt.Color(color.getRed(), color.getGreen(), color.getBlue());
    }

    @Override
    public void setColor(java.awt.Color color) {
        final Color bukkitColor = Color.fromRGB(color.getRed(), color.getGreen(), color.getBlue());

        if (itemStack.getItemMeta() instanceof LeatherArmorMeta) {
            ((LeatherArmorMeta) itemStack.getItemMeta()).setColor(bukkitColor);
        } else if (itemStack.getItemMeta() instanceof PotionMeta) {
            ((PotionMeta) itemStack.getItemMeta()).setColor(bukkitColor);
        }
    }

    @Override
    @Nullable
    public String getSkullOwner() {
        if (itemStack.getItemMeta() instanceof SkullMeta) {
            return ((SkullMeta) itemStack.getItemMeta()).getOwner();
        }
        return null;
    }

    @Override
    public void setSkullOwner(String owner) {
        if (itemStack.getItemMeta() instanceof SkullMeta) {
            ((SkullMeta) itemStack.getItemMeta()).setOwner(owner);
        }
    }

    private Material validateMaterial(String material) {
        try {
            return Material.valueOf(material);
        } catch (Exception e) {
            this.plugin.getLogger().error("Invalid material found for item: " + material);
            return Material.STONE;
        }
    }

    public ItemStack getBukkitItem() {
        return this.itemStack;
    }

}
