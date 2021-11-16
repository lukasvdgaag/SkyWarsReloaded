package net.gcnt.skywarsreloaded.game;

import net.gcnt.skywarsreloaded.game.types.TeamColor;
import net.gcnt.skywarsreloaded.wrapper.SWPlayer;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;

import java.util.List;

public interface GameTeam {

    /**
     * Get the color of the team.
     *
     * @return TeamColor
     */
    TeamColor getColor();

    /**
     * Get the name of the team.
     *
     * @return Name of the team
     */
    String getName();

    /**
     * Check if a player is a member of this team.
     *
     * @param player Player to check
     * @return if the player is in the team
     */
    boolean isMember(SWPlayer player);

    /**
     * Add players to a team.
     *
     * @param players Players to add
     */
    void addPlayers(SWPlayer... players);

    /**
     * Remove a player from the team.
     * This should not completely remove them, but rather make them inactive
     * so that they can rejoin the game later.
     *
     * @param player Player to remove
     */
    void removePlayer(SWPlayer player);

    /**
     * Put a player back into the game after they left.
     *
     * @param player Player to re-add.
     */
    void rejoin(SWPlayer player);

    /**
     * Get a list of team members
     *
     * @return Members of the team
     */
    List<GamePlayer> getPlayers();

    /**
     * Get a list of alive players in the team
     *
     * @return Alive players.
     */
    List<GamePlayer> getAlivePlayers();

    /**
     * Get the Game that this team belongs to.
     *
     * @return Parent Game.
     */
    GameWorld getGameWorld();

    /**
     * Get the size of the team.
     *
     * @return Team size.
     */
    int getSize();

    /**
     * Get the amount of alive players in the team.
     *
     * @return Amount of alive players in the team.
     */
    int getAliveSize();

    /**
     * Get whether the entire team is eliminated yet.
     *
     * @return if the team is eliminated.
     */
    boolean isEliminated();

    /**
     * Get a list of spawns for this team.
     *
     * @return List of player spawns
     */
    List<? extends TeamSpawn> getSpawns();

    /**
     * Reset the data of this team for future games.
     */
    void resetData();


}
