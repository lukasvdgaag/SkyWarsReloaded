package net.gcnt.skywarsreloaded.data;

import net.gcnt.skywarsreloaded.data.player.PlayerStat;
import net.gcnt.skywarsreloaded.game.kits.Unlockable;

import java.util.HashMap;
import java.util.Map;

public abstract class CoreUnlockable implements Unlockable {

    private boolean requirePermission;
    private int cost;
    private final Map<PlayerStat, Integer> minimumStats;

    public CoreUnlockable() {
        this(false, 0);
    }

    public CoreUnlockable(boolean requirePermission, int cost) {
        this(requirePermission, cost, null);
    }

    public CoreUnlockable(boolean requirePermission, int cost, Map<PlayerStat, Integer> minimumStats) {
        this.requirePermission = requirePermission;
        this.cost = cost;
        this.minimumStats = minimumStats == null ? new HashMap<>() : minimumStats;
    }

    @Override
    public boolean requiresPermission() {
        return requirePermission;
    }

    @Override
    public void setRequirePermission(boolean requirePermission) {
        this.requirePermission = requirePermission;
    }

    @Override
    public int getCost() {
        return cost;
    }

    @Override
    public void setCost(int cost) {
        this.cost = cost;
    }

    @Override
    public Map<PlayerStat, Integer> getMinimumStats() {
        return minimumStats;
    }

    @Override
    public void addMinimumStat(PlayerStat stat, int value) {
        minimumStats.put(stat, value);
    }

}
