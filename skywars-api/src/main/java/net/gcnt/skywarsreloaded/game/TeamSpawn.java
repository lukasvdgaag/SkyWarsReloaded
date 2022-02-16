package net.gcnt.skywarsreloaded.game;

import net.gcnt.skywarsreloaded.utils.SWCompletableFuture;
import net.gcnt.skywarsreloaded.utils.SWCoord;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface TeamSpawn {

    /**
     * Check whether a TeamSpawn is occupied (full).
     *
     * @return Whether the spawn is occupied yet.
     */
    boolean isOccupied();

    /**
     * Get the center origin location of this spawn.
     *
     * @return Location of the spawn.
     */
    SWCoord getLocation();

    /**
     * Get a list of Players that are in this team.
     *
     * @return List of Players in this team.
     */
    List<GamePlayer> getPlayers();

    /**
     * Add a player to the team.
     *
     * @param player Player to add.
     */
    void addPlayer(GamePlayer player);

    /**
     * Remove a player from the team.
     *
     * @param player Player to remove.
     */
    void removePlayer(GamePlayer player);

    /**
     * Get the Team that is assigned to this spawn.
     *
     * @return Team
     */
    GameTeam getTeam();

    /**
     * Determine which Cage design to choose based of the players in the team.
     */
    void determineCageDesign();

    /**
     * Update the TeamCage with new data.
     *
     * @return A {@link SWCompletableFuture} which completes when the cage has been successfully placed.
     */
    SWCompletableFuture<Boolean> updateCage();

    /**
     * Remove the TeamCage.
     */
    void removeCage();

    /**
     * Get the TeamCage to spawn the players in.
     *
     * @return TeamCage to spawn the team in.
     */
    TeamCage getCage();

    /**
     * Assign a cage to the team to spawn the players in.
     *
     * @param cage New TeamCage.
     */
    void setCage(TeamCage cage);

}
