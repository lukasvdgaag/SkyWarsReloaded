package net.gcnt.skywarsreloaded.game.kits;

import java.util.HashMap;

public class CoreKitRequirements implements KitRequirements {

    private final SWKit kit;
    private boolean requirePermission;
    private int cost;
    private HashMap<String, Integer> minimumStats;

    public CoreKitRequirements(SWKit kit) {
        this.kit = kit;
        this.minimumStats = new HashMap<>();
        this.cost = 0;
        this.requirePermission = false;
    }

    @Override
    public SWKit getKit() {
        return kit;
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
    public HashMap<String, Integer> getMinimumStats() {
        return minimumStats;
    }

    @Override
    public void addMinimumStat(String stat, int value) {
        minimumStats.put(stat, value);
    }

}
