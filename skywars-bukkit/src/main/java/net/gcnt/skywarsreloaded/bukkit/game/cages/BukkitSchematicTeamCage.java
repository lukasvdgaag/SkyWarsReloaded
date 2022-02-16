package net.gcnt.skywarsreloaded.bukkit.game.cages;

import com.sk89q.worldedit.bukkit.BukkitWorld;
import net.gcnt.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.TeamSpawn;
import net.gcnt.skywarsreloaded.game.cages.AbstractSchematicTeamCage;
import net.gcnt.skywarsreloaded.utils.CoreSWCCompletableFuture;
import net.gcnt.skywarsreloaded.utils.SWCompletableFuture;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class BukkitSchematicTeamCage extends AbstractSchematicTeamCage {

    private final BukkitSkyWarsReloaded plugin;

    public BukkitSchematicTeamCage(BukkitSkyWarsReloaded mainIn, TeamSpawn spawn) {
        super(mainIn, spawn);
        this.plugin = mainIn;
    }

    @Override
    public SWCompletableFuture<Boolean> placeCage(String cageId) {
        Plugin skywarsPlugin = this.plugin.getBukkitPlugin();

        SWCompletableFuture<Boolean> future = new CoreSWCCompletableFuture<>(plugin);
        skywarsPlugin.getServer().getScheduler().runTask(skywarsPlugin, () -> future.complete(placeCageNow(cageId)));

        return future;
    }

    @Override
    public SWCompletableFuture<Boolean> removeCage(String cage) {
        Plugin skywarsPlugin = this.plugin.getBukkitPlugin();

        SWCompletableFuture<Boolean> future = new CoreSWCCompletableFuture<>(plugin);
        skywarsPlugin.getServer().getScheduler().runTask(skywarsPlugin, () -> future.complete(removeCageNow()));

        return future;
    }

    @Override
    public BukkitWorld getWorldEditWorldByName(String name) {
        return new BukkitWorld(Bukkit.getWorld(name));
    }
}
