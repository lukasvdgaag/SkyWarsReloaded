package net.gcnt.skywarsreloaded.bukkit.game;

import net.gcnt.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.*;
import net.gcnt.skywarsreloaded.wrapper.SWPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BukkitGameWorld implements GameWorld {

    private final BukkitSkyWarsReloaded plugin;
    private final String gameId;
    private final GameData gameData;
    private final List<BukkitSWPlayer> spectators;
    private final List<Team> teams;
    private GameStatus status;
    private int timer;
    private String worldName;

    public BukkitGameWorld(BukkitSkyWarsReloaded plugin, GameData gameData) {
        this.plugin = plugin;
        this.gameData = gameData;
        this.gameId = UUID.randomUUID().toString();

        this.spectators = new ArrayList<>();
        this.teams = new ArrayList<>();
        this.status = GameStatus.DISABLED;
        this.timer = 0;
        // todo create a world and edit this.worldName.
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
    public List<GamePlayer> getPlayers() {
        List<GamePlayer> players = new ArrayList<>();
        for (GameTeam t : teams) {
            players.addAll(t.getPlayers());
        }
        return players;
    }

    @Override
    public List<GamePlayer> getAlivePlayers() {
        List<GamePlayer> players = new ArrayList<>();
        for (GameTeam t : teams) {
            players.addAll(t.getAlivePlayers());
        }
        return players;
    }

    public List<GamePlayer> getAllPlayers() {
        ArrayList<GamePlayer> prs = new ArrayList<>();
        prs.addAll(getPlayers());
        prs.addAll(getSpectators());
        return prs;
    }

    @Override
    public List<GamePlayer> getSpectators() {
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
