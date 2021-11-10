package net.gcnt.skywarsreloaded.game;

import net.gcnt.skywarsreloaded.utils.Coord;
import net.gcnt.skywarsreloaded.wrapper.SWPlayer;

import java.util.List;

public interface GameWorld {

    String getId();

    GameTemplate getGame();

    List<GameTeam> getTeams();

    String getWorldName();

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
    String[] generateChestLoot();

    /**
     * Fill the chest with the given coord.
     *
     * @param coord Location of the chest
     */
    void fillChest(Coord coord);

    /**
     * Removes all the cages that are currently placed.
     */
    void removeCages();

}
