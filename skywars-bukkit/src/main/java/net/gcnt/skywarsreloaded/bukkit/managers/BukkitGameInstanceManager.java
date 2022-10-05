package net.gcnt.skywarsreloaded.bukkit.managers;

import net.gcnt.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.game.BukkitLocalGameInstance;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.gameinstance.LocalGameInstance;
import net.gcnt.skywarsreloaded.manager.gameinstance.CoreLocalGameInstanceManager;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class BukkitGameInstanceManager extends CoreLocalGameInstanceManager {

    public BukkitGameInstanceManager(BukkitSkyWarsReloaded plugin) {
        super(plugin);
    }

    @Override
    public CompletableFuture<LocalGameInstance> createGameWorld(GameTemplate data) {
        LocalGameInstance localGameInstance = new BukkitLocalGameInstance((BukkitSkyWarsReloaded) plugin, UUID.randomUUID(), data);
        this.registerGameWorld(localGameInstance);
        return CompletableFuture.completedFuture(localGameInstance);
    }
}
