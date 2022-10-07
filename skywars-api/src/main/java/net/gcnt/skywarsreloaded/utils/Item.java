package net.gcnt.skywarsreloaded.utils;

import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;

public interface Item {

    String getMaterial();

    void setMaterial(String material);

    int getAmount();

    void setAmount(int amount);

    List<String> getEnchantments();

    void setEnchantments(List<String> enchs);

    List<String> getLore();

    void setLore(List<String> lore);

    String getDisplayName();

    void setDisplayName(String display);

    List<String> getItemFlags();

    void setItemFlags(List<String> itemFlags);

    void addItemFlag(String flag);

    void addAllItemFlags();

    short getDurability();

    void setDurability(short durability);

    byte getDamage();

    void setDamage(byte damage);

    Color getColor();

    void setColor(Color color);

    @Nullable
    String getSkullOwner();

    void setSkullOwner(String owner);

    Item withMessages(Item item);

}
