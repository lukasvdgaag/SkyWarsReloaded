package com.walrusone.skywarsreloaded.events;

import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.menus.gameoptions.objects.GameKit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SkyWarsSelectKitEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private GameMap map;
    private GameKit kit;

    public SkyWarsSelectKitEvent(Player p, GameMap game, GameKit kit) {
        this.player = p;
        this.map = game;
        this.kit = kit;
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

    public GameKit getKit() {
        return kit;
    }

}
