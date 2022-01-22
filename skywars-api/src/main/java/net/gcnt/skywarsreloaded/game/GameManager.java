package net.gcnt.skywarsreloaded.game;

import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;
import net.gcnt.skywarsreloaded.wrapper.world.SWWorld;

import java.util.List;

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
     * Get a {@link GameTemplate} by its wrapped world.
     *
     * @param swWorld The wrapped world object the game world is based on.
     * @return GameTemplate if found, null otherwise.
     */
    GameWorld getGameWorldBySWWorld(SWWorld swWorld);

    /**
     * Delete a game template from the skywars registry and optionally from the active storage method
     *
     * @param gameId    The template name to delete
     * @param deleteMap If true, the map will be deleted as well
     * @return If the map deletion was successful
     */
    boolean deleteGameTemplate(String gameId, boolean deleteMap);

    /**
     * Get all the currently known game templates
     *
     * @return A list of all the game templates
     */
    List<GameTemplate> getGameTemplates();

    /**
     * Create a new {@link GameTemplate} with the id/name specified
     *
     * @param gameId The name of the game template
     * @return The created {@link GameTemplate}
     */
    GameTemplate createGameTemplate(String gameId);

    /**
     * Create a new {@link GameWorld} from a {@link GameTemplate}.
     *
     * @param data GameTemplate to create a world from.
     * @return The newly created {@link GameWorld}.
     */
    GameWorld createGameWorld(GameTemplate data);

    /**
     * Get a list of {@link GameWorld} from its template.
     *
     * @param data Template to get the GameWorlds from.
     * @return List of {@link GameWorld} that matches the template.
     */
    List<GameWorld> getGameWorlds(GameTemplate data);

    /**
     * Get a GameWorld by its assigned world name.
     *
     * @param worldName Name of the world to look up.
     * @return {@link GameWorld} if found, null otherwise.
     */
    GameWorld getGameWorldByName(String worldName);

    /**
     * Get a list of all GameWorlds.
     *
     * @return List of all GameWorlds.
     */
    List<GameWorld> getGameWorlds();

    /**
     * Get a list of all GameWorlds that were created under a specified template.
     *
     * @param template The template to get the game instances of.
     * @return List of all GameWorlds.
     */
    List<GameWorld> getGameWorldsByTemplate(GameTemplate template);

}
