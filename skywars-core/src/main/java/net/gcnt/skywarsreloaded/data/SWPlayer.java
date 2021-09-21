package net.gcnt.skywarsreloaded.data;

import net.gcnt.skywarsreloaded.game.Game;

import java.util.UUID;

public abstract class SWPlayer {

    private boolean online;
    private Game game;
    private final UUID uuid;
    private boolean alive;
    private boolean spectating;

    public SWPlayer(UUID uuid, boolean online) {
        this.uuid = uuid;
        this.online = online;
        this.alive = true;
        this.spectating = false;
        this.game = null;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Game getGame() {
        return game;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public boolean isOnline() {
        return online;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void setSpectating(boolean spectating) {
        this.spectating = spectating;
    }

    public boolean isSpectating() {
        return spectating;
    }
}
