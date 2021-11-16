package net.gcnt.skywarsreloaded.game;

import net.gcnt.skywarsreloaded.utils.CoreSWCoord;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CoreGameTeam implements GameTeam {

    private final GameWorld game;
    private final String name;
    private final TeamColor color;
    private final List<CoreTeamSpawn> spawns;
    private List<GamePlayer> players;

    public CoreGameTeam(GameWorld game, String name, TeamColor color, List<CoreSWCoord> spawns) {
        this.game = game;
        this.name = name;
        this.color = color;
        this.spawns = new ArrayList<>();
        this.players = new ArrayList<>();

        for (CoreSWCoord coord : spawns) {
            this.spawns.add(new CoreTeamSpawn(this, coord));
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
        for (GamePlayer gp : players) {
            if (gp.getSWPlayer().equals(player)) return true;
        }
        return false;
    }

    @Override
    public void addPlayers(SWPlayer... players) {
        if (players == null) return;

        // todo add the players.
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
    public List<GamePlayer> getPlayers() {
        return this.players;
    }

    @Override
    public GameWorld getGameWorld() {
        return this.game;
    }

    @Override
    public int getSize() {
        return this.players.size();
    }

    @Override
    public int getAliveSize() {
        return this.getAlivePlayers().size();
    }

    public List<GamePlayer> getAlivePlayers() {
        return players.stream().filter(GamePlayer::isAlive).collect(Collectors.toList());
    }

    @Override
    public List<? extends TeamSpawn> getSpawns() {
        return this.spawns;
    }

    @Override
    public boolean isEliminated() {
        return this.getAlivePlayers().isEmpty();
    }

    @Override
    public void resetData() {
        players = new ArrayList<>();
    }
}
