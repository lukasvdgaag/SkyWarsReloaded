package net.gcnt.skywarsreloaded.bukkit.game;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.CoreGameManager;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.GameWorld;

import java.util.UUID;

public class BukkitGameManager extends CoreGameManager {

    public BukkitGameManager(SkyWarsReloaded plugin) {
        super(plugin);
    }

    @Override
    public GameWorld createGameWorld(GameTemplate data) {
        return new BukkitGameWorld(plugin, UUID.randomUUID().toString(), data);
    }
}
