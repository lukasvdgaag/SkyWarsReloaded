package net.gcnt.skywarsreloaded.game;

import net.gcnt.skywarsreloaded.data.config.YAMLConfig;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.utils.results.SpawnAddResult;
import net.gcnt.skywarsreloaded.utils.results.SpawnRemoveResult;
import net.gcnt.skywarsreloaded.wrapper.sender.SWCommandSender;

import java.util.List;

public interface GameTemplate {

    /**
     * Get the name/identifier of the template.
     *
     * @return Name of the template.
     */
    String getName();

    /**
     * Get the creator of the template.
     *
     * @return Creator of the template.
     */
    String getCreator();

    /**
     * Set the creator of the template.
     *
     * @param creator New creator.
     */
    void setCreator(String creator);

    /**
     * Get the display name of the template.
     *
     * @return Display name of the template.
     */
    String getDisplayName();

    /**
     * Set the display name of the template.
     *
     * @param value New display name.
     */
    void setDisplayName(String value);

    /**
     * Get the team size of the template.
     *
     * @return Team size of the template.
     */
    int getTeamSize();

    /**
     * Set the team size of the template.
     * Changing this to 1 will make it a solo game, anything higher will be a team game.
     *
     * @param size New team size.
     */
    void setTeamSize(int size);

    /**
     * Get the minimum amount of players needed to start the game.
     *
     * @return Minimum players to start the game.
     */
    int getMinPlayers();

    /**
     * Set the minimum amount of players needed to start the game.
     *
     * @param amount Minimum amount of players.
     */
    void setMinPlayers(int amount);

    /**
     * Get the waiting lobby spawn.
     *
     * @return null if no spawn set, SWCoord with the location if set.
     */
    SWCoord getWaitingLobbySpawn();

    /**
     * Set the waiting lobby spawn.
     * This spawn is primarily used to spawn players in during team games.
     *
     * @param loc New waiting lobby spawn.
     */
    void setWaitingLobbySpawn(SWCoord loc);

    /**
     * Get the spectator spawn.
     *
     * @return null if no spawn set, SWCoord with the location if set.
     */
    SWCoord getSpectateSpawn();

    /**
     * Set the spectator spawn.
     *
     * @param loc New spectator spawn.
     */
    void setSpectateSpawn(SWCoord loc);

    /**
     * Disable this template.
     * Disabling a template will prevent games from being created.
     */
    void disable();

    /**
     * Enable this template.
     * Enabling a template will allow games to be created.
     */
    void enable();

    /**
     * Load all template data from storage.
     */
    void loadData();

    /**
     * Save all cached template data to storage.
     */
    void saveData();

    /**
     * Add a new chest to the template.
     * This method will return false if there already was a chest at the target location.
     *
     * @param loc Location of the new chest.
     * @return true if added, false if not added.
     */
    boolean addChest(SWCoord loc);

    /**
     * Remove an existing chest from the template.
     * This method will return true if the target block is a chest, and was already registered.
     *
     * @param loc Location of the chest.
     * @return true if removed, false if not removed.
     */
    boolean removeChest(SWCoord loc);

    /**
     * Get a list of all registered chest locations.
     *
     * @return List of chest locations.
     */
    List<SWCoord> getChests();

    /**
     * Add a new player spawnpoint a team.
     *
     * @param team <p>Team number to add the spawn to, starting from 0. You can set this to whatever if the team size is 1.</p>
     * @param loc  Location of the player spawnpoint.
     * @return {@link SpawnAddResult} with the result of the spawn add request.
     */
    SpawnAddResult addSpawn(int team, SWCoord loc);

    /**
     * Remove an existing player spawnpoint from a team.
     *
     * @param loc Location of the player spawnpoint.
     * @return {@link SpawnRemoveResult} with the result of the spawn removal request.
     */
    SpawnRemoveResult removeSpawn(SWCoord loc);

    /**
     * Check whether the template is enabled or not.
     *
     * @return If the template is enabled.
     */
    boolean isEnabled();

    /**
     * Get a list of team spawnpoints per team.
     *
     * @return List of team spawnpoints per team.
     */
    List<List<SWCoord>> getTeamSpawnpoints();

    /**
     * Get the border radius of the template.
     * This border radius is used for saving the world to a schematic.
     *
     * @return Border radius of the template.
     */
    int getBorderRadius();

    /**
     * Set the border radius of the template.
     * This border radius is used for saving the world to a schematic.
     *
     * @param radius New border radius.
     */
    void setBorderRadius(int radius);

    /**
     * Get the corresponding YAML configuration of the template.
     *
     * @return YAML Configuration of the template.
     */
    YAMLConfig getConfig();

    /**
     * Run a check through all possible things to set up for the arena to see if anything is left.
     * This method will send a message to the target player with the next thing to do on the list.
     *
     * @param player Player to send the messages to.
     * @return true if all checks passed, false if there's things left to set up.
     */
    boolean checkToDoList(SWCommandSender player);

}
