package net.gcnt.skywarsreloaded.data.player;

public interface SWPlayerData {

    boolean isInitialized();

    void initData(int solo_wins,
                  int solo_kills,
                  int solo_games,
                  int team_wins,
                  int team_kills,
                  int team_games,
                  String selected_solo_cage,
                  String selected_team_cage,
                  String selected_particle,
                  String selected_kill_effect,
                  String selected_win_effect,
                  String selected_projectile_particle,
                  String kill_messages_theme);

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

    String getSoloCage();

    void setSoloCage(String value);

    String getTeamCage();

    void setTeamCage(String value);

    String getParticle();

    void setParticle(String value);

    String getKillEffect();

    void setKillEffect(String value);

    String getWinEffect();

    void setWinEffect(String value);

    String getProjectileParticle();

    void setProjectileParticle(String value);

    String getKillMessagesTheme();

    void setKillMessagesTheme(String value);

}
