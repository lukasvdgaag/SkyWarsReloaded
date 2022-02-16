package net.gcnt.skywarsreloaded.game;

import net.gcnt.skywarsreloaded.data.player.SWPlayerData;
import net.gcnt.skywarsreloaded.utils.SWCompletableFuture;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

public class CoreTeamSpawn implements TeamSpawn {

    private final GameTeam team;
    private final SWCoord location;
    private TeamCage cage;
    private List<GamePlayer> players;
    private String oldCageDesign;
    private String cageDesign;

    public CoreTeamSpawn(GameTeam team, SWCoord location) {
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

            // todo maybe check if cage isn't already in the list? idk, prob not
            chooseCageFrom.add(cage);
        }

        if (chooseCageFrom.isEmpty()) this.cageDesign = "default";
        else this.cageDesign = chooseCageFrom.get(ThreadLocalRandom.current().nextInt(chooseCageFrom.size()));

        // todo load cage object from memory from some kind of cage manager.

    }

    @Override
    public SWCompletableFuture<Boolean> updateCage() {
        determineCageDesign();
        // this could cause issues if the shape of a normal cage is different from the previously placed cage.
        if (oldCageDesign != null && !oldCageDesign.equals(cageDesign)) cage.removeCage(this.oldCageDesign);
        // todo check if cage should be placed already.
        return cage.placeCage(this.cageDesign);
    }

    @Override
    public void removeCage() {
        if (!cage.isPlaced()) return;
        cage.removeCage(this.cageDesign);
    }

    @Override
    public TeamCage getCage() {
        return this.cage;
    }

    @Override
    public void setCage(TeamCage cage) {
        this.cage = cage;
        // todo update cage here?
    }
}
