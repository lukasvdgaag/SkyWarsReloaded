package net.gcnt.skywarsreloaded.game;

import net.gcnt.skywarsreloaded.wrapper.AbstractSWPlayer;
import net.gcnt.skywarsreloaded.utils.Coord;

import java.util.List;

public interface Game {

    String getName();

    int getMaxPlayers();

    String getCreator();

    /**
     * Add a player to the game.
     *
     * @param players All players to add to the game.
     */
    void addPlayers(AbstractSWPlayer... players);

    /**
     * Remove a certain player from the game.
     * This does not actually remove the player from the game
     * but will kill the player if not already dead and make them a spectator in case of rejoining.
     *
     * @param player Player to remove from the game.
     */
    void removePlayer(AbstractSWPlayer player);

    /**
     * Get a list of all players.
     *
     * @return List of all players that joined the game.
     */
    List<? extends AbstractSWPlayer> getPlayers();

    /**
     * Get a list of alive players.
     *
     * @return List of alive players.
     */
    List<AbstractSWPlayer> getAlivePlayers();

    /**
     * Get a list of players that are spectating the game.
     *
     * @return List of spectators.
     */
    List<AbstractSWPlayer> getSpectators();

    /**
     * Get the status of the game.
     *
     * @return Game status.
     */
    GameStatus getStatus();

    /**
     * Set the state of the game.
     *
     * @param status New game status.
     */
    void setStatus(GameStatus status);

    /**
     * Get the amount of players per team.
     *
     * @return Team size.
     */
    int getTeamSize();

    String getDisplayName();

    int getTimer();

    int setTimer();

    Coord getSpectateSpawn();

    void setSpectateSpawn(Coord loc);

    Coord getWaitingLobbySpawn();

    void setWaitingLobbySpawn(Coord loc);

    void disable();

    void loadData();

    void saveData();

    void reset();

    void addChest(Coord loc);

    void removeChest(Coord loc);

    List<Coord> getChests();


}
