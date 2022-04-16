package net.gcnt.skywarsreloaded.game;

import net.gcnt.skywarsreloaded.utils.SWCompletableFuture;

public interface TeamCage {

    TeamSpawn getSpawn();

    /**
     * Place the cage in the world
     *
     * @return Future with a boolean indicating whether the placing of the cage succeeded
     */
    SWCompletableFuture<Boolean> placeCage();

    /**
     * Remove the cage from the world
     *
     * @return Future with a boolean indicating whether the removing of the cage succeeded
     */
    SWCompletableFuture<Boolean> removeCage();

    /**
     * Check whether the cage has been placed.
     *
     * @return True when placed, false otherwise.
     */
    boolean isPlaced();

    /**
     * Set whether the cage has been placed.
     *
     * @param placed Whether the cage was placed.
     */
    void setPlaced(boolean placed);

}
