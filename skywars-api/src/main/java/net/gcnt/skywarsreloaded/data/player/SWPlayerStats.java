package net.gcnt.skywarsreloaded.data.player;

public interface SWPlayerStats {

    boolean isInitialized();

    void initData(int solo_wins,
                  int solo_kills,
                  int solo_games,
                  int team_wins,
                  int team_kills,
                  int team_games);

    int getSoloWins();

    void setSoloWins(int amount);

    int getSoloKills();

    void setSoloKills(int amount);

    int getSoloGamesPlayed();

    void setSoloGamesPlayed(int amount);

    int getTeamWins();

    void setTeamWins(int amount);

    int getTeamKills();

    void setTeamKills(int amount);

    int getTeamGamesPlayed();

    void setTeamGamesPlayed(int amount);

    int getTotalGamesPlayed();

    int getTotalWins();

    int getTotalKills();

    int getStat(PlayerStat stat);

}
