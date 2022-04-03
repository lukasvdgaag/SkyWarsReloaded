package net.gcnt.skywarsreloaded.game.cages.cages;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.data.player.SWPlayerData;
import net.gcnt.skywarsreloaded.game.TeamCage;
import net.gcnt.skywarsreloaded.game.TeamSpawn;
import net.gcnt.skywarsreloaded.game.cages.MaterialCage;
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
        System.out.println("placing cage (placeCaeg)");
        HashMap<UUID, String> cages = new HashMap<>();
        spawn.getPlayers().forEach(gamePlayer -> {
            System.out.println("placing cage (placeCaeg) player: " + gamePlayer.getSWPlayer().getName());
            final SWPlayer playerByUUID = plugin.getPlayerManager().getPlayerByUUID(gamePlayer.getSWPlayer().getUuid());
            final SWPlayerData playerData = playerByUUID.getPlayerData();
            cages.put(playerByUUID.getUuid(), spawn.getTeam().getGameWorld().getTemplate().getTeamSize() == 1 ? playerData.getSoloCage() : playerData.getTeamCage());
        });

        String selected = (String) cages.values().toArray()[ThreadLocalRandom.current().nextInt(cages.size())];
        if (selected == null) selected = "GLASS";
        // todo get cage object from cage identifier (selected).

        SWCompletableFuture<Boolean> future = new CoreSWCCompletableFuture<>(plugin);
        String finalSelected = selected;

        plugin.getScheduler().runSync(() -> {
            System.out.println("completing cage placement for " + spawn.getTeam().getName() + " with " + finalSelected);
            future.complete(placeCageNow());
        });
        return future;
    }

    public boolean placeCageNow() {
        NormalCageShape shape = cage.getShape();
        if (shape == null) {
            System.out.println("shape is null");
            return false;
        }

        final SWCoord baseCoord = getSpawn().getLocation();
        baseCoord.setWorld(spawn.getTeam().getGameWorld().getWorld());

        System.out.println("placing cage at " + baseCoord);
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
        NormalCageShape shape = NormalCageShape.fromString(cage);
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
