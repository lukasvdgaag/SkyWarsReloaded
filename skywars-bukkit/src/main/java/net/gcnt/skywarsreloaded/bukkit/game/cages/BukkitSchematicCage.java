package net.gcnt.skywarsreloaded.bukkit.game.cages;

import com.sk89q.worldedit.bukkit.BukkitWorld;
import net.gcnt.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.TeamSpawn;
import net.gcnt.skywarsreloaded.game.cages.AbstractSchematicCage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.CompletableFuture;

public class BukkitSchematicCage extends AbstractSchematicCage {

    private BukkitSkyWarsReloaded main;

    public BukkitSchematicCage(BukkitSkyWarsReloaded mainIn, TeamSpawn spawn) {
        super(mainIn, spawn);
        this.main = mainIn;
    }

    @Override
    public CompletableFuture<Boolean> placeCage(String cageId) {
        Plugin skywarsPlugin = this.main.getPlugin();

        CompletableFuture<Boolean> future = new CompletableFuture<>();
        skywarsPlugin.getServer().getScheduler().runTask(skywarsPlugin, () -> future.complete(placeCageNow(cageId)));

        return future;
    }


    @Override
    public CompletableFuture<Boolean> removeCage(String cage) {
        Plugin skywarsPlugin = this.main.getPlugin();

        CompletableFuture<Boolean> future = new CompletableFuture<>();
        skywarsPlugin.getServer().getScheduler().runTask(skywarsPlugin, () -> future.complete(removeCageNow()));

        return future;
    }

    @Override
    public BukkitWorld getWorldEditWorldByName(String name) {
        return new BukkitWorld(Bukkit.getWorld(name));
    }
}
