package net.gcnt.skywarsreloaded.bukkit.game;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.utils.BukkitItem;
import net.gcnt.skywarsreloaded.game.AbstractGameWorld;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;

public class BukkitGameWorld extends AbstractGameWorld {

    public BukkitGameWorld(SkyWarsReloaded plugin, String id, GameTemplate gameData) {
        super(plugin, id, gameData);
    }

    public World getBukkitWorld() {
        return Bukkit.getWorld(worldName);
    }

    @Override
    public void fillChest(SWCoord coord) {
        World world = getBukkitWorld();
        if (world == null) return;
        Block block = world.getBlockAt(coord.x(), coord.y(), coord.z());
        if (block == null) return;
        // todo load chunk if not yet loaded?
        if (!block.getChunk().isLoaded()) {
            block.getChunk().load();
            return;
        }

        Item[] items = generateChestLoot();

        if (block.getState() instanceof Chest chest) {
            Inventory inventory = chest.getBlockInventory();
            inventory.clear();
            for (int i = 0; i < items.length; i++) {
                inventory.setItem(i, ((BukkitItem) items[i]).getBukkitItem());
            }
        }
    }
}
