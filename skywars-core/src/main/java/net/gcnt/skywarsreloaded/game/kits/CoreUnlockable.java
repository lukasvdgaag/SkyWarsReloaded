package net.gcnt.skywarsreloaded.game.kits;

import net.gcnt.skywarsreloaded.data.player.PlayerStat;

import java.util.HashMap;

public abstract class CoreUnlockable implements Unlockable {

    private boolean requirePermission;
    private int cost;
    private HashMap<PlayerStat, Integer> minimumStats;

    public CoreUnlockable() {
        this.minimumStats = new HashMap<>();
        this.cost = 0;
        this.requirePermission = false;
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
    public HashMap<PlayerStat, Integer> getMinimumStats() {
        return minimumStats;
    }

    @Override
    public void addMinimumStat(PlayerStat stat, int value) {
        minimumStats.put(stat, value);
    }

}
