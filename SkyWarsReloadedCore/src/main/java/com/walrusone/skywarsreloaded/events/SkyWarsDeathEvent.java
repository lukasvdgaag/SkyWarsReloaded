package com.walrusone.skywarsreloaded.events;

import com.walrusone.skywarsreloaded.game.GameMap;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;

public class SkyWarsDeathEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private GameMap map;
    private EntityDamageEvent.DamageCause cause;

    public SkyWarsDeathEvent(Player p, EntityDamageEvent.DamageCause cause, GameMap game) {
        this.player = p;
        this.map = game;
        this.cause = cause;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public GameMap getGame() {
        return map;
    }

    public EntityDamageEvent.DamageCause getCause() {
        return cause;
    }
}
