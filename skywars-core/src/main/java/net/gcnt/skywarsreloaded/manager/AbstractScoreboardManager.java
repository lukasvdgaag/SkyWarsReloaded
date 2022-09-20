package net.gcnt.skywarsreloaded.manager;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.GamePlayer;
import net.gcnt.skywarsreloaded.game.GameTeam;
import net.gcnt.skywarsreloaded.game.gameinstance.GameInstance;
import net.gcnt.skywarsreloaded.game.gameinstance.LocalGameInstance;
import net.gcnt.skywarsreloaded.game.types.GameState;
import net.gcnt.skywarsreloaded.utils.properties.MessageProperties;
import net.gcnt.skywarsreloaded.utils.scoreboards.SWBoard;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

public abstract class AbstractScoreboardManager implements ScoreboardManager {

    protected final SkyWarsReloaded plugin;
    protected final HashMap<SWPlayer, SWBoard> scoreboards;

    public AbstractScoreboardManager(SkyWarsReloaded plugin) {
        this.plugin = plugin;
        this.scoreboards = new HashMap<>();
    }

    @Override
    public String determineScoreboardFormat(SWPlayer player) {
        GameInstance gameWorld = player.getGameWorld();
        if (!(gameWorld instanceof LocalGameInstance)) return MessageProperties.SCOREBOARDS_LOBBY.toString();

        LocalGameInstance localGameInstance = (LocalGameInstance) gameWorld;

        if (localGameInstance.getState() == GameState.WAITING_CAGES || localGameInstance.getState() == GameState.WAITING_LOBBY || localGameInstance.getState() == GameState.COUNTDOWN) {
            if (localGameInstance.getState() == GameState.COUNTDOWN || (localGameInstance.getWaitingPlayers().size() >= localGameInstance.getTemplate().getMinPlayers())) {
                return MessageProperties.SCOREBOARDS_STARTING_SOON.toString();
            }
            return MessageProperties.SCOREBOARDS_WAITING.toString();
        } else if (localGameInstance.getState() == GameState.PLAYING) {
            // todo add event formats.
            return MessageProperties.SCOREBOARDS_PLAYING.toString();
        } else if (localGameInstance.getState() == GameState.ENDING) {
            return MessageProperties.SCOREBOARDS_ENDING.toString();
        }
        return MessageProperties.SCOREBOARDS_LOBBY.toString();
    }

    @Override
    public String prepareLine(SWPlayer player, String line, @Nullable LocalGameInstance gameWorld) {
        if (line.trim().isEmpty()) return "";

        line = plugin.getUtils().colorize(line);

        if (gameWorld == null || !line.contains("%")) return line;

        final GamePlayer gamePlayer = gameWorld.getPlayer(player);

        line = line
                .replace("%max_players%", gameWorld.getTemplate().getMaxPlayers() + "")
                .replace("%template%", plugin.getUtils().colorize(gameWorld.getTemplate().getDisplayName()))
                .replace("%difficulty%", "N/A") // todo add support for difficulty/mode.
                .replace("%timer%", gameWorld.getTimer() + "")
                .replace("%countdown%", gameWorld.getTimer() + "")
                .replace("%kills%", gamePlayer.getKills() + "")
                .replace("%assists%", gamePlayer.getAssists() + "");

        if (gameWorld.getState() == GameState.WAITING_CAGES || gameWorld.getState() == GameState.WAITING_LOBBY || gameWorld.getState() == GameState.COUNTDOWN) {
            final int waitingPlayers = gameWorld.getWaitingPlayers().size();
            line = line.replace("%players%", waitingPlayers + "")
                    .replace("%players_needed%", (waitingPlayers - gameWorld.getTemplate().getMinPlayers()) + "");
        } else if (gameWorld.getState() == GameState.PLAYING) {
            line = line
                    .replace("%team%", gamePlayer.getTeam() == null ? "N/A" : gamePlayer.getTeam().getName())
                    .replace("%players%", gameWorld.getAlivePlayers().size() + "")
                    .replace("%teams%", gameWorld.getAliveTeams().size() + "");

            // todo add support for events here.
        } else if (gameWorld.getState() == GameState.ENDING) {
            line = line.replace("%winning_team%", gameWorld.getWinningTeam() == null ? "N/A" : gameWorld.getWinningTeam().getName())
            ;
        }

        return line;
    }

    @Override
    public void updatePlayer(SWPlayer player) {
        String format = determineScoreboardFormat(player);
        List<String> lines = getScoreboardLines(player, format);

        SWBoard board = getScoreboard(player);
        if (board == null || lines.size() - 1 != board.getLineCount()) {
            board = createScoreboard(player, lines.size() - 1);
            scoreboards.put(player, board);
        }

        LocalGameInstance gameWorld = null;
        if (player.getGameWorld() instanceof LocalGameInstance) {
            gameWorld = (LocalGameInstance) player.getGameWorld();
        }

        if (gameWorld != null && gameWorld.getState() == GameState.ENDING) {
            GameTeam winners = gameWorld.getWinningTeam();
            int winnerIndex = -1;

            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).contains("{winners}")) {
                    winnerIndex = i;
                    break;
                }
            }
            if (winnerIndex != -1) lines.remove(winnerIndex);

            if (winners != null) {
                if (winnerIndex != -1) {
                    // replacing the {winners} line with winning players.
                    // by inserting the player names into the line list.
                    // Calculates how many players we can show, and show the remaining players that do not fit, as a number.
                    int availableSlotsForPlayers = 16 - lines.size() + 2;

                    int playersToDisplay = availableSlotsForPlayers;
                    if (availableSlotsForPlayers < winners.getPlayerCount()) {
                        playersToDisplay--;
                        int playersLeft = winners.getPlayerCount() - availableSlotsForPlayers;
                        lines.add(winnerIndex, plugin.getMessages().getString(MessageProperties.SCOREBOARDS_WINNERS_LEFT_LINE.toString())
                                .replace("%winners_left%", playersLeft + ""));
                    }

                    for (int i = 0; i < playersToDisplay; i++) {
                        if (i >= winners.getPlayerCount()) break;
                        lines.add(winnerIndex, plugin.getMessages().getString(MessageProperties.SCOREBOARDS_WINNER_LINE.toString())
                                .replace("%player%", winners.getPlayers().get(i).getSWPlayer().getName()));
                    }
                }
            }
        }

        board.setTitle(plugin.getUtils().colorize(lines.get(0)));
        for (int i = 1; i < lines.size(); i++) {
            board.setLine(i - 1, prepareLine(player, lines.get(i), gameWorld));
        }
    }

    @Override
    public void updateAllPlayers(LocalGameInstance gameWorld) {
        for (GamePlayer player : gameWorld.getPlayersCopy()) {
            updatePlayer(player.getSWPlayer());
        }
    }

    @Override
    public SWBoard getScoreboard(SWPlayer player) {
        return scoreboards.get(player);
    }

    @Override
    public List<String> getScoreboardLines(SWPlayer player, String format) {
        return plugin.getMessages().getStringList(format);
    }
}
