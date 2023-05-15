package com.walrusone.skywarsreloaded.matchevents;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.events.SkywarsGameEventAnnounceEvent;
import com.walrusone.skywarsreloaded.events.SkywarsGameEventTriggerEvent;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.managers.MatchManager;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.Util;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
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

    public boolean announceEnabled() {
        return announceEvent;
    }

    public void setAnnounceEvent(boolean announceEvent) {
        this.announceEvent = announceEvent;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean b) {
        enabled = b;
    }

    public String getStartMessage() {
        return startMessage;
    }

    public void setStartMessage(String startMessage) {
        this.startMessage = startMessage;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEndMessage() {
        return endMessage;
    }

    public void setEndMessage(String endMessage) {
        this.endMessage = endMessage;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getChance() {
        return chance;
    }

    public void setChance(int chance) {
        this.chance = chance;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public boolean isAnnounceEvent() {
        return announceEvent;
    }

    public boolean isUseThisMatch() {
        return useThisMatch;
    }

    public void setUseThisMatch(boolean useThisMatch) {
        this.useThisMatch = useThisMatch;
    }

    public String getTitle() {
        return ChatColor.translateAlternateColorCodes('&', title);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public org.bukkit.inventory.ItemStack getMaterial() {
        return material;
    }

    public void setMaterial(ItemStack material) {
        this.material = material;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public GameMap getGameMap() {
        return gMap;
    }

    public boolean hasFired() {
        return fired;
    }

    public void setFired(boolean fired) {
        this.fired = fired;
    }

    public boolean isRepeatable() {
        return repeatable;
    }

    public void setRepeatable(boolean repeatable) {
        this.repeatable = repeatable;
    }

    public int getMin() {
        return minOverride != null ? minOverride : min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return maxOverride != null ? maxOverride : max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public Integer getMinOverride() {
        return minOverride;
    }

    public void setMinOverride(@Nullable Integer min) {
        minOverride = min;
    }

    public Integer getMaxOverride() {
        return maxOverride;
    }

    public void setMaxOverride(@Nullable Integer max) {
        maxOverride = max;
    }
}
