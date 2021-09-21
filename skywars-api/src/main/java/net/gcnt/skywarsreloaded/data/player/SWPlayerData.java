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
                  String selected_projectile_effect);
}
