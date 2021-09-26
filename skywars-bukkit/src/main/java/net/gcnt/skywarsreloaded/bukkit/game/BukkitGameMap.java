package net.gcnt.skywarsreloaded.bukkit.game;

import net.gcnt.skywarsreloaded.bukkit.wrapper.BukkitSWPlayer;
import net.gcnt.skywarsreloaded.game.Game;
import net.gcnt.skywarsreloaded.game.GameMap;
import net.gcnt.skywarsreloaded.game.GameStatus;
import net.gcnt.skywarsreloaded.wrapper.SWPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BukkitGameMap implements GameMap {

    private final String gameId;
    private final Game game;
    private final List<BukkitSWPlayer> players;
    private final List<BukkitSWPlayer> spectators;
    private GameStatus status;
    private int timer;

    public BukkitGameMap(Game game) {
        this.game = game;
        this.gameId = UUID.randomUUID().toString();

        this.players = new ArrayList<>();
        this.spectators = new ArrayList<>();
        this.status = GameStatus.DISABLED;
        this.timer = 0;
    }

    @Override
    public String getId() {
        return gameId;
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public void addPlayers(SWPlayer... players) {

    }

    @Override
    public void removePlayer(SWPlayer player) {

    }

    @Override
    public List<? extends SWPlayer> getPlayers() {
        return this.players;
    }

    @Override
    public List<? extends SWPlayer> getAlivePlayers() {
        return null;
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
