package net.gcnt.skywarsreloaded.bukkit.listener;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.listener.AbstractSWEventListener;
import net.gcnt.skywarsreloaded.utils.CoreSWCoord;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.wrapper.event.*;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.*;

public class BukkitSWEventListener extends AbstractSWEventListener implements Listener {

    public BukkitSWEventListener(SkyWarsReloaded pluginIn) {
        super(pluginIn);
    }

    @EventHandler
    public void onPlayerLogin(AsyncPlayerPreLoginEvent event) {
        // Get data
        SWAsyncPlayerPreLoginEvent.Result result = SWAsyncPlayerPreLoginEvent.Result.valueOf(event.getLoginResult().name());

        // Fire event
        SWAsyncPlayerPreLoginEvent swEvent = new CoreSWAsyncPlayerPreLoginEvent(event.getUniqueId(), event.getName(), event.getAddress(), result);
        this.onAsyncPlayerPreLogin(swEvent);

        // Update changes
        event.setLoginResult(AsyncPlayerPreLoginEvent.Result.valueOf(swEvent.getResult().name()));
        event.setKickMessage(swEvent.getKickMessage());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Get data
        SWPlayer p = this.getPlayerFromBukkitPlayer(event.getPlayer());

        // Fire Event
        SWPlayerJoinEvent swEvent = new CoreSWPlayerJoinEvent(p, event.getJoinMessage());
        this.onPlayerJoin(swEvent);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Get data
        SWPlayer p = this.getPlayerFromBukkitPlayer(event.getPlayer());

        // Fire Event
        SWPlayerQuitEvent swEvent = new CoreSWPlayerQuitEvent(p, event.getQuitMessage());
        this.onPlayerQuit(swEvent);
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        // Get data
        SWPlayer p = this.getPlayerFromBukkitPlayer(event.getPlayer());

        SWCoord location = null;
        Block block = event.getClickedBlock();
        String blockType = "AIR";

        if (block != null) {
            Location loc = block.getLocation();
            location = new CoreSWCoord(plugin.getUtils().getSWWorld(loc.getWorld().getName()), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
            blockType = block.getType().name();
        }
        SWPlayerInteractEvent.Action action = SWPlayerInteractEvent.Action.valueOf(event.getAction().name());

        // Fire Event
        SWPlayerInteractEvent swEvent = new CoreSWPlayerInteractEvent(p, location, blockType, action);
        this.onPlayerInteract(swEvent);

        // Update changes
        if (swEvent.isCancelled()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerBlockBreakEvent(BlockBreakEvent event) {
        // Get data
        SWPlayer p = this.getPlayerFromBukkitPlayer(event.getPlayer());
        Block block = event.getBlock();
        Location loc = block.getLocation();
        SWCoord coord = new CoreSWCoord(plugin.getUtils().getSWWorld(loc.getWorld().getName()), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        String wName = block.getType().name();

        // Fire core event
        SWBlockBreakEvent swEvent = new CoreSWBlockBreakEvent(p, coord, wName);
        this.onPlayerBlockBreak(swEvent);

        // Update changes
        if (swEvent.isCancelled()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent e) {
        SWPlayer p = this.getPlayerFromBukkitPlayer(e.getPlayer());

        SWAsyncPlayerChatEvent swEvent = new CoreSWAsyncPlayerChatEvent(p, e.getMessage());
        this.onAsyncPlayerChat(swEvent);

        e.setMessage(swEvent.getMessage());
        if (swEvent.isCancelled()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerBlockPlaceEvent(BlockPlaceEvent event) {
        // Get data
        SWPlayer p = this.getPlayerFromBukkitPlayer(event.getPlayer());
        Block block = event.getBlock();
        Location loc = block.getLocation();
        SWCoord coord = new CoreSWCoord(plugin.getUtils().getSWWorld(loc.getWorld().getName()), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        String wName = event.getBlockPlaced().getType().name();

        // Fire core event
        SWBlockPlaceEvent swEvent = new CoreSWBlockPlaceEvent(p, coord, wName);
        this.onPlayerBlockPlace(swEvent);

        // Update changes
        if (swEvent.isCancelled()) {
            event.setCancelled(true);
        }
    }

    // Utils

    private SWPlayer getPlayerFromBukkitPlayer(Player player) {
        return this.plugin.getPlayerManager().initPlayer(player.getUniqueId());
    }

}
