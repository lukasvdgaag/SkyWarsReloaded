package com.walrusone.skywarsreloaded.menus.gameoptions.objects;

import javax.annotation.Nonnull;
import org.bukkit.inventory.ItemStack;

public class ChestItem implements Comparable<ChestItem>
{
  private ItemStack item;
  private int chance;
  
  public ChestItem(ItemStack item, int chance)
  {
    this.item = item;
    this.chance = chance;
  }
  
  public ItemStack getItem() {
    return item;
  }
  
  public int getChance() {
    return chance;
  }
  
  public int compareTo(@Nonnull ChestItem o)
  {
    return Integer.compare(chance, chance);
  }
}
