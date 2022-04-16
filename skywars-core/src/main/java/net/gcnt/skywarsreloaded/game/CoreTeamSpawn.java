package net.gcnt.skywarsreloaded.game;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.data.player.SWPlayerData;
import net.gcnt.skywarsreloaded.game.cages.Cage;
import net.gcnt.skywarsreloaded.game.cages.MaterialCage;
import net.gcnt.skywarsreloaded.game.cages.SchematicCage;
import net.gcnt.skywarsreloaded.game.cages.cages.CoreMaterialTeamCage;
import net.gcnt.skywarsreloaded.game.cages.cages.CoreSchematicTeamCage;
import net.gcnt.skywarsreloaded.utils.CoreSWCCompletableFuture;
import net.gcnt.skywarsreloaded.utils.SWCompletableFuture;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class CoreTeamSpawn implements TeamSpawn {

    private final SkyWarsReloaded plugin;
    private final GameTeam team;
    private final SWCoord location;
    private TeamCage teamCage;
    private List<GamePlayer> players;
    private String oldCageDesign;
    private String cageDesign;

    public CoreTeamSpawn(SkyWarsReloaded plugin, GameTeam team, SWCoord location) {
        this.plugin = plugin;
        this.team = team;
        this.location = location;
        this.players = new ArrayList<>();
        this.cageDesign = "default";
        this.oldCageDesign = null;
    }

    @Override
    public boolean isOccupied() {
        return this.players.size() >= this.team.getGameWorld().getTemplate().getTeamSize();
    }

    @Override
    public SWCoord getLocation() {
        return this.location;
    }

    /*
    Get methods
     */

    @Override
    public List<GamePlayer> getPlayers() {
        return this.players;
    }

    @Override
    public void addPlayer(GamePlayer player) {
        if (isOccupied()) return;

        this.players.add(player);
        this.determineCageDesign();

        // teleport?
        // todo update cage here?
    }

    @Override
    public void removePlayer(GamePlayer player) {

    }

    @Override
    public GameTeam getTeam() {
        return team;
    }

    @Override
    public void determineCageDesign() {
        List<String> chooseCageFrom = new ArrayList<>();

        if (this.oldCageDesign == null) {
            this.oldCageDesign = this.cageDesign;
        }

        for (GamePlayer gp : getPlayers()) {
            SWPlayer swp = gp.getSWPlayer();
            if (!swp.isOnline()) continue;
            SWPlayerData swpd = swp.getPlayerData();

            final int teamSize = getTeam().getGameWorld().getTemplate().getTeamSize();
            String cage = teamSize == 1 ? swpd.getSoloCage() : swpd.getTeamCage();

            // todo probably also check if the cage exists here.
            if (cage == null || cage.isEmpty()) cage = "default";

            chooseCageFrom.add(cage);
        }

        if (chooseCageFrom.isEmpty()) this.cageDesign = "default";
        else this.cageDesign = chooseCageFrom.get(ThreadLocalRandom.current().nextInt(chooseCageFrom.size()));
    }

    @Override
    public SWCompletableFuture<Boolean> updateCage() {

        determineCageDesign();
        // this could cause issues if the shape of a normal cage is different from the previously placed cage.
        Cage cage = plugin.getCageManager().getCageById(this.cageDesign);
        if (cage == null) cage = plugin.getCageManager().getCageById("default");

        if (this.cageDesign.equals(this.oldCageDesign) && teamCage != null) {
            return CoreSWCCompletableFuture.completedFuture(plugin, true);
        }

        // Create future to complete when all the tasks below are done
        SWCompletableFuture<Boolean> updateFuture = new CoreSWCCompletableFuture<>(plugin);

        // Prevent players from falling
        this.players.forEach((player) -> player.getSWPlayer().freeze());

        // Remove the cage
        SWCompletableFuture<Boolean> removeFuture;
        if (teamCage == null) removeFuture = CoreSWCCompletableFuture.completedFuture(plugin, true);
        else removeFuture = teamCage.removeCage();

        // After cage removal
        final Cage finalCage = cage;
        removeFuture.thenRun(() -> {
            if (finalCage instanceof SchematicCage) {
                this.teamCage = new CoreSchematicTeamCage(plugin, this, (SchematicCage) finalCage);
            } else if (finalCage instanceof MaterialCage) {
                this.teamCage = new CoreMaterialTeamCage(plugin, this, (MaterialCage) finalCage);
            } else {
                // todo: debug
                System.out.println("NOT ANY OF THE TYPES " + finalCage.getClass().getName());
            }

            SWCompletableFuture<Boolean> placeFuture = this.teamCage.placeCage();

            // Release players' freeze & complete after the cage is placed
            placeFuture.thenRunSync(() -> {
                this.players.forEach((player) -> player.getSWPlayer().unfreeze());

                // END
                updateFuture.complete(true);
            });
        });

        return updateFuture;
    }

    @Override
    public void removeCage() {
        if (!teamCage.isPlaced()) return;
        teamCage.removeCage();
    }

    @Override
    public TeamCage getCage() {
        return this.teamCage;
    }

    @Override
    public void setCage(TeamCage cage) {
        this.teamCage = cage;
        // todo update cage here?
    }
}
