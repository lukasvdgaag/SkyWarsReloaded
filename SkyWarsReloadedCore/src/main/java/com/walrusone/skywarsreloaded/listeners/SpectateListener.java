package com.walrusone.skywarsreloaded.listeners;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.enums.PlayerRemoveReason;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.managers.MatchManager;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class SpectateListener implements org.bukkit.event.Listener {
    private HashMap<String, BukkitTask> teleportRequests = new HashMap();

    public SpectateListener() {
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        Player player = e.getPlayer();
        GameMap gameMap = MatchManager.get().getSpectatorMap(player);
        if ((gameMap == null) || (player.hasPermission("sw.opteleport"))) {
            return;
        }
        if (e.getCause() != TeleportCause.END_PORTAL) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onSpectatorDamaged(EntityDamageEvent e) {
        if ((e.getEntity() instanceof Player)) {
            Player player = (Player) e.getEntity();
            GameMap gameMap = MatchManager.get().getSpectatorMap(player);
            if (gameMap == null) {
                return;
            }
            e.setCancelled(true);
            if (e.getCause() == EntityDamageEvent.DamageCause.VOID) {
                org.bukkit.World world = gameMap.getCurrentWorld();
                Location spectateSpawn = new Location(world, 0.0D, 0.0D, 0.0D);
                player.teleport(spectateSpawn);
            }
        }
    }

    // TODO: REMOVE IF NO PROBLEMS PRESENTED
//    @EventHandler
//    public void onPlayerQuit(PlayerQuitEvent e) {
//        Player player = e.getPlayer();
//        GameMap gameMap = MatchManager.get().getSpectatorMap(player);
//        if (gameMap == null) {
//            return;
//        }
//        gameMap.getSpectators().remove(player.getUniqueId());
//        gameMap.getAlivePlayers().remove(player);
//        gameMap.getAllPlayers().remove(player);
//        MatchManager.get().removeSpectator(player);
//
//    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (player == null)
            return;
        GameMap gameMap = MatchManager.get().getSpectatorMap(player);
        if (gameMap == null) {
            return;
        }
        int slot = e.getSlot();
        if (slot == 8) {
            player.closeInventory();
            SkyWarsReloaded.get().getPlayerManager().removePlayer(
                    player, PlayerRemoveReason.PLAYER_QUIT_GAME, null, false
            );
        } else if ((slot >= 9) && (slot <= 35)) {
            player.closeInventory();
            ItemStack item = e.getCurrentItem();
            if (item != null && !item.getType().equals(Material.AIR)) {
                // Get name to TP to & sanity check
                String name = org.bukkit.ChatColor.stripColor(item.getItemMeta().getDisplayName());
                if (name == null)
                    return;
                Player toSpec = SkyWarsReloaded.get().getServer().getPlayer(name);
                if (toSpec != null && !gameMap.mapContainsDead(toSpec.getUniqueId())) {
                    player.teleport(toSpec.getLocation(), TeleportCause.END_PORTAL);
                } else {
                    SkyWarsReloaded.get().getLogger().warning("Spectator attempted to TP to " + name + " but that player is dead or not online!");
                }
            }
        }
    }


    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerMove(PlayerMoveEvent e) {
        if ((teleportRequests.containsKey(e.getPlayer().getUniqueId().toString())) && (
                (e.getTo().getBlockX() != e.getFrom().getBlockX()) || (e.getTo().getBlockY() != e.getFrom().getBlockY()) || (e.getTo().getBlockZ() != e.getFrom().getBlockZ()))) {
            e.getPlayer().sendMessage(new Messaging.MessageFormatter().format("error.spectate-cancelled"));
            ((BukkitTask) teleportRequests.get(e.getPlayer().getUniqueId().toString())).cancel();
            teleportRequests.remove(e.getPlayer().getUniqueId().toString());
        }
    }
}