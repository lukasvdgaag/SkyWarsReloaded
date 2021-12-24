package net.gcnt.skywarsreloaded.bukkit.game.cages;

import net.gcnt.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.game.BukkitGameWorld;
import net.gcnt.skywarsreloaded.game.TeamSpawn;
import net.gcnt.skywarsreloaded.game.cages.AbstractNormalCage;
import net.gcnt.skywarsreloaded.game.cages.NormalCageShape;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.CompletableFuture;

public class BukkitNormalCage extends AbstractNormalCage {

    private BukkitSkyWarsReloaded main;

    public BukkitNormalCage(BukkitSkyWarsReloaded mainIn, TeamSpawn spawn) {
        super(mainIn, spawn);
        this.main = mainIn;
    }

    @Override
    public CompletableFuture<Boolean> placeCage(String cage) {
        Plugin skywarsPlugin = this.main.getBukkitPlugin();

        CompletableFuture<Boolean> future = new CompletableFuture<>();
        skywarsPlugin.getServer().getScheduler().runTask(skywarsPlugin, () -> future.complete(placeCageNow(cage)));

        return future;
    }

    @Override
    public boolean placeCageNow(String cage) {
        removeCage(cage);

        NormalCageShape shape = NormalCageShape.fromString(cage);
        if (shape == null) return false;

        final World world = ((BukkitGameWorld) getSpawn().getTeam().getGameWorld()).getBukkitWorld();
        final SWCoord baseCoord = getSpawn().getLocation();

        for (SWCoord toAdd : shape.getLocations()) {
            SWCoord loc = baseCoord.clone().add(toAdd);
            world.getBlockAt(loc.x(), loc.y(), loc.z()).setType(Material.GLASS);
            // todo get cage material from config?
        }

        setPlaced(true);
        return true;
    }

    @Override
    public CompletableFuture<Boolean> removeCage(String cage) {
        Plugin skywarsPlugin = this.main.getBukkitPlugin();

        CompletableFuture<Boolean> future = new CompletableFuture<>();
        skywarsPlugin.getServer().getScheduler().runTask(skywarsPlugin, () -> future.complete(removeCageNow(cage)));

        return future;
    }

    @Override
    public boolean removeCageNow(String cage) {
        if (!isPlaced()) return false;
        NormalCageShape shape = NormalCageShape.fromString(cage);
        if (shape == null) return false;

        final World world = ((BukkitGameWorld) getSpawn().getTeam().getGameWorld()).getBukkitWorld();
        final SWCoord baseCoord = getSpawn().getLocation();

        for (SWCoord toAdd : shape.getLocations()) {
            SWCoord loc = baseCoord.clone().add(toAdd);
            world.getBlockAt(loc.x(), loc.y(), loc.z()).setType(Material.AIR);
        }

        setPlaced(false);
        return true;
    }
}
