package net.gcnt.skywarsreloaded.game.cages;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.data.player.SWPlayerData;
import net.gcnt.skywarsreloaded.game.TeamCage;
import net.gcnt.skywarsreloaded.game.TeamSpawn;
import net.gcnt.skywarsreloaded.unlockable.cage.MaterialCage;
import net.gcnt.skywarsreloaded.game.cages.NormalCageShape;
import net.gcnt.skywarsreloaded.utils.CoreSWCCompletableFuture;
import net.gcnt.skywarsreloaded.utils.CoreSWCoord;
import net.gcnt.skywarsreloaded.utils.SWCompletableFuture;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class CoreMaterialTeamCage implements TeamCage {

    private final SkyWarsReloaded plugin;
    protected final TeamSpawn spawn;
    private final MaterialCage cage;

    private boolean placed;


    public CoreMaterialTeamCage(SkyWarsReloaded pluginIn, TeamSpawn spawn, MaterialCage cageIn) {
        this.plugin = pluginIn;
        this.spawn = spawn;
        this.cage = cageIn;
        this.placed = false;
    }

    @Override
    public TeamSpawn getSpawn() {
        return spawn;
    }

    @Override
    public SWCompletableFuture<Boolean> placeCage() {
        HashMap<UUID, String> cages = new HashMap<>();
        spawn.getPlayers().forEach(gamePlayer -> {
            final SWPlayer playerByUUID = plugin.getPlayerManager().getPlayerByUUID(gamePlayer.getSWPlayer().getUuid());
            final SWPlayerData playerData = playerByUUID.getPlayerData();

            String selectedCage = spawn.getTeam().getGameWorld().getTemplate().getTeamSize() == 1 ? playerData.getSoloCage() : playerData.getTeamCage();
            if (selectedCage == null || selectedCage.isEmpty()) selectedCage = "default";

            cages.put(playerByUUID.getUuid(), selectedCage);
        });

        String selected = (String) cages.values().toArray()[ThreadLocalRandom.current().nextInt(cages.size())];
        if (selected == null) selected = "GLASS";
        // todo get cage object from cage identifier (selected).

        SWCompletableFuture<Boolean> future = new CoreSWCCompletableFuture<>(plugin);
        String finalSelected = selected;

        plugin.getScheduler().runSync(() -> {
            future.complete(placeCageNow());
        });
        return future;
    }

    public boolean placeCageNow() {
        NormalCageShape shape = cage.getShape();
        if (shape == null) {
            return false;
        }

        final SWCoord baseCoord = getSpawn().getLocation();
        baseCoord.setWorld(spawn.getTeam().getGameWorld().getWorld());

        plugin.getCageManager().placeCage(this.cage, baseCoord);

        setPlaced(true);
        return true;
    }

    @Override
    public SWCompletableFuture<Boolean> removeCage() {

        SWCompletableFuture<Boolean> future = new CoreSWCCompletableFuture<>(plugin);
        plugin.getScheduler().runSync(() -> future.complete(removeCageNow()));

        return future;
    }

    public boolean removeCageNow() {
        if (!isPlaced()) return false;
        NormalCageShape shape = cage.getShape();
        if (shape == null) return false;

        final SWCoord baseCoord = getSpawn().getLocation();

        for (SWCoord toAdd : shape.getLocations()) {
            SWCoord loc = baseCoord.clone().add(toAdd);
            getSpawn().getTeam().getGameWorld().getWorld().setBlockAt(new CoreSWCoord(loc.x(), loc.y(), loc.z()), "AIR");
        }

        setPlaced(false);
        return true;
    }

    @Override
    public boolean isPlaced() {
        return placed;
    }

    @Override
    public void setPlaced(boolean placed) {
        this.placed = placed;
    }

}
