package com.walrusone.skywarsreloaded.listeners;

import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.managers.MatchManager;
import me.ebonjaeger.perworldinventory.event.InventoryLoadEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PerWorldInventoryListener
        implements Listener {
    public PerWorldInventoryListener() {
    }

    @EventHandler
    public void perWorldInventoryLoad(InventoryLoadEvent e) {
        GameMap gMap = MatchManager.get().getPlayerMap(e.getPlayer());
        if (gMap != null) {
            e.setCancelled(true);
        }
    }
}
