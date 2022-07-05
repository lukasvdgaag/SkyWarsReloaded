package net.gcnt.skywarsreloaded.unlockable;

import net.gcnt.skywarsreloaded.data.player.PlayerStat;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;

import java.util.Map;

public interface Unlockable {

    String getId();

    String getPermissionPrefix();

    String getPermission();

    boolean needsPermission();

    void setNeedPermission(boolean require);

    int getCost();

    void setCost(int cost);

    Map<PlayerStat, Integer> getMinimumStats();

    void addMinimumStat(PlayerStat stat, int value);

    boolean hasUnlocked(SWPlayer player);

    boolean canUnlock(SWPlayer player);


}
