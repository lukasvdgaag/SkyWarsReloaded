package net.gcnt.skywarsreloaded.game;

import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;

public class CoreGamePlayer implements GamePlayer {

    private final SWPlayer player;
    private final GameWorld gameWorld;
    private GameTeam team;
    private boolean alive;
    private boolean spectating;

    public CoreGamePlayer(SWPlayer player, GameWorld gameWorld) {
        this.player = player;
        this.gameWorld = gameWorld;
        this.team = null;
        this.alive = false;
        this.spectating = false;
    }

    @Override
    public SWPlayer getSWPlayer() {
        return player;
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public void setAlive(boolean alive) {
        this.alive = alive;
        // todo do things?
    }

    @Override
    public boolean isSpectating() {
        return spectating;
    }

    @Override
    public void setSpectating(boolean spectating) {
        this.spectating = spectating;
        // todo do things?
    }

    @Override
    public GameWorld getGame() {
        return gameWorld;
    }

    @Override
    public GameTeam getTeam() {
        return team;
    }
}
