package net.gcnt.skywarsreloaded.manager;

import net.gcnt.skywarsreloaded.game.GameInstance;
import net.gcnt.skywarsreloaded.utils.scoreboards.SWBoard;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ScoreboardManager {

    void updatePlayer(SWPlayer player);

    void updateAllPlayers(GameInstance gameWorld);

    /**
     * Determine what scoreboard format to apply to a player.
     *
     * @param player Player to show scoreboard to.
     * @return id of scoreboard format.
     */
    String determineScoreboardFormat(SWPlayer player);

    /**
     * Replaces all placeholders for the player in the game.
     *
     * @param player    Player to parse the placeholders for.
     * @param line      Line to parse the placeholders for.
     * @param gameWorld Optional GameWorld of the player to use for parsing.
     * @return Parsed line.
     */
    String prepareLine(SWPlayer player, String line, @Nullable GameInstance gameWorld);

    SWBoard createScoreboard(SWPlayer player, int lineCount);

    SWBoard getScoreboard(SWPlayer player);

    List<String> getScoreboardLines(SWPlayer player, String format);

}
