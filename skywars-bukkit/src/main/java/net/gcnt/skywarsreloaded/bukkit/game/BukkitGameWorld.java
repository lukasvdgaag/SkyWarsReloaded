package net.gcnt.skywarsreloaded.bukkit.game;

import net.gcnt.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.utils.BukkitItem;
import net.gcnt.skywarsreloaded.bukkit.wrapper.world.BukkitSWWorld;
import net.gcnt.skywarsreloaded.game.AbstractGameWorld;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.chest.SWChestTier;
import net.gcnt.skywarsreloaded.game.state.WaitingStateHandler;
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
        return Bukkit.getWorld(this.getWorldName());
    }

    @Override
    public SWWorld getWorld() {
        return new BukkitSWWorld((BukkitSkyWarsReloaded) plugin, getBukkitWorld());
    }

    @Override
    public void readyForGame() {
        startScheduler();
        gameTemplate.getTeamSpawnpoints().forEach(swCoords -> swCoords.forEach(swCoord -> getWorld().setBlockAt(swCoord, (Item) null)));
        final WaitingStateHandler handler = new WaitingStateHandler(plugin, this);

        scheduler.setGameStateHandler(handler);
        setState(handler.getBeginningWaitingState(getTemplate()));
    }

    @Override
    public void readyForEditing() {
        World world = getBukkitWorld();
        plugin.getWorldLoader().updateWorldBorder(this);
        getTemplate().getTeamSpawnpoints().forEach(swCoords -> swCoords.forEach(swCoord -> world.getBlockAt(swCoord.x(), swCoord.y(), swCoord.z()).setType(Material.BEACON)));
    }
}
