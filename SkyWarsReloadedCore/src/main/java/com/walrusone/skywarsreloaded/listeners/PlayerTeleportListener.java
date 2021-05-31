package com.walrusone.skywarsreloaded.listeners;

import com.google.common.collect.Lists;
import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.config.Config;
import com.walrusone.skywarsreloaded.enums.MatchState;
import com.walrusone.skywarsreloaded.enums.PlayerRemoveReason;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.managers.MatchManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PlayerTeleportListener implements org.bukkit.event.Listener {
    public PlayerTeleportListener() {
    }

    private static final List<Player> cooldowns = Lists.newArrayList();
    private static final Object cooldownsLock = new Object();

    @org.bukkit.event.EventHandler(priority = org.bukkit.event.EventPriority.LOWEST)
    public void onPlayerTeleport(PlayerTeleportEvent a1) {
        Player player = a1.getPlayer();
        GameMap gameMap = MatchManager.get().getPlayerMap(player);

        if (gameMap == null) {
            if (SkyWarsReloaded.getCfg().getSpawn() != null) {
                // Pre vars
                World spawnWorld = SkyWarsReloaded.getCfg().getSpawn().getWorld();
                boolean wasInSpawnWorld = a1.getFrom().getWorld().equals(spawnWorld);
                boolean isGoingToSpawnWorld = a1.getTo().getWorld().equals(spawnWorld);

                // Going to spawn world
                if ((!a1.getFrom().getWorld().equals(spawnWorld)) && (a1.getTo().getWorld().equals(spawnWorld))) {
                    setPlayerOnCooldown(player, true);
                    Bukkit.getScheduler().runTaskLaterAsynchronously(SkyWarsReloaded.get(), () -> setPlayerOnCooldown(player, false), 5);
                    com.walrusone.skywarsreloaded.managers.PlayerStat.updatePlayer(player.getUniqueId().toString());
                    if (SkyWarsReloaded.get().getUpdater().getUpdateStatus() == 1 && (player.isOp() || player.hasPermission("sw.admin"))) {
                        //player.spigot().sendMessage(base);
                        SkyWarsReloaded.getNMS().sendJSON(player, "[\"\",{\"text\":\"§d§l[SkyWarsReloaded] §aA new update has been found: §b" + SkyWarsReloaded.get().getUpdater().getLatestVersion() + "§a. Click here to update!\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + SkyWarsReloaded.get().getUpdater().getUpdateURL() + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"§7Click here to update to the latest version!\"}]}}}]");
                    }
                    return;
                }

                // Leaving spawn world
                if (wasInSpawnWorld && !isGoingToSpawnWorld) {
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
        else if (a1.getCause().equals(TeleportCause.END_PORTAL) ||
                player.hasPermission("sw.opteleport") ||
                a1.getTo().getWorld().equals(a1.getFrom().getWorld()))
        {
            a1.setCancelled(false);
        }
        else if (a1.getCause().equals(TeleportCause.ENDER_PEARL) &&
                gameMap.getMatchState() != MatchState.ENDING &&
                gameMap.getMatchState() != MatchState.WAITINGSTART &&
                gameMap.getMatchState() != MatchState.WAITINGLOBBY)
        {
            a1.setCancelled(false);
        }
        else {
            a1.setCancelled(true);
        }
    }

    @org.bukkit.event.EventHandler(priority = org.bukkit.event.EventPriority.HIGHEST)
    public void onTP(PlayerTeleportEvent e) {
        GameMap gameMap = MatchManager.get().getPlayerMap(e.getPlayer());
        if (gameMap != null) {
            String toWorldName = e.getTo().getWorld().getName();
            if (gameMap.getCurrentWorld() == null) {
                SkyWarsReloaded.get().getLogger().severe("Skywars could not find a world by the name of " + gameMap.getName());
                return;
            }
            String mapWorldName = gameMap.getCurrentWorld().getName();
            if (!toWorldName.equals(mapWorldName)) {
                Config config = SkyWarsReloaded.getCfg();
                if (config.getKickOnWorldTeleport()) {
                    Player player = e.getPlayer();
//                    EntityDamageEvent.DamageCause damageCause = EntityDamageEvent.DamageCause.CUSTOM;
//                    if (player.getLastDamageCause() != null) {
//                        damageCause = player.getLastDamageCause().getCause();
//                    }
                    Location spawnLoc = config.getSpawn();
                    if (spawnLoc == null) {
                        SkyWarsReloaded.get().getLogger().severe("Spawn location is not set! Cannot perform auto return to lobby on world change!");
                        return;
                    }
                    String lobbyWorldName = spawnLoc.getWorld().getName();
                    boolean shouldSendToLobby = config.bungeeMode() || !lobbyWorldName.equals(toWorldName);
                    System.out.println("0 " + shouldSendToLobby + " " + lobbyWorldName);
                    SkyWarsReloaded.get().getPlayerManager().removePlayer(
                           player,
                           PlayerRemoveReason.PLAYER_QUIT_GAME,
                           null,
                            shouldSendToLobby,
                           true
                   );
                    // MatchManager.get().removeAlivePlayer(player, damageCause, true, true);
                } else {
                    e.setCancelled(true);
                }
            }
        }
    }

    // UTILS

    public static void setPlayerOnCooldown(Player p, boolean cooldown) {
        synchronized (cooldownsLock) {
            boolean contains = cooldowns.contains(p);
            if (cooldown && !contains) cooldowns.add(p);
            else if (!cooldown && contains) cooldowns.remove(p);
        }
    }

    public static boolean isPlayerOnCooldown(Player p) {
        synchronized (cooldownsLock) {
            return cooldowns.contains(p);
        }
    }
}
