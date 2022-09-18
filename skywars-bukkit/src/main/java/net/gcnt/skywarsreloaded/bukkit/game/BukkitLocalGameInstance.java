package net.gcnt.skywarsreloaded.bukkit.game;

import net.gcnt.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.wrapper.world.BukkitSWWorld;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.gameinstance.AbstractLocalGameInstance;
import net.gcnt.skywarsreloaded.wrapper.world.SWWorld;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;

public class BukkitLocalGameInstance extends AbstractLocalGameInstance {

    public BukkitLocalGameInstance(BukkitSkyWarsReloaded plugin, String id, GameTemplate gameData) {
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
    public void makeReadyForEditing() {
        World world = getBukkitWorld();
        plugin.getWorldLoader().updateWorldBorder(this);
        // Place beacons for each player spawn point
        getTemplate().getTeamSpawnpoints().forEach(swCoords ->
                swCoords.forEach(swCoord ->
                        world.getBlockAt(swCoord.x(), swCoord.y(), swCoord.z()).setType(Material.BEACON)));
    }
}
