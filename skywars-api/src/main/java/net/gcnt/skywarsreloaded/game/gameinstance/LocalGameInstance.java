package net.gcnt.skywarsreloaded.game.gameinstance;

import net.gcnt.skywarsreloaded.game.GamePlayer;
import net.gcnt.skywarsreloaded.game.GameScheduler;
import net.gcnt.skywarsreloaded.game.GameTeam;
import net.gcnt.skywarsreloaded.game.chest.SWChestTier;
import net.gcnt.skywarsreloaded.game.types.ChestType;
import net.gcnt.skywarsreloaded.utils.Message;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;
import net.gcnt.skywarsreloaded.wrapper.world.SWWorld;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface LocalGameInstance extends GameInstance {

    List<GameTeam> getTeams();

    GameTeam getTeam(GamePlayer player);

    GamePlayer getPlayer(SWPlayer player);

    SWWorld getWorld();

    String getWorldName();

    boolean isSpawnAvailable();

    void setEditing(boolean edit);

    void startScheduler();

    void startGame();

    void endGame();

    void setIdle();

    List<GamePlayer> getTopKillers();

    GameScheduler getScheduler();

    Map<UUID, SWChestTier> getVotedChestTiers();

    Map<UUID, ChestType> getSelectedEditingChestTypes();

    SWChestTier getChestTier();

    void announce(Message message);

    void announce(String message);

    void announceTitle(Message message);

    void announceTitle(Message message, int fadeIn, int stay, int fadeOut);

    void setChestTypeSelected(UUID player, SWChestTier type);

    /**
     * This method is to be run async since it could perform long operations
     *
     * @param uuid                  UUID of the player to add
     * @param ignoreAvailableSpawns true if the player should be added anyway if there is no available spawn points
     * @return A {@link GamePlayer} object if the player was added, null otherwise
     */
    GamePlayer preparePlayerJoin(UUID uuid, boolean ignoreAvailableSpawns);

    /**
     * If this method doesn't return true, the calling method is expected to handle kicking the player(s) which
     * have not made it into a team.
     *
     * @param players List of player to try adding to the game
     * @return true if all players were added to the game
     */
    boolean addPlayers(GamePlayer... players);

    void removePlayer(GamePlayer player);

    void preparePlayer(SWPlayer player);

    void preparePlayerInventory(SWPlayer player);

    List<GamePlayer> getPlayersCopy();

    List<GamePlayer> getAlivePlayers();

    List<GameTeam> getAliveTeams();

    List<GamePlayer> getWaitingPlayers();

    List<GamePlayer> getSpectators();

    void makeReadyForEditing();

    int getTimer();

    void setTimer(int timer);

    /**
     * Removes all the cages that are currently placed.
     */
    void removeCages();

    /**
     * Fill all the registered chests in the world
     */
    void fillChests();

    void loadTeams();

    void makeReadyForGame();

    GameTeam getWinningTeam();

    void determineWinner();
}
