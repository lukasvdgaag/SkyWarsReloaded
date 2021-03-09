package com.walrusone.skywarsreloaded.game;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.enums.MatchState;
import com.walrusone.skywarsreloaded.enums.ScoreVar;
import com.walrusone.skywarsreloaded.managers.PlayerStat;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GameBoard {
    private GameMap gMap;
    private int restartTimer;

    GameBoard(GameMap gMap) {
        this.gMap = gMap;
        this.restartTimer = -1;
        setupScoreBoard();
    }

    private void setupScoreBoard() {
        // todo add new code for teams
        updateScoreboard();
    }

    public void updateScoreboard() {
        for (Player player : gMap.getAllPlayers()) {
            if (player == null) continue;
            updateScoreboard(player);
        }
    }

    public void updateScoreboard(Player player) {
        String sb = "";
        if (gMap.getMatchState() == MatchState.WAITINGSTART || gMap.getMatchState() == MatchState.WAITINGLOBBY) {
            if (gMap.getAllPlayers().size() >= gMap.getMinTeams() || (gMap.getForceStart() && gMap.getAllPlayers().size() != 0)) {
                sb = "waitboard-countdown";
            } else {
                sb = "waitboard";
            }
        } else if (gMap.getMatchState() == MatchState.PLAYING) {
            sb = "playboard";
        } else if (gMap.getMatchState() == MatchState.ENDING) {
            sb = "endboard";
            if (restartTimer == -1) {
                startRestartTimer();
            }
        }

        PlayerStat.updateScoreboard(player, sb);
    }

    public void updateScoreboardVar(ScoreVar var) {
        /*updateScoreboard();*/
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
                    updateScoreboard();
                }
            }.runTaskTimer(SkyWarsReloaded.get(), 0, 20);
        }
    }

    public int getRestartTimer() {
        return restartTimer;
    }

    public void setRestartTimer(int i) {
        restartTimer = i;
    }
}