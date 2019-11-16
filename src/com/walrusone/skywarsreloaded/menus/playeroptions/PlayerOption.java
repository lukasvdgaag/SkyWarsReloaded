package com.walrusone.skywarsreloaded.menus.playeroptions;

import org.bukkit.inventory.ItemStack;

public abstract class PlayerOption
  implements Comparable<PlayerOption>
{
  protected ItemStack item;
  protected int level;
  protected int cost;
  protected String key;
  
  public PlayerOption() {}
  
  public abstract String getPermission();
  
  public abstract String getMenuName();
  
  public abstract String getPurchaseMessage();
  
  public abstract String getUseMessage();
  
  public abstract void setEffect(com.walrusone.skywarsreloaded.managers.PlayerStat paramPlayerStat);
  
  public abstract String getUseLore();
  
  public ItemStack getItem() { return item; }
  
  protected String name;
  
  public int getLevel() { return level; }
  
  protected int position;
  
  public String getKey() { return key; }
  
  protected int page;
  protected int menuSize;
  public String getName() { return name; }
  
  public int getCost()
  {
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
  
  public int compareTo(@javax.annotation.Nonnull PlayerOption o)
  {
    return Integer.compare(level, level);
  }
}
