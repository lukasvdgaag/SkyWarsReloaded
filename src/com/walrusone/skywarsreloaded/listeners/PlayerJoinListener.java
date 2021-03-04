package com.walrusone.skywarsreloaded.listeners;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.enums.GameType;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.managers.MatchManager;
import com.walrusone.skywarsreloaded.managers.PlayerStat;
import com.walrusone.skywarsreloaded.utilities.Util;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;

public class PlayerJoinListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(final PlayerJoinEvent event) {

        final Player player = event.getPlayer();

        if (SkyWarsReloaded.get().getUpdater().getUpdateStatus() == 1 && (player.isOp() || player.hasPermission("sw.admin"))) {
            BaseComponent base = new TextComponent("§d§l[SkyWarsReloaded] §aA new update has been found: §b" + SkyWarsReloaded.get().getUpdater().getLatestVersion() + "§a. Click here to update!");
            base.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, SkyWarsReloaded.get().getUpdater().getUpdateURL()));
            base.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent("§7Click here to update to the latest version!")}));
            SkyWarsReloaded.getNMS().sendJSON(player, "[\"\",{\"text\":\"§d§l[SkyWarsReloaded] §aA new update has been found: §b" + SkyWarsReloaded.get().getUpdater().getLatestVersion() + "§a. Click here to update!\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + SkyWarsReloaded.get().getUpdater().getUpdateURL() + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"§7Click here to update to the latest version!\"}]}}}]");
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if (SkyWarsReloaded.getCfg().getSpawn() != null && SkyWarsReloaded.getCfg().teleportOnJoin()) {
                    player.teleport(SkyWarsReloaded.getCfg().getSpawn());
                }
            }
        }.runTaskLater(SkyWarsReloaded.get(), 1);

        if (SkyWarsReloaded.getCfg().promptForResource()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.setResourcePack(SkyWarsReloaded.getCfg().getResourceLink());
                }
            }.runTaskLater(SkyWarsReloaded.get(), 20);
        }

        if (PlayerStat.getPlayerStats(player) != null) {
            PlayerStat.removePlayer(player.getUniqueId().toString());
        }
        //new BukkitRunnable() {
        // @Override
        //public void run() {
        if (!SkyWarsReloaded.getCfg().bungeeMode()) {
            for (GameMap gMap : GameMap.getMaps()) {
                if (gMap.getCurrentWorld() != null && gMap.getCurrentWorld().equals(player.getWorld())) {
                    if (SkyWarsReloaded.getCfg().getSpawn() != null) {
                        player.teleport(SkyWarsReloaded.getCfg().getSpawn());
                    }
                }
            }
        }
        //    }
        // }.runTaskLater(SkyWarsReloaded.get(), 1);

        PlayerStat pStats = new PlayerStat(event.getPlayer());
        // Load player data
        pStats.loadStats(() -> {
            // After stats are done loading, move to a game if in bungeecord mode
            if (SkyWarsReloaded.getCfg().bungeeMode()) {
                if (player != null) {
                    if (!SkyWarsReloaded.getCfg().isLobbyServer()) {
                        Bukkit.getLogger().log(Level.WARNING, "Trying to let " + player.getName() + " join a game");

                        boolean joined = MatchManager.get().joinGame(player, GameType.ALL);
                        if (!joined) {
                            Bukkit.getLogger().log(Level.WARNING, "Failed to put " + player.getName() + " in a game");
                            if (SkyWarsReloaded.getCfg().debugEnabled()) {
                                Util.get().logToFile(ChatColor.YELLOW + "Couldn't find an arena for player " + player.getName() + ". Sending the player back to the skywars lobby.");
                            }
                            SkyWarsReloaded.get().sendBungeeMsg(player, "Connect", SkyWarsReloaded.getCfg().getBungeeLobby());
                        }
                    }
                }
            }
        });
        PlayerStat.getPlayers().add(pStats);
    }
}