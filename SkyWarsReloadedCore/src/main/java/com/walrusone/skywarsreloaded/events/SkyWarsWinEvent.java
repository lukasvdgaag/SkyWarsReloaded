package com.walrusone.skywarsreloaded.events;

import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.managers.PlayerStat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SkyWarsWinEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private PlayerStat stat;
    private GameMap g;


    public SkyWarsWinEvent(PlayerStat pl, GameMap map) {
        this.stat = pl;
        this.g = map;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public PlayerStat getPlayerStat() {
        return stat;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(stat.getPlayerName());
    }

    public GameMap getGame() {
        return g;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}
