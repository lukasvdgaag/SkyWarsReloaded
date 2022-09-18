package net.gcnt.skywarsreloaded.bukkit.managers;

import net.gcnt.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.game.BukkitLocalGameInstance;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.gameinstance.GameInstance;
import net.gcnt.skywarsreloaded.manager.gameinstance.CoreLocalGameInstanceManager;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class BukkitGameInstanceManager extends CoreLocalGameInstanceManager {

    public BukkitGameInstanceManager(BukkitSkyWarsReloaded plugin) {
        super(plugin);
    }

    @Override
    public CompletableFuture<GameInstance> createGameWorld(GameTemplate data) {
        GameInstance world = new BukkitLocalGameInstance((BukkitSkyWarsReloaded) plugin, UUID.randomUUID().toString(), data);
        this.registerGameWorld(data, world);
        return CompletableFuture.completedFuture(world);
    }
}
