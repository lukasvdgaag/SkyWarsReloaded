package net.gcnt.skywarsreloaded.game;

import net.gcnt.skywarsreloaded.game.gameinstance.GameInstance;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;

public class CoreGamePlayer implements GamePlayer {

    private final SWPlayer player;
    private final GameInstance gameWorld;
    private GameTeam team;
    private boolean alive;
    private boolean spectating;
    private SWScoreboard scoreboard;
    private SWPlayer taggedBy;
    private int kills;
    private int assists;

    public CoreGamePlayer(SWPlayer player, GameInstance gameWorld) {
        this.player = player;
        this.gameWorld = gameWorld;
        this.team = null;
        this.alive = false;
        this.spectating = false;
        this.scoreboard = null;
        this.taggedBy = null;
        this.kills = 0;
        this.assists = 0;
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
        this.alive = false;
        // todo do things?
    }

    @Override
    public GameInstance getGame() {
        return gameWorld;
    }

    @Override
    public GameTeam getTeam() {
        return team;
    }

    @Override
    public void setTeam(GameTeam team) {
        this.team = team;
    }

    @Override
    public SWScoreboard getScoreboard() {
        return scoreboard;
    }

    @Override
    public void setScoreboard(SWScoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

    @Override
    public SWPlayer getLastTaggedBy() {
        return taggedBy;
    }

    @Override
    public void setLastTaggedBy(SWPlayer player) {
        this.taggedBy = player;
    }

    @Override
    public int getKills() {
        return kills;
    }

    @Override
    public void addKill() {
        this.kills++;
    }

    @Override
    public int getAssists() {
        return assists;
    }

    @Override
    public void addAssist() {
        this.assists++;
    }
}
