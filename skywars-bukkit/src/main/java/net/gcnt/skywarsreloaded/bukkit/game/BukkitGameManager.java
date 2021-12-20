package net.gcnt.skywarsreloaded.bukkit.game;

import net.gcnt.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.CoreGameManager;
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
        addWorld(data, world);
        return world;
    }
}
