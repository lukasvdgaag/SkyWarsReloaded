package com.walrusone.skywarsreloaded.listeners;

import com.google.common.collect.Maps;
import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.enums.MatchState;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.game.PlayerCard;
import com.walrusone.skywarsreloaded.game.TeamCard;
import com.walrusone.skywarsreloaded.managers.MatchManager;
import com.walrusone.skywarsreloaded.managers.PlayerStat;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.Util;
import com.walrusone.skywarsreloaded.utilities.VaultUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class ChatListener implements Listener {

    private static Map<UUID, Long> chatList = Maps.newHashMap();
    private static Map<UUID, String> toChange = Maps.newHashMap();

    public static void setTime(UUID uuid, long time) {
        chatList.put(uuid, time);
    }

    public static void setSetting(UUID uuid, String setting) {
        toChange.put(uuid, setting);
    }

    @EventHandler
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID uuid = event.getPlayer().getUniqueId();
        if (chatList.containsKey(uuid)) {
            if (Math.abs((System.currentTimeMillis() - chatList.get(uuid))) < 20000) {
                ChatListener.chatList.remove(uuid);
                event.setCancelled(true);
                String[] settings = toChange.get(uuid).split(":");
                GameMap gMap = GameMap.getMap(settings[0]);
                String setting = settings[1];
                String variable = event.getMessage();
                if (gMap != null && setting.equals("display")) {
                    gMap.setDisplayName(variable);
                    player.sendMessage(new Messaging.MessageFormatter().setVariable("mapname", gMap.getName()).setVariable("displayname", variable).format("maps.name"));
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            gMap.update();
                        }
                    }.runTask(SkyWarsReloaded.get());
                    SkyWarsReloaded.getIC().show(player, gMap.getArenaKey());
                } else if (gMap != null && setting.equalsIgnoreCase("creator")) {
                    gMap.setCreator(variable);
                    player.sendMessage(new Messaging.MessageFormatter().setVariable("mapname", gMap.getName()).setVariable("creator", variable).format("maps.creator"));
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            gMap.update();
                        }
                    }.runTask(SkyWarsReloaded.get());
                    SkyWarsReloaded.getIC().show(player, gMap.getArenaKey());
                }
                ChatListener.toChange.remove(uuid);
            } else {
                ChatListener.chatList.remove(uuid);
                ChatListener.toChange.remove(uuid);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        if (SkyWarsReloaded.getCfg().formatChat()) {
            GameMap gMap = MatchManager.get().getPlayerMap(event.getPlayer());
            GameMap sMap = MatchManager.get().getSpectatorMap(event.getPlayer());
            if (gMap != null || sMap != null) {
                if (SkyWarsReloaded.getCfg().useExternalChat()) {
                    PlayerStat ps = PlayerStat.getPlayerStats(event.getPlayer());
                    String prefix = "";
                    if (SkyWarsReloaded.getCfg().addPrefix() && ps != null) {
                        prefix = new Messaging.MessageFormatter()
                                .setVariable("wins", Integer.toString(ps.getWins()))
                                .setVariable("losses", Integer.toString(ps.getLosses()))
                                .setVariable("kills", Integer.toString(ps.getKills()))
                                .setVariable("deaths", Integer.toString(ps.getDeaths()))
                                .setVariable("xp", Integer.toString(ps.getXp()))
                                .format("chat.externalPrefix");
                    }
                    String format = event.getFormat();
                    ArrayList<Player> recievers = null;
                    if (sMap != null && SkyWarsReloaded.getCfg().limitSpecChat()) {
                        recievers = sMap.getMessageAblePlayers(true);
                    } else if (sMap != null && SkyWarsReloaded.getCfg().limitGameChat() || gMap != null && SkyWarsReloaded.getCfg().limitGameChat()) {
                        if (sMap != null) {
                            recievers = sMap.getMessageAblePlayers(false);
                        } else {
                            recievers = gMap.getMessageAblePlayers(false);
                        }
                    }
                    if (recievers != null) {
                        //
                        event.getRecipients().clear();
                        event.getRecipients().addAll(recievers);
                    }

                    event.setFormat(prefix + format);
                } else {
                    String prefix = "";
                    if (SkyWarsReloaded.getCfg().economyEnabled()) {
                        if (VaultUtils.get().getChat() != null) {
                            if (VaultUtils.get().getChat().getPlayerPrefix(event.getPlayer()) != null) {
                                prefix = VaultUtils.get().getChat().getPlayerPrefix(event.getPlayer());
                            }
                        }
                    }

                    String colorMessage = ChatColor.translateAlternateColorCodes('&', event.getMessage());
                    String message;
                    if (event.getPlayer().hasPermission("sw.colorchat")) {
                        message = colorMessage;
                    } else {
                        message = ChatColor.stripColor(colorMessage);
                        while (message.contains("&")) {
                            message = ChatColor.translateAlternateColorCodes('&', message);
                            message = ChatColor.stripColor(message);
                        }
                    }
                    PlayerStat ps = PlayerStat.getPlayerStats(event.getPlayer());
                    String messageToSend;
                    if (ps != null) {
                        ArrayList<Player> recievers = null;
                        boolean sendTeamChat = false;
                        if (sMap != null && SkyWarsReloaded.getCfg().limitSpecChat()) {
                            recievers = sMap.getMessageAblePlayers(true);
                        } else if (gMap != null && SkyWarsReloaded.getCfg().isUseTeamChat() && gMap.getTeamSize() > 1 && gMap.getMatchState() != MatchState.WAITINGLOBBY) {
                            TeamCard tcard = gMap.getTeamCard(event.getPlayer());
                            if (message.startsWith("!") || tcard == null) {
                                // send message to global chat
                                recievers = gMap.getMessageAblePlayers(false);
                                message = message.replaceFirst("!", "");
                            } else {
                                sendTeamChat = true;
                                recievers = new ArrayList<>();
                                for (PlayerCard pc : tcard.getPlayerCards()) {
                                    if (pc != null && pc.getPlayer() != null) {
                                        recievers.add(pc.getPlayer());
                                    }
                                }
                            }
                        } else if (sMap != null && SkyWarsReloaded.getCfg().limitGameChat() || gMap != null && SkyWarsReloaded.getCfg().limitGameChat()) {
                            if (sMap != null) {
                                recievers = sMap.getMessageAblePlayers(false);
                            } else {
                                recievers = gMap.getMessageAblePlayers(false);
                            }
                        }
                        if (recievers != null) {
                            event.getRecipients().clear();
                            event.getRecipients().addAll(recievers);

                            //event.getRecipients().removeIf((Player player) -> !recipents.contains(player));
                        }

                        if (sMap != null) {
                            messageToSend = new Messaging.MessageFormatter()
                                    .setVariable("name", event.getPlayer().getName())
                                    .setVariable("displayname", event.getPlayer().getDisplayName())
                                    .setVariable("wins", Integer.toString(ps.getWins()))
                                    .setVariable("losses", Integer.toString(ps.getLosses()))
                                    .setVariable("kills", Integer.toString(ps.getKills()))
                                    .setVariable("deaths", Integer.toString(ps.getDeaths()))
                                    .setVariable("xp", Integer.toString(ps.getXp()))
                                    .setVariable("level", Util.get().getPlayerLevel(event.getPlayer(), false) + "")
                                    .setVariable("prefix", prefix)
                                    .setVariable("message", message)
                                    .setVariable("mapname", sMap.getDisplayName())
                                    .format("chat.specchat");
                        } else {
                            String format = sendTeamChat ? "chat.teamchat" : "chat.ingamechat";

                            messageToSend = new Messaging.MessageFormatter()
                                    .setVariable("player", event.getPlayer().getName())
                                    .setVariable("displayname", event.getPlayer().getDisplayName())
                                    .setVariable("wins", Integer.toString(ps.getWins()))
                                    .setVariable("losses", Integer.toString(ps.getLosses()))
                                    .setVariable("kills", Integer.toString(ps.getKills()))
                                    .setVariable("deaths", Integer.toString(ps.getDeaths()))
                                    .setVariable("xp", Integer.toString(ps.getXp()))
                                    .setVariable("level", Util.get().getPlayerLevel(event.getPlayer(), false) + "")
                                    .setVariable("prefix", prefix)
                                    .setVariable("message", message)
                                    .setVariable("mapname", gMap.getDisplayName())
                                    .format(format);
                        }

                        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                            messageToSend = (me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(Bukkit.getOfflinePlayer(event.getPlayer().getUniqueId()), messageToSend));
                        }
                        messageToSend = messageToSend.replace("%", "%%");
                        event.setFormat(messageToSend);
                    }
                }
            } else {
                if (SkyWarsReloaded.getCfg().getSpawn() != null && event.getPlayer().getWorld() != null && event.getPlayer().getWorld().equals(SkyWarsReloaded.getCfg().getSpawn().getWorld())) {
                    if (SkyWarsReloaded.getCfg().useExternalChat()) {
                        PlayerStat ps = PlayerStat.getPlayerStats(event.getPlayer());
                        String prefix = "";
                        if (SkyWarsReloaded.getCfg().addPrefix() && ps != null) {
                            prefix = new Messaging.MessageFormatter()
                                    .setVariable("wins", Integer.toString(ps.getWins()))
                                    .setVariable("losses", Integer.toString(ps.getLosses()))
                                    .setVariable("kills", Integer.toString(ps.getKills()))
                                    .setVariable("deaths", Integer.toString(ps.getDeaths()))
                                    .setVariable("xp", Integer.toString(ps.getXp()))
                                    .setVariable("level", Util.get().getPlayerLevel(event.getPlayer(), false) + "")
                                    .format("chat.externalPrefix");
                        }
                        String format = event.getFormat();
                        if (SkyWarsReloaded.getCfg().limitLobbyChat()) {
                            World world = event.getPlayer().getWorld();
                            //event.getRecipients().removeIf((Player player) -> !player.getWorld().equals(world));
                            event.getRecipients().clear();
                            event.getRecipients().addAll(world.getPlayers());
                        }

                        event.setFormat(prefix + format.replace("%", "%%"));
                    } else {
                        String prefix = "";
                        if (SkyWarsReloaded.getCfg().economyEnabled()) {
                            if (VaultUtils.get().getChat() != null) {
                                if (VaultUtils.get().getChat().getPlayerPrefix(event.getPlayer()) != null) {
                                    prefix = VaultUtils.get().getChat().getPlayerPrefix(event.getPlayer());
                                }
                            }
                        }

                        String colorMessage = ChatColor.translateAlternateColorCodes('&', event.getMessage());
                        String message;
                        if (event.getPlayer().hasPermission("sw.colorchat")) {
                            message = colorMessage;
                        } else {
                            message = ChatColor.stripColor(colorMessage);
                            if (message.contains("&")) {
                                message = ChatColor.translateAlternateColorCodes('&', message);
                                message = ChatColor.stripColor(message);
                            }
                        }
                        PlayerStat ps = PlayerStat.getPlayerStats(event.getPlayer());
                        if (ps != null) {
                            if (SkyWarsReloaded.getCfg().limitLobbyChat()) {
                                World world = event.getPlayer().getWorld();
                                event.getRecipients().clear();
                                event.getRecipients().addAll(world.getPlayers());
                            }
                            String format = (new Messaging.MessageFormatter()
                                    .setVariable("player", event.getPlayer().getName())
                                    .setVariable("displayname", event.getPlayer().getDisplayName())
                                    .setVariable("wins", Integer.toString(ps.getWins()))
                                    .setVariable("losses", Integer.toString(ps.getLosses()))
                                    .setVariable("kills", Integer.toString(ps.getKills()))
                                    .setVariable("deaths", Integer.toString(ps.getDeaths()))
                                    .setVariable("xp", Integer.toString(ps.getXp()))
                                    .setVariable("level", Util.get().getPlayerLevel(event.getPlayer(), false) + "")
                                    .setVariable("prefix", prefix)
                                    .setVariable("message", message)
                                    .format("chat.lobbychat"));

                            if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                                format = (me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(Bukkit.getOfflinePlayer(event.getPlayer().getUniqueId()), format));
                            }
                            format = format.replace("%", "%%");
                            event.setFormat(format);
                        }
                    }
                }
            }
        }
    }

}