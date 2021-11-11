package net.gcnt.skywarsreloaded.utils;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractItem implements Item {

    public String material;
    public int amount;
    public List<String> enchantments;
    public List<String> lore;
    public String displayName;
    public List<String> itemFlags;
    public short durability;
    public byte damage;

    public AbstractItem(String material) {
        this.material = material;
        amount = 1;
        enchantments = new ArrayList<>();
        lore = new ArrayList<>();
        displayName = null;
        itemFlags = new ArrayList<>();
        durability = -1;
        damage = 0;
    }

    @Override
    public String getMaterial() {
        return material;
    }

    @Override
    public void setMaterial(String material) {
        this.material = material;
        clearCachedItem();
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public void setAmount(int amount) {
        this.amount = amount;
        clearCachedItem();
    }

    @Override
    public List<String> getEnchantments() {
        return enchantments;
    }

    @Override
    public void setEnchantments(List<String> enchantments) {
        this.enchantments = enchantments;
        clearCachedItem();
    }

    @Override
    public List<String> getLore() {
        return lore;
    }

    @Override
    public void setLore(List<String> lore) {
        this.lore = lore;
        clearCachedItem();
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
        clearCachedItem();
    }

    @Override
    public List<String> getItemFlags() {
        return itemFlags;
    }

    @Override
    public void setItemFlags(List<String> itemFlags) {
        this.itemFlags = itemFlags;
        clearCachedItem();
    }

    @Override
    public short getDurability() {
        return durability;
    }

    @Override
    public void setDurability(short durability) {
        this.durability = durability;
        clearCachedItem();
    }

    @Override
    public byte getDamage() {
        return damage;
    }

    @Override
    public void setDamage(byte damage) {
        this.damage = damage;
        clearCachedItem();
    }

    @Override
    public abstract void clearCachedItem();

    @Override
    public abstract void cacheItem();

}
