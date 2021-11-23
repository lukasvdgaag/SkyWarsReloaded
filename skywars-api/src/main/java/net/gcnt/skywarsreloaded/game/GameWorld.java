package net.gcnt.skywarsreloaded.game;

import net.gcnt.skywarsreloaded.game.types.GameStatus;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;
import net.gcnt.skywarsreloaded.wrapper.world.SWWorld;

import java.util.List;
import java.util.UUID;

public interface GameWorld {

    String getId();

    GameTemplate getTemplate();

    List<GameTeam> getTeams();

    SWWorld getWorld();

    String getWorldName();

    boolean canJoin();

    boolean isEditing();

    void setEditing(boolean edit);

    GamePlayer preparePlayerJoin(UUID uuid);

    void addPlayers(SWPlayer... players);

    void removePlayer(SWPlayer player);

    List<GamePlayer> getPlayers();

    List<GamePlayer> getAlivePlayers();

    List<GamePlayer> getSpectators();

    GameStatus getStatus();

    void setStatus(GameStatus status);

    int getTimer();

    void setTimer(int timer);

    /**
     * Generate the loot of one chest based on the voted chest type.
     *
     * @return String array with all slots and their ItemStack values as strings.
     */
    Item[] generateChestLoot();

    /**
     * Fill the chest with the given coord.
     *
     * @param coord Location of the chest
     */
    void fillChest(SWCoord coord);

    /**
     * Removes all the cages that are currently placed.
     */
    void removeCages();

}
