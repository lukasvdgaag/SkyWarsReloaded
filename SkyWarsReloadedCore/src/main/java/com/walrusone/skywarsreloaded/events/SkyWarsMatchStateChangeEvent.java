package com.walrusone.skywarsreloaded.events;

import com.walrusone.skywarsreloaded.enums.MatchState;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.utilities.SWRServer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;

public class SkyWarsMatchStateChangeEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private GameMap map;
    private SWRServer server;
    private MatchState state;
    private boolean async;
    private boolean bungeecord;

    public SkyWarsMatchStateChangeEvent(GameMap gameIn, MatchState stateIn) {
        this.map = gameIn;
        this.server = null;
        this.state = stateIn;
        this.async = !Bukkit.isPrimaryThread();
        this.bungeecord = false;
    }

    public SkyWarsMatchStateChangeEvent(SWRServer serverIn, MatchState stateIn) {
        this.map = null;
        this.server = serverIn;
        this.state = stateIn;
        this.async = !Bukkit.isPrimaryThread();
        this.bungeecord = true;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public GameMap getGameMap() throws Exception {
        if (this.bungeecord) throw new Exception("Cannot get GameMap while running in bungeecord mode. Use SWRServer instead!");
        return this.map;
    }

    public SWRServer getSWRServer() throws Exception {
        if (!this.bungeecord) throw new Exception("Cannot get SWRServer while running in local mode. Use GameMap instead!");
        return this.server;
    }

    public MatchState getState() {
        return this.state;
    }

    public boolean isAsync() {
        return this.async;
    }

    public boolean isBungeecord() {
        return this.bungeecord;
    }
}
