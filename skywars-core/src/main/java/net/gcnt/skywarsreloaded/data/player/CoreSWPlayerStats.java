package net.gcnt.skywarsreloaded.data.player;

import net.gcnt.skywarsreloaded.data.player.stats.PlayerStat;
import net.gcnt.skywarsreloaded.data.player.stats.SWPlayerStats;

public class CoreSWPlayerStats implements SWPlayerStats {

    private boolean initialized;

    private int soloWins;
    private int soloKills;
    private int soloGames;
    private int teamWins;
    private int teamKills;
    private int teamGames;

    public CoreSWPlayerStats() {
        this.initialized = false;
    }

    @Override
    public void initData(int solo_wins,
                         int solo_kills,
                         int solo_games,
                         int team_wins,
                         int team_kills,
                         int team_games) {
        this.soloWins = solo_wins;
        this.soloKills = solo_kills;
        this.soloGames = solo_games;
        this.teamWins = team_wins;
        this.teamKills = team_kills;
        this.teamGames = team_games;
        this.initialized = true;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public int getSoloWins() {
        return soloWins;
    }

    @Override
    public void setSoloWins(int amount) {
        soloWins = amount;
    }

    @Override
    public int getSoloKills() {
        return soloKills;
    }

    @Override
    public void setSoloKills(int amount) {
        soloKills = amount;
    }

    @Override
    public int getSoloGamesPlayed() {
        return soloGames;
    }

    @Override
    public void setSoloGamesPlayed(int amount) {
        soloGames = amount;
    }

    @Override
    public int getTeamWins() {
        return teamWins;
    }

    @Override
    public void setTeamWins(int amount) {
        teamWins = amount;
    }

    @Override
    public int getTeamKills() {
        return teamKills;
    }

    @Override
    public void setTeamKills(int amount) {
        teamKills = amount;
    }

    @Override
    public int getTeamGamesPlayed() {
        return teamGames;
    }

    @Override
    public void setTeamGamesPlayed(int amount) {
        teamGames = amount;
    }

    @Override
    public int getTotalGamesPlayed() {
        return teamGames + soloGames;
    }

    @Override
    public int getTotalWins() {
        return teamWins + soloWins;
    }

    @Override
    public int getTotalKills() {
        return teamKills + soloKills;
    }

    @Override
    public int getStat(PlayerStat stat) {
        switch (stat) {
            case SOLO_WINS:
                return this.getSoloWins();
            case SOLO_GAMES:
                return this.getSoloGamesPlayed();
            case SOLO_KILLS:
                return this.getSoloKills();
            case TEAM_WINS:
                return this.getTeamWins();
            case TEAM_GAMES:
                return this.getTeamGamesPlayed();
            case TEAM_KILLS:
                return this.getTeamKills();
            case TOTAL_GAMES:
                return this.getTotalGamesPlayed();
            case TOTAL_WINS:
                return this.getTotalWins();
            case TOTAL_KILLS:
                return this.getTotalKills();
            default:
                return 0;
            // todo implement player stats that haven't been added yet (level, exp).
        }
    }
}
