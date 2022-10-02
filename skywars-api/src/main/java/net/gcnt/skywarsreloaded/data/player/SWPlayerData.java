package net.gcnt.skywarsreloaded.data.player;

public interface SWPlayerData {

    boolean isInitialized();

    void initData(SWPlayerStats stats,
                  String selected_solo_cage,
                  String selected_team_cage,
                  String selected_particle,
                  String selected_kill_effect,
                  String selected_win_effect,
                  String selected_projectile_particle,
                  String kill_messages_theme,
                  String selected_kit);

    SWPlayerStats getStats();

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

    String getKit();

    void setKit(String kit);

}
