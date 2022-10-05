package net.gcnt.skywarsreloaded.bukkit.listener;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.managers.BukkitInventoryManager;
import net.gcnt.skywarsreloaded.bukkit.utils.BukkitItem;
import net.gcnt.skywarsreloaded.bukkit.wrapper.event.BukkitSWPlayerFoodLevelChangeEvent;
import net.gcnt.skywarsreloaded.enums.DeathReason;
import net.gcnt.skywarsreloaded.listener.PlatformSWEventListener;
import net.gcnt.skywarsreloaded.utils.CoreSWCoord;
import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.utils.gui.SWGui;
import net.gcnt.skywarsreloaded.utils.gui.SWGuiClickHandler;
import net.gcnt.skywarsreloaded.wrapper.entity.SWEntity;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;
import net.gcnt.skywarsreloaded.wrapper.event.*;
import net.gcnt.skywarsreloaded.wrapper.server.SWInventory;
import net.gcnt.skywarsreloaded.wrapper.world.SWWorld;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BukkitSWEventListener implements Listener, PlatformSWEventListener {

    private final SkyWarsReloaded plugin;

    public BukkitSWEventListener(SkyWarsReloaded plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerLogin(AsyncPlayerPreLoginEvent event) {
        // Get data
        SWAsyncPlayerPreLoginEvent.Result result = SWAsyncPlayerPreLoginEvent.Result.valueOf(event.getLoginResult().name());

        // Fire event
        SWAsyncPlayerPreLoginEvent swEvent = new CoreSWAsyncPlayerPreLoginEvent(event.getUniqueId(), event.getName(),
                event.getAddress(), result, null);

        plugin.getEventManager().callEvent(swEvent);

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
        plugin.getEventManager().callEvent(swEvent);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Get data
        SWPlayer p = this.getPlayerFromBukkitPlayer(event.getPlayer());

        // Fire Event
        SWPlayerQuitEvent swEvent = new CoreSWPlayerQuitEvent(p, event.getQuitMessage());
        plugin.getEventManager().callEvent(swEvent);
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
        plugin.getEventManager().callEvent(swEvent);

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
        plugin.getEventManager().callEvent(swEvent);

        // Update changes
        if (swEvent.isCancelled()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent e) {
        SWPlayer p = this.getPlayerFromBukkitPlayer(e.getPlayer());

        SWAsyncPlayerChatEvent swEvent = new CoreSWAsyncPlayerChatEvent(p, e.getMessage());
        plugin.getEventManager().callEvent(swEvent);

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
        plugin.getEventManager().callEvent(swEvent);

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
        plugin.getEventManager().callEvent(swEvent);
    }

    @EventHandler
    public void onWorldInitEvent(WorldInitEvent event) {
        // Get data
        SWWorld world = this.plugin.getServer().getWorld(event.getWorld().getName());

        // Fire core event
        SWWorldInitEvent swEvent = new CoreSWWorldInitEvent(world);
        plugin.getEventManager().callEvent(swEvent);
    }

    @EventHandler
    public void onPlayerFoodLevelChange(FoodLevelChangeEvent event) {
        Player player = (Player) event.getEntity();
        SWPlayer p = this.getPlayerFromBukkitPlayer(player);

        SWPlayerFoodLevelChangeEvent swEvent = new BukkitSWPlayerFoodLevelChangeEvent(
                event, p, event.getFoodLevel(),
                new BukkitItem(plugin, event.getItem())
        );
        plugin.getEventManager().callEvent(swEvent);
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
        plugin.getEventManager().callEvent(swEvent);

        // Update changes
        if (swEvent.isCancelled()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        // Get data
        SWEntity entity = plugin.getEntityManager().getEntityByUUID(event.getEntity().getUniqueId());
        DeathReason reason = DeathReason.fromString(event.getCause().name());
        double damage = event.getDamage();
        double finalDamage = event.getFinalDamage();

        // Fire core event
        SWEntityDamageEvent swEvent = new CoreSWEntityDamageEvent(entity, damage, finalDamage, reason);
        plugin.getEventManager().callEvent(swEvent);

        // Update changes
        if (swEvent.isCancelled()) {
            event.setCancelled(true);
        }
        event.setDamage(swEvent.getDamage());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        // Get data
        SWPlayer p = this.getPlayerFromBukkitPlayer(event.getEntity());

        List<Item> drops = new ArrayList<>();
        for (ItemStack stack : event.getDrops()) {
            drops.add(new BukkitItem(plugin, stack));
        }

        // Fire core event
        SWPlayerDeathEvent swEvent = new CoreSWPlayerDeathEvent(p, event.getDeathMessage(), event.getKeepInventory(), drops);
        plugin.getEventManager().callEvent(swEvent);

        // Update changes
        event.setDeathMessage(swEvent.getDeathMessage());
        event.setKeepInventory(swEvent.isKeepInventory());
        event.getDrops().clear();
        event.getDrops().addAll(swEvent.getDrops().stream().map(item -> ((BukkitItem) item).getBukkitItem()).collect(Collectors.toList()));
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // Get data
        SWEntity entity = plugin.getEntityManager().getEntityByUUID(event.getEntity().getUniqueId());
        SWEntity damager = plugin.getEntityManager().getEntityByUUID(event.getDamager().getUniqueId());
        DeathReason reason = DeathReason.fromString(event.getCause().name());
        double damage = event.getDamage();
        double finalDamage = event.getFinalDamage();

        // Fire core event
        SWEntityDamageByEntityEvent swEvent = new CoreSWEntityDamageByEntityEvent(entity, damager, damage, finalDamage, reason);
        plugin.getEventManager().callEvent(swEvent);

        // Update changes
        if (swEvent.isCancelled()) {
            event.setCancelled(true);
        }
        event.setDamage(swEvent.getDamage());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        SWInventory inv = ((BukkitInventoryManager) this.plugin.getInventoryManager()).getSWInventory(event.getInventory());
        SWGuiClickHandler.ClickType clickType;
        switch (event.getClick()) {
            case RIGHT:
            case SHIFT_RIGHT:
                clickType = SWGuiClickHandler.ClickType.SECONDARY;
                break;
            case MIDDLE:
                clickType = SWGuiClickHandler.ClickType.MIDDLE;
                break;
            default:
                clickType = SWGuiClickHandler.ClickType.PRIMARY;
                break;
        }

        CoreSWInventoryClickEvent swEvent = new CoreSWInventoryClickEvent(
                inv,
                clickType,
                event.getSlot(),
                event.getRawSlot(),
                event.isShiftClick(),
                new BukkitItem(plugin, event.getCurrentItem())
        );
        plugin.getEventManager().callEvent(swEvent);

        if (swEvent.isCancelled()) {
            event.setCancelled(true);
        }
        event.setCurrentItem(((BukkitItem) swEvent.getCurrentItem()).getBukkitItem());


        SWGui gui = this.plugin.getGuiManager().getActiveGui(inv);
        if (gui == null) return;

        event.setCancelled(true);

        gui.handleClick(event.getSlot(), clickType, event.isShiftClick());
    }

    // Utils

    private SWPlayer getPlayerFromBukkitPlayer(Player player) {
        return this.plugin.getPlayerManager().getPlayerByUUID(player.getUniqueId());
    }

}
