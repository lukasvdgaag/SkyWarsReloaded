package net.gcnt.skywarsreloaded.manager;

import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;

import java.util.List;

public interface ScoreboardManager {

    void updatePlayer(SWPlayer player);

    /**
     * Determine what scoreboard format to apply to a player.
     *
     * @param player Player to show scoreboard to.
     * @return id of scoreboard format.
     */
    String determineScoreboardFormat(SWPlayer player);

    List<String> getScoreboardLines(SWPlayer player, String format);

}
