package com.walrusone.skywarsreloaded.listeners;

import com.google.common.collect.Lists;
import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.enums.MatchState;
import com.walrusone.skywarsreloaded.enums.PlayerRemoveReason;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.managers.MatchManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PlayerTeleportListener implements org.bukkit.event.Listener {
    public PlayerTeleportListener() {
    }

    public static List<Player> cooldowns = Lists.newArrayList();

    @org.bukkit.event.EventHandler(priority = org.bukkit.event.EventPriority.LOWEST)
    public void onPlayerTeleport(PlayerTeleportEvent a1) {
        Player player = a1.getPlayer();
        GameMap gameMap = MatchManager.get().getPlayerMap(player);

        if (gameMap == null) {
            if (SkyWarsReloaded.getCfg().getSpawn() != null) {
                if ((!a1.getFrom().getWorld().equals(SkyWarsReloaded.getCfg().getSpawn().getWorld())) && (a1.getTo().getWorld().equals(SkyWarsReloaded.getCfg().getSpawn().getWorld()))) {
                    cooldowns.add(player);
                    Bukkit.getScheduler().runTaskLaterAsynchronously(SkyWarsReloaded.get(), () -> cooldowns.remove(player), 5);
                    com.walrusone.skywarsreloaded.managers.PlayerStat.updatePlayer(a1.getPlayer().getUniqueId().toString());
                    if (SkyWarsReloaded.get().getUpdater().getUpdateStatus() == 1 && (a1.getPlayer().isOp() || a1.getPlayer().hasPermission("sw.admin"))) {
                        //player.spigot().sendMessage(base);
                        SkyWarsReloaded.getNMS().sendJSON(player, "[\"\",{\"text\":\"§d§l[SkyWarsReloaded] §aA new update has been found: §b" + SkyWarsReloaded.get().getUpdater().getLatestVersion() + "§a. Click here to update!\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + SkyWarsReloaded.get().getUpdater().getUpdateURL() + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"§7Click here to update to the latest version!\"}]}}}]");
                    }
                    return;
                }
                if ((a1.getFrom().getWorld().equals(SkyWarsReloaded.getCfg().getSpawn().getWorld())) && (!a1.getTo().getWorld().equals(SkyWarsReloaded.getCfg().getSpawn().getWorld()))) {
                    if (SkyWarsReloaded.getCfg().lobbyBoardEnabled()) {
                        SkyWarsReloaded.getNMS().removeFromScoreboardCollection(player.getScoreboard());//for 1.13+
                        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                    }
                    if ((SkyWarsReloaded.getCfg().optionsMenuEnabled()) &&
                            (player.getInventory().getItem(SkyWarsReloaded.getCfg().getOptionsSlot()) != null) &&
                            (player.getInventory().getItem(SkyWarsReloaded.getCfg().getOptionsSlot()).equals(SkyWarsReloaded.getIM().getItem("optionselect")))) {
                        player.getInventory().setItem(SkyWarsReloaded.getCfg().getOptionsSlot(), new ItemStack(Material.AIR, 1));
                    }


                    if ((SkyWarsReloaded.getCfg().joinMenuEnabled()) && (player.hasPermission("sw.join")) &&
                            (player.getInventory().getItem(SkyWarsReloaded.getCfg().getJoinSlot()) != null) &&
                            (player.getInventory().getItem(SkyWarsReloaded.getCfg().getJoinSlot()).equals(SkyWarsReloaded.getIM().getItem("joinselect")))) {
                        player.getInventory().setItem(SkyWarsReloaded.getCfg().getJoinSlot(), new ItemStack(Material.AIR, 1));
                    }


                    if ((SkyWarsReloaded.getCfg().spectateMenuEnabled()) && (player.hasPermission("sw.spectate")) &&
                            (player.getInventory().getItem(SkyWarsReloaded.getCfg().getSpectateSlot()) != null) &&
                            (player.getInventory().getItem(SkyWarsReloaded.getCfg().getSpectateSlot()).equals(SkyWarsReloaded.getIM().getItem("spectateselect")))) {
                        player.getInventory().setItem(SkyWarsReloaded.getCfg().getSpectateSlot(), new ItemStack(Material.AIR, 1));
                    }

                }

            }
        }
        else if (a1.getCause().equals(TeleportCause.SPECTATE)) {
            a1.setCancelled(true);
        }
        else if ((a1.getCause().equals(TeleportCause.END_PORTAL)) || (player.hasPermission("sw.opteleport")) || (a1.getTo().getWorld().equals(a1.getFrom().getWorld()))) {
            a1.setCancelled(false);
        } else if ((a1.getCause().equals(TeleportCause.ENDER_PEARL)) && (gameMap.getMatchState() != MatchState.ENDING) && (gameMap.getMatchState() != MatchState.WAITINGSTART && gameMap.getMatchState() != MatchState.WAITINGLOBBY)) {
            a1.setCancelled(false);
        } else {
            a1.setCancelled(true);
        }
    }

    @org.bukkit.event.EventHandler(priority = org.bukkit.event.EventPriority.HIGHEST)
    public void onTP(PlayerTeleportEvent e) {
        GameMap g = MatchManager.get().getPlayerMap(e.getPlayer());
        if (g != null) {
            if (!e.getTo().getWorld().getName().equals(g.getCurrentWorld().getName())) {
                if (SkyWarsReloaded.getCfg().getKickOnWorldTeleport()) {
                    Player player = e.getPlayer();
                    EntityDamageEvent.DamageCause damageCause = EntityDamageEvent.DamageCause.CUSTOM;
                    if (player.getLastDamageCause() != null) {
                        damageCause = player.getLastDamageCause().getCause();
                    }
                    SkyWarsReloaded.get().getPlayerManager().removePlayer(player, PlayerRemoveReason.PLAYER_QUIT_GAME, null, true);
                    // MatchManager.get().removeAlivePlayer(player, damageCause, true, true);
                } else {
                    e.setCancelled(true);
                }
            }
        }
    }
}
