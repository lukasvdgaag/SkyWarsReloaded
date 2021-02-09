package com.walrusone.skywarsreloaded.events;

import com.walrusone.skywarsreloaded.enums.MatchState;
import com.walrusone.skywarsreloaded.game.GameMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;

public class SkyWarsMatchStateChangeEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private GameMap map;
    private MatchState state;
    private boolean async;

    public SkyWarsMatchStateChangeEvent(GameMap game, MatchState stateIn) {
        this.map = game;
        this.state = stateIn;
        this.async = !Bukkit.isPrimaryThread();
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public GameMap getGame() {
        return map;
    }

    public MatchState getState() {
        return state;
    }

    public boolean isAsync() {
        return async;
    }
}
