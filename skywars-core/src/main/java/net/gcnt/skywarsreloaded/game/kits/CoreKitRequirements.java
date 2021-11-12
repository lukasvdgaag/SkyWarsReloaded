package net.gcnt.skywarsreloaded.game.kits;

import net.gcnt.skywarsreloaded.data.player.PlayerStat;

import java.util.HashMap;

public class CoreKitRequirements implements KitRequirements {

    private final SWKit kit;
    private boolean requirePermission;
    private int cost;
    private int level;
    private int experience;
    private HashMap<PlayerStat, Integer> minimumStats;

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
    public int getLevel() {
        return level;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public int getExperience() {
        return experience;
    }

    @Override
    public void setExperience(int experience) {
        this.experience = experience;
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
