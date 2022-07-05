package net.gcnt.skywarsreloaded.game;

import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;

public interface GamePlayer {

    /**
     * Get the actual Player that is in the game.
     *
     * @return The Player that's in the team.
     */
    SWPlayer getSWPlayer();

    /**
     * Get the player that damaged the player last.
     *
     * @return The player that damaged the player last.
     */
    SWPlayer getLastTaggedBy();

    /**
     * Set the player that damaged the player last.
     *
     * @param player Player that dealt damage.
     */
    void setLastTaggedBy(SWPlayer player);

    /**
     * Get if the player is still alive in the game.
     *
     * @return true when alive, false when dead.
     */
    boolean isAlive();

    /**
     * Set whether a player is still alive in the game.
     * This should only be called once per game to mark the player as dead.
     *
     * @param alive Whether the player is alive.
     */
    void setAlive(boolean alive);

    /**
     * Get if the player is spectating the game.
     *
     * @return True when spectating, false otherwise.
     */
    boolean isSpectating();

    /**
     * Set whether a player is spectating the game.
     *
     * @param spectating Whether the player is spectating the game.
     */
    void setSpectating(boolean spectating);

    /**
     * Get the {@link GameWorld} that the Player is in.
     *
     * @return Game that the player is in.
     */
    GameWorld getGame();

    /**
     * Get the team that the Player is in.
     *
     * @return Team that the player is in.
     */
    GameTeam getTeam();

    void setTeam(GameTeam team);

    SWScoreboard getScoreboard();

    void setScoreboard(SWScoreboard scoreboard);

}
