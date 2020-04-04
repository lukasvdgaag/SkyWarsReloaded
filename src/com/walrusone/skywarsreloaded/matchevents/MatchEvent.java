package com.walrusone.skywarsreloaded.matchevents;

import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.managers.MatchManager;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.Util;
import org.bukkit.ChatColor;

public abstract class MatchEvent {
    protected GameMap gMap;
    protected boolean enabled;
    protected int min;
    protected int max;
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

    public abstract void doEvent();

    public abstract void endEvent(boolean paramBoolean);

    public abstract void saveEventData();

    public void reset() {
        fired = false;
        fireThisMatch();
        setStartTime();
    }

    private void fireThisMatch() {
        useThisMatch = false;
        if (enabled) {
            int rand = Util.get().getRandomNum(0, 100);
            if (rand < chance) {
                useThisMatch = true;
            }
        }
    }

    public boolean willFire() {
        return useThisMatch;
    }

    public void setStartTime() {
        startTime = java.util.concurrent.ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public int getStartTime() {
        return startTime;
    }

    protected void sendTitle() {
        for (org.bukkit.entity.Player player : gMap.getAlivePlayers()) {
            if (com.walrusone.skywarsreloaded.SkyWarsReloaded.getCfg().titlesEnabled()) {
                Util.get().sendTitle(player, 2, 20, 2, ChatColor.translateAlternateColorCodes('&', title),
                        ChatColor.translateAlternateColorCodes('&', subtitle));
            }
        }
        MatchManager.get().message(gMap, ChatColor.translateAlternateColorCodes('&', startMessage));
    }

    public void announceTimer() {
        int v1 = startTime - gMap.getTimer();
        String time;
        if (v1 % 60 == 0) {
            time = v1 / 60 + " " + (v1 > 60 ? new Messaging.MessageFormatter().format("timer.minutes") : new Messaging.MessageFormatter().format("timer.minute"));
        } else {
            if ((v1 >= 60) || ((v1 % 10 != 0) && (v1 >= 10)) || (v1 <= 0)) {
                return;
            }
            time = v1 + " " + (v1 > 1 ? new Messaging.MessageFormatter().format("timer.seconds") : new Messaging.MessageFormatter().format("timer.second"));
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
}
