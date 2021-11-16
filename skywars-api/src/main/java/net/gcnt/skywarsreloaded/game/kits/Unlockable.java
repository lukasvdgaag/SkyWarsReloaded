package net.gcnt.skywarsreloaded.game.kits;

import net.gcnt.skywarsreloaded.data.player.PlayerStat;

import java.util.HashMap;

public interface Unlockable {

    boolean requiresPermission();

    void setRequirePermission(boolean require);

    int getCost();

    void setCost(int cost);

    HashMap<PlayerStat, Integer> getMinimumStats();

    void addMinimumStat(PlayerStat stat, int value);


}
