package net.gcnt.skywarsreloaded.unlockable;

import net.gcnt.skywarsreloaded.data.player.PlayerStat;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;

import java.util.HashMap;
import java.util.Map;

public abstract class CoreUnlockable implements Unlockable {

    private final String id;
    private boolean requirePermission;
    private int cost;
    private final Map<PlayerStat, Integer> minimumStats;

    public CoreUnlockable(String id) {
        this(id, false, 0);
    }

    public CoreUnlockable(String id, boolean requirePermission, int cost) {
        this(id, requirePermission, cost, null);
    }

    public CoreUnlockable(String id, boolean requirePermission, int cost, Map<PlayerStat, Integer> minimumStats) {
        this.id = id;
        this.requirePermission = requirePermission;
        this.cost = cost;
        this.minimumStats = minimumStats == null ? new HashMap<>() : minimumStats;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getPermission() {
        return getPermissionPrefix() + getId();
    }

    @Override
    public boolean needsPermission() {
        return requirePermission;
    }

    @Override
    public void setNeedPermission(boolean requirePermission) {
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

    @Override
    public boolean canUnlock(SWPlayer player) {
        return !needsPermission() || player.hasPermission(getPermissionPrefix() + getId());
    }

    @Override
    public boolean hasUnlocked(SWPlayer player) {
        if (!canUnlock(player)) return false;
        final String permission = getPermissionPrefix() + getId();

        // Has permission -> true
        // todo add option to require all stats before able to buy.

        // needPermission = true -> check if they have permission
        //   no perm = return false
        //   yes perm = check stats
        //      missing 1 stat = return false
        //      all stats = return true

        // EXAMPLE: Free kit (perm: false, no stats, no price), Normal Case (perm: false, some stat or some price)
        // needPermission: false
        // no stats /

        // EXAMPLE: Permission or stats
        // needPermission: false
        // some stats

        // EXAMPLE: Need permission to be able to unlock with stats
        // needPermission: true
        // some stats

        // EXAMPLE: Exclusively unlockable with permission
        // needPermission: true
        // no stats

        // needPermission = false -> check if they have permission
        //   yes perm = return true (instant access)
        //   no perm = check stats ->
        //      missing 1 stat = return false
        //      all stats = return true


        if (!needsPermission() && player.hasPermission(permission)) {
            return true;

            // Doesn't have perm and unlockable needs it
        } else if (needsPermission()) {
            return false;
        }
        // todo check if player has this unlockable in a list or smt.

        // Check stats
        for (Map.Entry<PlayerStat, Integer> requirement : getMinimumStats().entrySet()) {
            if (player.getPlayerData().getStats().getStat(requirement.getKey()) < requirement.getValue()) return false;
        }

        return true;
    }
}
