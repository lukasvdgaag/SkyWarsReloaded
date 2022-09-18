package net.gcnt.skywarsreloaded.game;

import net.gcnt.skywarsreloaded.game.gameinstance.GameInstance;
import net.gcnt.skywarsreloaded.game.types.TeamColor;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;

import java.util.List;
import java.util.UUID;

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
     * Add a player to a team.
     *
     * @param player Player to add
     */
    void addPlayer(GamePlayer player);

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
     * Get the number of players part of that team
     *
     * @return Count of members in the team
     */
    int getPlayerCount();

    /**
     * Get a list of alive players in the team
     *
     * @return Alive players.
     */
    List<GamePlayer> getAlivePlayers();

    /**
     * Eliminate a player from the team.
     *
     * @param player Player to eliminate
     */
    void eliminatePlayer(GamePlayer player);

    /**
     * Get the Game that this team belongs to.
     *
     * @return Parent Game.
     */
    GameInstance getGameWorld();

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
     * Get whether a new player can be added to the team.
     *
     * @return true if a new player can join the team
     */
    boolean canJoin();

    /**
     * Get whether the player can be added to the team.
     *
     * @param uuid UUID of the player to check
     * @return true if a new player can join the team
     */
    boolean canJoin(UUID uuid);

    /**
     * Get the spawn a player was asigned to. If not found return null
     *
     * @param player Player to get the spawn for
     * @return the {@link TeamSpawn} that the player was assigned to
     */
    TeamSpawn getSpawn(GamePlayer player);

    /**
     * Assign spawn to a player. If there are no available spawns, return null
     *
     * @param player Player to assign the spawn to
     * @return the {@link TeamSpawn} that the player was assigned to
     */
    TeamSpawn assignSpawn(GamePlayer player);

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

    /**
     * Get the number of reserved slots for this team.
     *
     * @return Reserved slots
     */
    int getValidReservationCount();

    /**
     * Add a reservation to the team.
     *
     * @param uuid       The UUID of the player
     * @param expireTime The time at which the reservation should become invalid
     */
    void addReservation(UUID uuid, long expireTime);

    /**
     * Get the reservation for a player if they have one.
     *
     * @return A Long representing the time at which the reservation expires or null if they don't have one.
     */
    Long getReservation(UUID uuid);

}
