package com.walrusone.skywarsreloaded.events;

import com.walrusone.skywarsreloaded.matchevents.MatchEvent;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SkywarsGameEventTriggerEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final MatchEvent event;
    private boolean cancelled;

    public SkywarsGameEventTriggerEvent(MatchEvent event) {
        this.event = event;
    }

    public MatchEvent getEvent() {
        return event;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean state) {
        this.cancelled = state;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
