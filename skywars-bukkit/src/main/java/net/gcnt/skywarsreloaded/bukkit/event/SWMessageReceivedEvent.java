package net.gcnt.skywarsreloaded.bukkit.event;

import net.gcnt.skywarsreloaded.data.messaging.SWMessage;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class SWMessageReceivedEvent extends Event implements net.gcnt.skywarsreloaded.event.SWMessageReceivedEvent {

    private static final HandlerList handlers = new HandlerList();
    private final SWMessage message;

    public SWMessageReceivedEvent(SWMessage message) {
        this.message = message;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public SWMessage getMessage() {
        return message;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}
