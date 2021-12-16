package net.gcnt.skywarsreloaded.game;

import java.util.concurrent.CompletableFuture;

public interface TeamCage {

    TeamSpawn getSpawn();

    /**
     * Place the cage in the world
     *
     * @param cageName The cage identifier from the configuration
     * @return Future with a boolean indicating whether the placing of the cage succeeded
     */
    CompletableFuture<Boolean> placeCage(String cageName);

    /**
     * Remove the cage from the world
     *
     * @param cage Identifier of the cage to remove. Only applies to normal cages.
     * @return Future with a boolean indicating whether the removing of the cage succeeded
     */
    CompletableFuture<Boolean> removeCage(String cage);

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
