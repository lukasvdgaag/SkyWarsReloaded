package net.gcnt.skywarsreloaded.bukkit.game;

import net.gcnt.skywarsreloaded.bukkit.wrapper.BukkitSWPlayer;
import net.gcnt.skywarsreloaded.game.*;
import net.gcnt.skywarsreloaded.wrapper.SWPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BukkitGameWorld implements GameWorld {

    private final String gameId;
    private final GameData gameData;
    private final List<BukkitSWPlayer> spectators;
    private final List<Team> teams;
    private GameStatus status;
    private int timer;

    public BukkitGameWorld(GameData gameData) {
        this.gameData = gameData;
        this.gameId = UUID.randomUUID().toString();

        this.spectators = new ArrayList<>();
        this.teams = new ArrayList<>();
        this.status = GameStatus.DISABLED;
        this.timer = 0;
    }

    @Override
    public String getId() {
        return gameId;
    }

    @Override
    public GameData getGame() {
        return gameData;
    }

    @Override
    public List<Team> getTeams() {
        return teams;
    }

    @Override
    public void addPlayers(SWPlayer... players) {

    }

    @Override
    public void removePlayer(SWPlayer player) {

    }

    @Override
    public List<? extends SWPlayer> getPlayers() {
        List<SWPlayer> players = new ArrayList<>();
        for (Team t : teams) {
            for (TeamSpawn ts : t.getSpawns()) {
                players.add(ts.getPlayer());
            }
        }
        return players;
    }

    @Override
    public List<? extends SWPlayer> getAlivePlayers() {
        return null;
    }

    public List<? extends SWPlayer> getAllPlayers() {
        ArrayList<SWPlayer> prs = new ArrayList<>();
        prs.addAll(getPlayers());
        prs.addAll(getSpectators());
        return prs;
    }

    @Override
    public List<? extends SWPlayer> getSpectators() {
        return this.spectators;
    }

    @Override
    public GameStatus getStatus() {
        return this.status;
    }

    @Override
    public void setStatus(GameStatus status) {
        this.status = status;
    }

    @Override
    public int getTimer() {
        return 0;
    }

    @Override
    public void setTimer(int timer) {
        this.timer = timer;
    }
}
