package net.gcnt.skywarsreloaded.game;

import net.gcnt.skywarsreloaded.data.player.SWPlayerData;
import net.gcnt.skywarsreloaded.utils.Coordinate;
import net.gcnt.skywarsreloaded.wrapper.SWPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class CoreTeamSpawn implements TeamSpawn {

    private final GameTeam team;
    private final Coordinate location;
    private TeamCage cage;
    private List<GamePlayer> players;
    private String oldCageDesign;
    private String cageDesign;

    public CoreTeamSpawn(GameTeam team, Coordinate location) {
        this.team = team;
        this.location = location;
        this.players = new ArrayList<>();
        this.cageDesign = "default";
        this.oldCageDesign = null;
    }

    @Override
    public boolean isOccupied() {
        return players.size() < this.team.getGameWorld().getGame().getTeamSize();
    }

    @Override
    public Coordinate getLocation() {
        return location;
    }

    /*
    Get methods
     */

    @Override
    public List<GamePlayer> getPlayers() {
        return players;
    }

    @Override
    public void addPlayer(GamePlayer player) {
        if (isOccupied()) return;

        players.add(player);
        determineCageDesign();
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

            final int teamSize = getTeam().getGameWorld().getGame().getTeamSize();
            String cage = teamSize == 1 ? swpd.getSoloCage() : swpd.getTeamCage();
            // todo probably also check if the cage exists here.
            if (cage == null || cage.isEmpty()) cage = "default";

            // todo maybe check if cage isn't already in the list? idk, prob not
            chooseCageFrom.add(cage);
        }

        if (chooseCageFrom.isEmpty()) this.cageDesign = "default";
        this.cageDesign = chooseCageFrom.get(ThreadLocalRandom.current().nextInt(chooseCageFrom.size()));
    }

    @Override
    public void updateCage() {
        // this could cause issues if the shape of a normal cage is different from the previously placed cage.
        if (oldCageDesign != null && !oldCageDesign.equals(cageDesign)) cage.removeCage(this.oldCageDesign);
        // todo check if cage should be placed already.
        cage.placeCage(this.cageDesign);
    }

    @Override
    public void removeCage() {
        if (cage.isPlaced()) return;
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