package com.walrusone.skywarsreloaded.matchevents;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.events.SkywarsGameEventAnnounceEvent;
import com.walrusone.skywarsreloaded.events.SkywarsGameEventTriggerEvent;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.managers.MatchManager;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.Util;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.Nullable;

public abstract class MatchEvent {
    protected GameMap gMap;
    protected boolean enabled;
    protected int min;
    protected int max;
    // Temporarily store min and max values for the event. Should not save to config.
    protected Integer minOverride;
    protected Integer maxOverride;
    protected int length;
    protected int startTime;
    protected int chance;
    protected boolean fired;
    protected boolean repeatable;
    protected String title;
    protected String subtitle;
    protected String startMessage;
    protected String endMessage;
    protected boolean announceEvent;
    protected org.bukkit.inventory.ItemStack material;
    protected int slot;
    protected String eventName;
    private boolean useThisMatch;

    public MatchEvent() {
    }

    public void doEvent() {
        SkyWarsReloaded swr = SkyWarsReloaded.get();

        SkywarsGameEventTriggerEvent bukkitEvent = new SkywarsGameEventTriggerEvent(this);
        swr.getServer().getPluginManager().callEvent(bukkitEvent);

        if (bukkitEvent.isCancelled()) {
            return;
        }

        onDoEvent();
    }
    
    public abstract void onDoEvent();

    public abstract void endEvent(boolean paramBoolean);

    public abstract void saveEventData();

    public void reset() {
        fired = false;
        fireThisMatch();
        resetStartTime();
    }

    private void fireThisMatch() {
        if (!enabled) return;

        useThisMatch = false;
        if (enabled) {
            int rand = Util.get().getRandomNum(0, 100);
            if (rand < chance) {
                useThisMatch = true;
            }
        }
    }

    public boolean willFire() {
        if (!enabled) return false;

        return useThisMatch;
    }

    public void resetStartTime() {
        if (!enabled) return;

        startTime = java.util.concurrent.ThreadLocalRandom.current().nextInt(getMin(), getMax() + 1);
    }

    /**
     * Change start time of event
     * @param updatedStartTime In seconds
     */
    public void setStartTime(int updatedStartTime) {
        startTime = updatedStartTime;
    }

    public int getStartTime() {
        if (!enabled) return -1;

        return startTime;
    }

    protected void sendTitle() {
        if (!enabled) return;

        for (org.bukkit.entity.Player player : gMap.getAlivePlayers()) {
            if (com.walrusone.skywarsreloaded.SkyWarsReloaded.getCfg().titlesEnabled()) {
                Util.get().sendTitle(player, 2, 20, 2, ChatColor.translateAlternateColorCodes('&', title),
                        ChatColor.translateAlternateColorCodes('&', subtitle));
            }
        }
        MatchManager.get().message(gMap, ChatColor.translateAlternateColorCodes('&', startMessage));
    }

    public void announceTimer() {
        if (!enabled) return;

        // Bukkit event
        SkyWarsReloaded swr = SkyWarsReloaded.get();
        SkywarsGameEventAnnounceEvent bukkitEvent = new SkywarsGameEventAnnounceEvent(this);
        swr.getServer().getPluginManager().callEvent(bukkitEvent);
        if (bukkitEvent.isCancelled()) {
            return;
        }

        int remainingTime = startTime - gMap.getTimer();
        String time;
        if (remainingTime % 60 == 0) {
            time = remainingTime / 60 + " " + (remainingTime > 60 ? new Messaging.MessageFormatter().format("timer.minutes") : new Messaging.MessageFormatter().format("timer.minute"));
        } else {
            if ((remainingTime >= 60) || ((remainingTime % 10 != 0) && (remainingTime >= 10)) || (remainingTime <= 0)) {
                return;
            }
            time = remainingTime + " " + (remainingTime > 1 ? new Messaging.MessageFormatter().format("timer.seconds") : new Messaging.MessageFormatter().format("timer.second"));
        }
        MatchManager.get().message(gMap, new Messaging.MessageFormatter().setVariable("event", title).setVariable("time", time).format("event.announce"));
    }

    public boolean fired() {
        return fired;
    }

    public boolean announceEnabled() {
        return announceEvent;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean b) {
        enabled = b;
    }

    public String getTitle() {
        return ChatColor.translateAlternateColorCodes('&', title);
    }

    public org.bukkit.inventory.ItemStack getMaterial() {
        return material;
    }

    public int getSlot() {
        return slot;
    }

    public GameMap getGameMap() {
        return gMap;
    }

    public boolean hasFired() {
        return fired;
    }

    public boolean isRepeatable() {
        return repeatable;
    }

    public int getMin() {
        return minOverride != null ? minOverride : min;
    }

    public int getMax() {
        return maxOverride != null ? maxOverride : max;
    }

    public void setTempMin(@Nullable Integer min) {
        minOverride = min;
    }

    public void setTempMax(@Nullable Integer max) {
        maxOverride = max;
    }
}
