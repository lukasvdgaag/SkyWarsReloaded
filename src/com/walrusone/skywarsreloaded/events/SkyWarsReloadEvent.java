package com.walrusone.skywarsreloaded.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SkyWarsReloadEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    public SkyWarsReloadEvent() { }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}
