package net.gcnt.skywarsreloaded.game;

import java.util.List;

public interface GameManager {

    void loadAllGameTemplates();

    GameTemplate getGameTemplateByName(String gameId);

    /**
     * Delete a game template from the skywars registry and optionally from the active storage method
     *
     * @param gameId The template name to delete
     * @param deleteMap If true, the map will be deleted as well
     * @return If the map deletion was successful
     */
    boolean deleteGameTemplate(String gameId, boolean deleteMap);

    /**
     * Get all the currently known game templates
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

    GameWorld createGameWorld(GameTemplate data);

    List<GameWorld> getGameWorlds(GameTemplate data);

    List<GameWorld> getGameWorlds();

}
