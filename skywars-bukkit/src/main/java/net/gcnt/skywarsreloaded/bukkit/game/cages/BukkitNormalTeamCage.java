package net.gcnt.skywarsreloaded.bukkit.game.cages;

import net.gcnt.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.game.BukkitGameWorld;
import net.gcnt.skywarsreloaded.bukkit.utils.BukkitItem;
import net.gcnt.skywarsreloaded.data.player.SWPlayerData;
import net.gcnt.skywarsreloaded.game.TeamSpawn;
import net.gcnt.skywarsreloaded.game.cages.AbstractNormalTeamCage;
import net.gcnt.skywarsreloaded.game.cages.NormalCageShape;
import net.gcnt.skywarsreloaded.utils.CoreSWCCompletableFuture;
import net.gcnt.skywarsreloaded.utils.SWCompletableFuture;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;
import net.gcnt.skywarsreloaded.wrapper.world.SWWorld;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class BukkitNormalTeamCage extends AbstractNormalTeamCage {

    private final BukkitSkyWarsReloaded plugin;

    public BukkitNormalTeamCage(BukkitSkyWarsReloaded mainIn, TeamSpawn spawn) {
        super(mainIn, spawn);
        this.plugin = mainIn;
    }

    @Override
    public SWCompletableFuture<Boolean> placeCage(String cage) {
        Plugin skywarsPlugin = this.plugin.getBukkitPlugin();

        HashMap<UUID, String> cages = new HashMap<>();
        spawn.getPlayers().forEach(gamePlayer -> {
            final SWPlayer playerByUUID = plugin.getPlayerManager().getPlayerByUUID(gamePlayer.getSWPlayer().getUuid());
            final SWPlayerData playerData = playerByUUID.getPlayerData();
            cages.put(playerByUUID.getUuid(), spawn.getTeam().getGameWorld().getTemplate().getTeamSize() == 1 ? playerData.getSoloCage() : playerData.getTeamCage());
        });

        String selected = (String) cages.values().toArray()[ThreadLocalRandom.current().nextInt(cages.size())];
        if (selected == null) selected = "GLASS";
        // todo get cage object from cage identifier (selected).

        SWCompletableFuture<Boolean> future = new CoreSWCCompletableFuture<>(plugin);
        String finalSelected = selected;
        skywarsPlugin.getServer().getScheduler().runTask(skywarsPlugin, () -> future.complete(placeCageNow(cage, finalSelected)));

        return future;
    }

    @Override
    public boolean placeCageNow(String cage, String material) {
        removeCage(cage);

        NormalCageShape shape = NormalCageShape.fromString(cage);
        if (shape == null) return false;

        final SWCoord baseCoord = getSpawn().getLocation();
        final SWWorld world = spawn.getTeam().getGameWorld().getWorld();

        for (SWCoord toAdd : shape.getLocations()) {
            SWCoord loc = baseCoord.clone().add(toAdd);

            world.setBlockAt(loc, new BukkitItem(plugin, material));
            // todo get cage material from config?
        }

        setPlaced(true);
        return true;
    }

    @Override
    public SWCompletableFuture<Boolean> removeCage(String cage) {
        Plugin skywarsPlugin = this.plugin.getBukkitPlugin();

        SWCompletableFuture<Boolean> future = new CoreSWCCompletableFuture<>(plugin);
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
