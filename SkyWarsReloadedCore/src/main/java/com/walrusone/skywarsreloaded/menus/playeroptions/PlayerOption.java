package com.walrusone.skywarsreloaded.menus.playeroptions;

import org.bukkit.inventory.ItemStack;

public abstract class PlayerOption
        implements Comparable<PlayerOption> {
    protected ItemStack item;
    protected int level;
    protected int cost;
    protected String key;
    protected String name;
    protected int position;
    protected int page;
    protected int menuSize;

    public PlayerOption() {
    }

    public abstract String getPermission();

    public abstract String getMenuName();

    public abstract String getPurchaseMessage();

    public abstract String getUseMessage();

    public abstract void setEffect(com.walrusone.skywarsreloaded.managers.PlayerStat paramPlayerStat);

    public abstract String getUseLore();

    public ItemStack getItem() {
        return item;
    }

    public int getLevel() {
        return level;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int x) {
        position = x;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int x) {
        page = x;
    }

    public int getMenuSize() {
        return menuSize;
    }

    public void setMenuSize(int x) {
        menuSize = x;
    }

    public int compareTo(@javax.annotation.Nonnull PlayerOption o) {
        return Integer.compare(level, level);
    }
}
