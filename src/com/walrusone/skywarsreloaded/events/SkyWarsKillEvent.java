package com.walrusone.skywarsreloaded.events;

import com.walrusone.skywarsreloaded.game.GameMap;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SkyWarsKillEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Player killer;
    private Player killed;
    private GameMap map;

    public SkyWarsKillEvent(Player killer, Player killed, GameMap game) {
        this.killed = killed;
        this.killer = killer;
        this.map = game;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public Player getKiller() {
        return killer;
    }

    public Player getKilled() {
        return killed;
    }

    public GameMap getGame() {
        return map;
    }

}
