package net.gcnt.skywarsreloaded.game.gameinstance;

import com.google.gson.JsonObject;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.types.GameState;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface GameInstance {

    /**
     * Gets the ID of the instance.
     *
     * @return The ID of the instance
     */
    UUID getId();

    /**
     * Gets the {@link GameTemplate} used to create the instance.
     *
     * @return The instance template
     */
    GameTemplate getTemplate();

    /**
     * Gets the {@link GameState} of the instance. The state will be EDIT_MODE if the instance is being used to
     * edit the {@link GameTemplate} of this instance.
     *
     * @return
     */
    GameState getState();

    /**
     * Get is a player is allowed to join the game
     *
     * @return True if a player can join, false otherwise
     */
    boolean canJoinGame();

    /**
     * Get the total number of players in the instance.
     *
     * @return The number of players.
     */
    int getPlayerCount();

    void setState(GameState state);

    boolean isEditing();

    CompletableFuture<Void> requestEditSession();

    JsonObject toJSON();

}
