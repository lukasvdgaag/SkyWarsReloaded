package net.gcnt.skywarsreloaded.game.kits;

import net.gcnt.skywarsreloaded.data.player.PlayerStat;

import java.util.HashMap;

public interface KitRequirements {

    SWKit getKit();

    boolean requiresPermission();

    void setRequirePermission(boolean require);

    int getCost();

    void setCost(int cost);

    int getLevel();

    void setLevel(int level);

    int getExperience();

    void setExperience(int experience);

    HashMap<PlayerStat, Integer> getMinimumStats();

    void addMinimumStat(PlayerStat stat, int value);


}
