package net.gcnt.skywarsreloaded.bukkit.managers;

import net.gcnt.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.game.BukkitGameWorld;
import net.gcnt.skywarsreloaded.manager.CoreGameManager;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.GameWorld;

import java.util.UUID;

public class BukkitGameManager extends CoreGameManager {

    public BukkitGameManager(BukkitSkyWarsReloaded plugin) {
        super(plugin);
    }

    @Override
    public GameWorld createGameWorld(GameTemplate data) {
        GameWorld world = new BukkitGameWorld((BukkitSkyWarsReloaded) plugin, UUID.randomUUID().toString(), data);
        this.registerGameWorld(data, world);
        return world;
    }
}
