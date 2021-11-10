package net.gcnt.skywarsreloaded.game.kits;

import java.util.HashMap;

public interface KitRequirements {

    SWKit getKit();

    boolean requiresPermission();

    void setRequirePermission(boolean require);

    int getCost();

    void setCost(int cost);

    HashMap<String, Integer> getMinimumStats();

    void addMinimumStat(String stat, int value);


}
