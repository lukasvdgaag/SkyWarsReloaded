package net.gcnt.skywarsreloaded.bukkit.listener;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.listener.AbstractSWEventListener;
import net.gcnt.skywarsreloaded.utils.CoreSWCoord;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.wrapper.event.CoreSWBlockPlaceEvent;
import net.gcnt.skywarsreloaded.wrapper.player.AbstractSWOfflinePlayer;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class BukkitSWEventListener extends AbstractSWEventListener implements Listener {

    public BukkitSWEventListener(SkyWarsReloaded pluginIn) {
        super(pluginIn);
    }

    @EventHandler
    public void onPlayerLogin(AsyncPlayerPreLoginEvent event) {
        this.onAsyncPlayerPreLogin(new AbstractSWOfflinePlayer(event.getUniqueId(), false));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.onPlayerJoin(this.getPlayerFromBukkitPlayer(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.onPlayerQuit(this.getPlayerFromBukkitPlayer(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        this.onPlayerInteract(this.getPlayerFromBukkitPlayer(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerBlockBreakEvent(BlockBreakEvent event) {
        this.onPlayerBlockBreak(this.getPlayerFromBukkitPlayer(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerBlockPlaceEvent(BlockPlaceEvent event) {
        SWPlayer p = this.getPlayerFromBukkitPlayer(event.getPlayer());
        Block block = event.getBlock();
        Location loc = block.getLocation();
        SWCoord coord = new CoreSWCoord(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        String wName = event.getBlockPlaced().getType().name();
        this.onPlayerBlockPlace(new CoreSWBlockPlaceEvent(p, loc.getWorld().getName(), coord, wName));
    }

    // Utils

    private SWPlayer getPlayerFromBukkitPlayer(Player player) {
        return this.plugin.getPlayerManager().initPlayer(player.getUniqueId());
    }

}
