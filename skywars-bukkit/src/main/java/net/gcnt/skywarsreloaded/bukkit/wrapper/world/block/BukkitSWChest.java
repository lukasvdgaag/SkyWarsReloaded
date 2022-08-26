package net.gcnt.skywarsreloaded.bukkit.wrapper.world.block;

import net.gcnt.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.utils.BukkitItem;
import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.wrapper.world.block.SWChest;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

public class BukkitSWChest extends BukkitSWBlock implements SWChest {

    private final Chest chest;

    public BukkitSWChest(BukkitSkyWarsReloaded plugin, Chest chest) {
        super(plugin, chest.getBlock());
        this.chest = chest;
    }

    @Override
    public Item[] getContents() {
        final ItemStack[] bukkitContents = chest.getBlockInventory().getContents();
        final Item[] contents = new Item[bukkitContents.length];

        for (int i = 0; i < bukkitContents.length; i++) {
            contents[i] = BukkitItem.fromBukkit(this.plugin, bukkitContents[i]);
        }

        return contents;
    }

    @Override
    public int getSize() {
        return chest.getBlockInventory().getSize();
    }

    @Override
    public void setContents(Item[] items) {
        ItemStack[] parsedItems = new ItemStack[items.length];

        for (int i = 0; i < items.length; i++) {
            Item item = items[i];
            if (item instanceof BukkitItem) parsedItems[i] = ((BukkitItem) item).getBukkitItem();
            else parsedItems[i] = null;
        }

        chest.getBlockInventory().setContents(parsedItems);
    }

    @Override
    public void setItem(int slot, Item item) {
        if (item instanceof BukkitItem) chest.getBlockInventory().setItem(slot, ((BukkitItem) item).getBukkitItem());
    }

    @Override
    public void clearContents() {
        chest.getBlockInventory().clear();
    }
}
