package net.gcnt.skywarsreloaded.bukkit.listener;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.utils.BukkitItem;
import net.gcnt.skywarsreloaded.bukkit.wrapper.event.BukkitSWPlayerFoodLevelChangeEvent;
import net.gcnt.skywarsreloaded.listener.AbstractSWEventListener;
import net.gcnt.skywarsreloaded.utils.CoreSWCoord;
import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.wrapper.event.*;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;
import net.gcnt.skywarsreloaded.wrapper.world.SWWorld;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.WorldInitEvent;

public class BukkitSWEventListener extends AbstractSWEventListener implements Listener {

    public BukkitSWEventListener(SkyWarsReloaded pluginIn) {
        super(pluginIn);
    }

    @EventHandler
    public void onPlayerLogin(AsyncPlayerPreLoginEvent event) {
        // Get data
        SWAsyncPlayerPreLoginEvent.Result result = SWAsyncPlayerPreLoginEvent.Result.valueOf(event.getLoginResult().name());

        // Fire event
        SWAsyncPlayerPreLoginEvent swEvent = new CoreSWAsyncPlayerPreLoginEvent(event.getUniqueId(), event.getName(),
                event.getAddress(), result, null);
        this.onAsyncPlayerPreLogin(swEvent);

        // Update changes
        SWAsyncPlayerPreLoginEvent.Result updatedResult = swEvent.getResult();
        AsyncPlayerPreLoginEvent.Result updatedResultBukkit = AsyncPlayerPreLoginEvent.Result.valueOf(updatedResult.name());
        event.setLoginResult(updatedResultBukkit);
        if (!updatedResult.equals(SWAsyncPlayerPreLoginEvent.Result.ALLOWED)) {
            event.setKickMessage(swEvent.getKickMessage());
        }
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

    @EventHandler
    public void onChunkLoadEvent(ChunkLoadEvent event) {
        // Get data
        int x = event.getChunk().getX();
        int z = event.getChunk().getZ();
        SWWorld world = this.plugin.getServer().getWorld(event.getWorld().getName());

        // Fire core event
        SWChunkLoadEvent swEvent = new CoreSWChunkLoadEvent(world, x, z, event.isNewChunk());
        this.onChunkLoad(swEvent);
    }

    @EventHandler
    public void onWorldInitEvent(WorldInitEvent event) {
        // Get data
        SWWorld world = this.plugin.getServer().getWorld(event.getWorld().getName());

        // Fire core event
        SWWorldInitEvent swEvent = new CoreSWWorldInitEvent(world);
        this.onWorldInit(swEvent);
    }

    @EventHandler
    public void onPlayerFoodLevelChange(FoodLevelChangeEvent event) {
        Player player = (Player) event.getEntity();
        SWPlayer p = this.getPlayerFromBukkitPlayer(player);

        SWPlayerFoodLevelChangeEvent swEvent = new BukkitSWPlayerFoodLevelChangeEvent(event, p, event.getFoodLevel(), BukkitItem.fromBukkit(plugin, event.getItem()));
        this.onPlayerFoodLevelChange(swEvent);
    }

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        // Get data
        SWPlayer p = this.getPlayerFromBukkitPlayer(event.getPlayer());
        Location from = event.getFrom();
        Location to = event.getTo();

        if (from.getWorld() == null || to == null || to.getWorld() == null) return;

        SWCoord coordFrom = new CoreSWCoord(plugin.getUtils().getSWWorld(from.getWorld().getName()),
                from.getBlockX(), from.getBlockY(), from.getBlockZ());
        SWCoord coordTo = new CoreSWCoord(plugin.getUtils().getSWWorld(to.getWorld().getName()),
                to.getBlockX(), to.getBlockY(), to.getBlockZ());

        // Fire core event
        SWPlayerMoveEvent swEvent = new CoreSWPlayerMoveEvent(p, coordFrom, coordTo);
        this.onPlayerMove(swEvent);

        // Update changes
        if (swEvent.isCancelled()) {
            event.setCancelled(true);
        }
    }

    // Utils

    private SWPlayer getPlayerFromBukkitPlayer(Player player) {
        return this.plugin.getPlayerManager().getPlayerByUUID(player.getUniqueId());
    }

}