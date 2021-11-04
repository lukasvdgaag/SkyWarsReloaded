package net.gcnt.skywarsreloaded.game;

import net.gcnt.skywarsreloaded.utils.Coord;
import net.gcnt.skywarsreloaded.wrapper.SWPlayer;

import java.util.ArrayList;
import java.util.List;

public class GameTeam implements Team {

    private final GameData game;
    private final String name;
    private final TeamColor color;
    private final List<TeamSpawn> spawns;
    private List<SWPlayer> aliveplayers;
    private List<SWPlayer> players;

    public GameTeam(GameData game, String name, TeamColor color, List<Coord> spawns) {
        this.game = game;
        this.name = name;
        this.color = color;
        this.spawns = new ArrayList<>();
        for (Coord coord : spawns) {
            this.spawns.add(new TeamSpawn(this, coord));
        }
    }

    @Override
    public TeamColor getColor() {
        return this.color;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean isMember(SWPlayer player) {
        return this.players.contains(player);
    }

    @Override
    public void addPlayers(SWPlayer... players) {
        if (players == null) return;

        playerLoop:
        for (SWPlayer swp : players) {
            for (TeamSpawn spawn : this.spawns) {
                if (!spawn.isOccupied()) {
                    spawn.addPlayer(swp);
                    continue playerLoop;
                }
            }
        }
        // todo add better support for multiple players fitting in the same team.
    }

    @Override
    public void removePlayer(SWPlayer player) {
        // todo remove the player.
    }

    @Override
    public void rejoin(SWPlayer player) {
        // todo re-add the player.
    }

    @Override
    public List<SWPlayer> getMembers() {
        return this.players;
    }

    @Override
    public GameData getArena() {
        return this.game;
    }

    @Override
    public int getSize() {
        return this.players.size();
    }

    @Override
    public int getAliveSize() {
        return this.aliveplayers.size();
    }

    @Override
    public boolean isEliminated() {
        return this.aliveplayers.isEmpty();
    }

    @Override
    public List<TeamSpawn> getSpawns() {
        return this.spawns;
    }

    @Override
    public void resetData() {
        players = new ArrayList<>();
        aliveplayers = new ArrayList<>();
    }
}
