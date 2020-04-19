package com.walrusone.skywarsreloaded.game;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.enums.MatchState;
import com.walrusone.skywarsreloaded.enums.ScoreVar;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.Util;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameBoard {
    private GameMap gMap;
    private Scoreboard scoreboard;
    private Objective objective;
    private int restartTimer;
    private Map<ScoreVar, Integer> waitboard = new HashMap<>();
    private Map<ScoreVar, Integer> playboard = new HashMap<>();
    private Map<ScoreVar, Integer> endboard = new HashMap<>();
    private Map<Integer, String> currentLineText = new HashMap<>();

    GameBoard(GameMap gMap) {
        this.gMap = gMap;
        this.restartTimer = -1;
        setupFormats("waitboard");
        setupFormats("playboard");
        setupFormats("endboard");
        setupScoreBoard();
    }

    private void setupFormats(String board) {
        Pattern PATTERN = Pattern.compile("(?i)(\\{[a-z0-9_]+})");
        File storageFile = new File(SkyWarsReloaded.get().getDataFolder(), "src/messages.yml");
        FileConfiguration storage = YamlConfiguration.loadConfiguration(storageFile);
        for (int i = 1; i < 17; i++) {
            String message = storage.getString("scoreboards." + board + ".line" + i);
            Matcher matcher = PATTERN.matcher(message);
            while (matcher.find()) {
                String variable = matcher.group();
                variable = variable.substring(1, variable.length() - 1);
                ScoreVar toAdd = ScoreVar.matchType(variable);
                if (((toAdd == ScoreVar.PLAYERS || toAdd == ScoreVar.TIME || toAdd == ScoreVar.RESTARTTIME
                        || toAdd == ScoreVar.CHESTVOTE || toAdd == ScoreVar.HEALTHVOTE || toAdd == ScoreVar.TIMEVOTE
                        || toAdd == ScoreVar.WEATHERVOTE || toAdd == ScoreVar.MODIFIERVOTE))) {
                    if (board.equalsIgnoreCase("waitboard")) {
                        waitboard.put(toAdd, i);
                    } else if (board.equalsIgnoreCase("playboard")) {
                        playboard.put(toAdd, i);
                    } else if (board.equalsIgnoreCase("endboard")) {
                        endboard.put(toAdd, i);
                    }
                }
            }
        }
    }

    private void setupScoreBoard() {
        if (scoreboard != null) {
            resetScoreboard();
        }
        ScoreboardManager manager = SkyWarsReloaded.get().getServer().getScoreboardManager();
        scoreboard = manager.getNewScoreboard();
        objective = SkyWarsReloaded.getNMS().getNewObjective(scoreboard, "dummy", "info");
        if (SkyWarsReloaded.getCfg().showHealth()) {
            Objective objective2 = SkyWarsReloaded.getNMS().getNewObjective(scoreboard, "health", ChatColor.DARK_RED + "❤");
            objective2.setDisplaySlot(DisplaySlot.BELOW_NAME);
        }
        for (int i = 2; i < 17; i++) {
            scoreboard.registerNewTeam("line" + i);
        }
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        for (int i = 0; i < gMap.getTeamCards().size(); i++) {
            TeamCard tCard = gMap.getTeamCards().get(i);
            tCard.setTeam(scoreboard.registerNewTeam("team" + i));
            tCard.getTeam().setPrefix(tCard.getPrefix());
            tCard.getTeam().setAllowFriendlyFire(gMap.allowFriendlyFire());
        }
        updateScoreboard();
    }

    public void updateScoreboard() {
        if (objective != null) {
            objective.unregister();
        }
        if (scoreboard != null) {
            objective = SkyWarsReloaded.getNMS().getNewObjective(scoreboard, "dummy", "info");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            String sb = "";
            if (gMap.getMatchState() == MatchState.WAITINGSTART) {
                sb = "scoreboards.waitboard.line";
            } else if (gMap.getMatchState() == MatchState.PLAYING) {
                sb = "scoreboards.playboard.line";
            } else if (gMap.getMatchState() == MatchState.ENDING) {
                sb = "scoreboards.endboard.line";
                if (restartTimer == -1) {
                    startRestartTimer();
                }
            }

            for (int i = 1; i < 17; i++) {
                if (i == 1) {
                    String title = getScoreboardLine(sb + i);
                    if (title.length() > 32) {
                        title = title.substring(0, 31);
                    }
                    objective.setDisplayName(title);
                } else {
                    StringBuilder s = new StringBuilder(39);
                    if (getScoreboardLine(sb + i).length() == 0) {
                        for (int j = 0; j < i; j++) {
                            s.append(" ");
                        }
                    } else {
                        s.append(getScoreboardLine(sb + i));
                    }
                    if (!s.toString().equalsIgnoreCase("remove")) {
                        if (s.length() > 40) {
                            s.substring(0, 39);
                        }
                        if (currentLineText.containsKey(i)) {
                            scoreboard.resetScores(currentLineText.get(i));
                            scoreboard.getTeam("line" + i).removeEntry(currentLineText.get(i));
                        }
                        scoreboard.getTeam("line" + i).addEntry(s.toString());
                        objective.getScore(s.toString()).setScore(17 - i);
                        currentLineText.put(i, s.toString());
                    }
                }
            }
        }
    }

    public void updateScoreboardVar(ScoreVar var) {
        int position = 0;
        String sb = "";
        if (gMap.getMatchState() == MatchState.WAITINGSTART) {
            if (waitboard.containsKey(var)) {
                position = waitboard.get(var);
                sb = "scoreboards.waitboard.line";
            }
        } else if (gMap.getMatchState() == MatchState.PLAYING) {
            if (playboard.containsKey(var)) {
                position = playboard.get(var);
                sb = "scoreboards.playboard.line";
            }
        } else if (gMap.getMatchState() == MatchState.ENDING) {
            if (endboard.containsKey(var)) {
                position = endboard.get(var);
                sb = "scoreboards.endboard.line";
            }
        }
        if (position != 0) {
            if (position == 1) {
                String title = getScoreboardLine(sb + position);
                if (title.length() > 32) {
                    title = title.substring(0, 31);
                }
                objective.setDisplayName(title);
            } else {
                String newLine = getScoreboardLine(sb + position);
                if (newLine.length() > 40) {
                    newLine = newLine.substring(0, 39);
                }
                if (currentLineText.containsKey(position)) {
                    scoreboard.resetScores(currentLineText.get(position));
                    scoreboard.getTeam("line" + position).removeEntry(currentLineText.get(position));
                }
                scoreboard.getTeam("line" + position).addEntry(newLine);
                objective.getScore(newLine).setScore(17 - position);
                currentLineText.put(position, newLine);
            }
        }
    }


    private void startRestartTimer() {
        restartTimer = SkyWarsReloaded.getCfg().getTimeAfterMatch();
        if (SkyWarsReloaded.get().isEnabled()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    restartTimer--;
                    if (restartTimer == 0) {
                        this.cancel();
                    }
                    updateScoreboardVar(ScoreVar.RESTARTTIME);
                }
            }.runTaskTimer(SkyWarsReloaded.get(), 0, 20);
        }
    }

    private String getScoreboardLine(String lineNum) {
        return new Messaging.MessageFormatter()
                .setVariable("mapname", gMap.getDisplayName())
                .setVariable("time", "" + Util.get().getFormattedTime(gMap.getTimer()))
                .setVariable("players", "" + gMap.getAlivePlayers().size())
                .setVariable("maxplayers", "" + gMap.getTeamCards().size() * gMap.getTeamSize())
                .setVariable("winner", SkyWarsReloaded.getCfg().usePlayerNames() ? getWinnerName(0) : getWinningTeamName())
                .setVariable("winner1", SkyWarsReloaded.getCfg().usePlayerNames() ? getWinnerName(0) : getWinningTeamName())
                .setVariable("winner2", SkyWarsReloaded.getCfg().usePlayerNames() ? getWinnerName(1) : "")
                .setVariable("winner3", SkyWarsReloaded.getCfg().usePlayerNames() ? getWinnerName(2) : "")
                .setVariable("winner4", SkyWarsReloaded.getCfg().usePlayerNames() ? getWinnerName(3) : "")
                .setVariable("winner5", SkyWarsReloaded.getCfg().usePlayerNames() ? getWinnerName(4) : "")
                .setVariable("winner6", SkyWarsReloaded.getCfg().usePlayerNames() ? getWinnerName(5) : "")
                .setVariable("winner7", SkyWarsReloaded.getCfg().usePlayerNames() ? getWinnerName(6) : "")
                .setVariable("winner8", SkyWarsReloaded.getCfg().usePlayerNames() ? getWinnerName(7) : "")
                .setVariable("restarttime", "" + restartTimer)
                .setVariable("chestvote", ChatColor.stripColor(gMap.getCurrentChest()))
                .setVariable("timevote", ChatColor.stripColor(gMap.getCurrentTime()))
                .setVariable("healthvote", ChatColor.stripColor(gMap.getCurrentHealth()))
                .setVariable("weathervote", ChatColor.stripColor(gMap.getCurrentWeather()))
                .setVariable("modifiervote", ChatColor.stripColor(gMap.getCurrentModifier()))
                .format(lineNum);
    }

    private String getWinnerName(int i) {
        if (gMap.getWinners().size() > i) {
            return gMap.getWinners().get(i);
        }
        return "";
    }

    private String getWinningTeamName() {
        if (gMap.getWinningTeam() != null) {
            return gMap.getWinningTeam().getTeamName();
        }
        return "";
    }

    private void resetScoreboard() {
        for (Team team : scoreboard.getTeams()) {
            team.unregister();
        }
        if (objective != null) {
            objective.unregister();
        }

        if (scoreboard != null) {
            scoreboard = null;
        }
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public void setRestartTimer(int i) {
        restartTimer = i;
    }
}