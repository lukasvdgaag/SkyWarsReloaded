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
     * @param cage Id of the cage to remove. Only applies to normal cages.
     * @return Future with a boolean indicating whether the removing of the cage succeeded
     */
    CompletableFuture<Boolean> removeCage(String cage);

    boolean isPlaced();

    void setPlaced(boolean placed);

}
