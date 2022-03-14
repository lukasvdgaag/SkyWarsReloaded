package net.gcnt.skywarsreloaded.game.kits;

import net.gcnt.skywarsreloaded.data.player.PlayerStat;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;

import java.util.Map;

public interface Unlockable {

    String getId();

    String getPermissionPrefix();

    boolean needsPermission();

    void setNeedPermission(boolean require);

    int getCost();

    void setCost(int cost);

    Map<PlayerStat, Integer> getMinimumStats();

    void addMinimumStat(PlayerStat stat, int value);

    boolean hasUnlocked(SWPlayer player);

    boolean canUnlock(SWPlayer player);


}
