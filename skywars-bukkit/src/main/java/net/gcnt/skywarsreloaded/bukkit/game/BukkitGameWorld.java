package net.gcnt.skywarsreloaded.bukkit.game;

import net.gcnt.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.utils.BukkitItem;
import net.gcnt.skywarsreloaded.bukkit.wrapper.world.BukkitSWWorld;
import net.gcnt.skywarsreloaded.game.AbstractGameWorld;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.wrapper.world.SWWorld;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;

public class BukkitGameWorld extends AbstractGameWorld {

    public BukkitGameWorld(BukkitSkyWarsReloaded plugin, String id, GameTemplate gameData) {
        super(plugin, id, gameData);
    }

    public World getBukkitWorld() {
        return Bukkit.getWorld(worldName);
    }

    @Override
    public SWWorld getWorld() {
        return new BukkitSWWorld((BukkitSkyWarsReloaded) plugin, getBukkitWorld());
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

    @Override
    public void readyForEditing() {
        World world = getBukkitWorld();
        plugin.getWorldLoader().updateWorldBorder(this);
        getTemplate().getTeamSpawnpoints().forEach(swCoords -> swCoords.forEach(swCoord -> world.getBlockAt(swCoord.x(), swCoord.y(), swCoord.z()).setType(Material.BEACON)));
    }
}
