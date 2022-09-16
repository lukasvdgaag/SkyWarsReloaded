package net.gcnt.skywarsreloaded.manager;

import net.gcnt.skywarsreloaded.game.GameInstance;
import net.gcnt.skywarsreloaded.game.GameTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface GameManager {

    /**
     * Load all {@link GameTemplate} from storage into memory.
     */
    void loadAllGameTemplates();

    /**
     * Get a {@link GameTemplate} by its name/identifier.
     *
     * @param gameId Identifier of the template.
     * @return {#link GameTemplate} if found, null otherwise.
     */
    GameTemplate getGameTemplateByName(String gameId);

    /**
     * Get a list of all GameWorlds.
     *
     * @return List of all GameWorlds.
     */
    List<GameInstance> getGameInstancesListCopy();

    /**
     * Get a list of {@link GameInstance} from its template.
     *
     * @param data Template to get the GameWorlds from.
     * @return List of {@link GameInstance} that matches the template.
     */
    List<GameInstance> getGameInstancesCopy(GameTemplate data);

    /**
     * Delete a game template from the skywars registry and optionally from the active storage method
     *
     * @param gameId       The template name to delete
     * @param deleteRemote If true, the map will be deleted as well
     * @return If the map deletion was successful
     */
    boolean deleteGameTemplate(String gameId, boolean deleteRemote);

    /**
     * Get all the currently known game templates
     *
     * @return A list of all the game templates
     */
    List<GameTemplate> getGameTemplatesCopy();

    /**
     * Create a new {@link GameTemplate} with the id/name specified
     *
     * @param gameId The name of the game template
     * @return The created {@link GameTemplate}
     */
    GameTemplate createGameTemplate(String gameId);

    /**
     * Create a new {@link GameInstance} from a {@link GameTemplate}.
     *
     * @param data GameTemplate to create a world from.
     * @return The newly created {@link GameInstance}.
     */
    CompletableFuture<GameInstance> createGameWorld(GameTemplate data);

    /**
     * Remove a game world from running.
     *
     * @param instance GameWorld to remove.
     */
    CompletableFuture<Void> deleteGameInstance(GameInstance instance);

    /**
     * Get a GameWorld by its assigned world name.
     *
     * @param worldName Name of the world to look up.
     * @return {@link GameInstance} if found, null otherwise.
     */
    GameInstance getGameInstanceByName(String worldName);

    /**
     * Get a list of all GameWorlds that were created under a specified template.
     *
     * @param template The template to get the game instances of.
     * @return List of all GameWorlds.
     */
    List<GameInstance> getGameInstancesByTemplate(GameTemplate template);


}
