package com.walrusone.skywarsreloaded.listeners;

import com.google.common.collect.Maps;
import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.config.Config;
import com.walrusone.skywarsreloaded.enums.MatchState;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.game.PlayerCard;
import com.walrusone.skywarsreloaded.game.TeamCard;
import com.walrusone.skywarsreloaded.managers.MatchManager;
import com.walrusone.skywarsreloaded.managers.PlayerStat;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.Util;
import com.walrusone.skywarsreloaded.utilities.VaultUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        Config cfg = SkyWarsReloaded.getCfg();
        Player player = event.getPlayer();

        // Check chat formatting enabled
        if (!cfg.formatChat()) return;

        ChatIntent chatIntent = new ChatIntent();

        GameMap playerMap = MatchManager.get().getPlayerMap(player);
        GameMap specMap = MatchManager.get().getSpectatorMap(player);

        // Calc intents that will affect formatting and scope
        calcPreIntents(event, player, playerMap, specMap, cfg, chatIntent);

        // Format the chat
        formatChat(event, cfg, player, playerMap, specMap, chatIntent);
        // Control who sees the message
        applyRecipients(event, player, playerMap, specMap, cfg, chatIntent);
    }

    private void calcPreIntents(AsyncPlayerChatEvent event, Player player, GameMap playingMap, GameMap specMap, Config cfg, ChatIntent chatIntent) {
        GameMap currentMap = playingMap;
        if (currentMap == null) currentMap = specMap;

        // Strip global prefix
        String message = event.getMessage();
        if (message.charAt(0) == '!') {
            chatIntent.wantsGameChat = true;
            message = message.substring(1);
            event.setMessage(message);
        }
        if (currentMap == null || currentMap.getTeamSize() < 2) {
            chatIntent.forceGameChat = true;
        }

        // Check if player is in the lobby
        chatIntent.isLobbyChat = cfg.getSpawn() != null && player.getWorld() != null && player.getWorld().equals(cfg.getSpawn().getWorld());
    }

    private void applyRecipients(AsyncPlayerChatEvent event, Player player, GameMap playingMap, GameMap specMap, Config cfg, ChatIntent chatIntent) {
        // Force chat to only send to lobby players
        if (chatIntent.isLobbyChat && cfg.limitLobbyChat()) {
            World world = player.getWorld();
            event.getRecipients().clear();
            event.getRecipients().addAll(world.getPlayers());
            return;
        }

        // Handle scope of chat for alive players
        if (playingMap != null)
            applyRecipientsPlaying(event, playingMap, cfg, chatIntent);
        // Handle scope of chat for spectators
        else if (specMap != null)
            applyRecipientsSpec(event, player, specMap, cfg);
    }

    private void applyRecipientsPlaying(AsyncPlayerChatEvent event, @NotNull GameMap playerMap, Config cfg, ChatIntent chatIntent) {
        ArrayList<Player> receivers = null;

        // Handle team chat system
        if (cfg.isUseTeamChat() && playerMap.getTeamSize() > 1 && playerMap.getMatchState() != MatchState.WAITINGLOBBY && !chatIntent.wantsGameChat) {
            TeamCard tCard = playerMap.getTeamCard(event.getPlayer());

            receivers = new ArrayList<>();
            for (PlayerCard card : tCard.getPlayerCards()) {
                if (card == null) continue;

                Player cardPlayer = card.getPlayer();
                if (cardPlayer == null) continue;

                receivers.add(cardPlayer);
            }
        }

        // Handle all other situations
        else if (cfg.limitGameChat()) {
            receivers = playerMap.getMessageAblePlayers(false);
        }

        // Apply to event
        if (receivers != null) {
            event.getRecipients().clear();
            event.getRecipients().addAll(receivers);
        }
    }

    private void applyRecipientsSpec(AsyncPlayerChatEvent event, Player player, GameMap specMap, Config cfg) {
        ArrayList<Player> receivers = null;

        if (cfg.limitSpecChat()) {
            receivers = specMap.getMessageAblePlayers(true);
        } else if (cfg.limitGameChat()) {
            receivers = specMap.getMessageAblePlayers(false);
        }

        if (receivers != null) {
            event.getRecipients().clear();
            event.getRecipients().addAll(receivers);
        }
    }

    private ChatIntent formatChat(AsyncPlayerChatEvent event, Config cfg, Player player, GameMap playingMap, GameMap specMap, ChatIntent chatIntent) {
        GameMap currentMap = playingMap;
        if (currentMap == null) currentMap = specMap;

        if (cfg.useExternalChat()) {
            formatChatExternal(event, cfg, currentMap, chatIntent);
        } else {
            String chatType = "";

            // Lobby Chat
            if (chatIntent.isLobbyChat) {
                chatType = "lobbychat";
            }
            // Spec chat
            else if (specMap != null) {
                chatType = "specchat";
            }
            // In-game chat
            else if (playingMap != null) {
                if (chatIntent.forceGameChat || chatIntent.wantsGameChat) chatType = "ingamechat";
                else chatType = "teamchat";
            }

            formatChatCustom(event, player, cfg, currentMap, chatType, chatIntent);
        }

        return chatIntent;
    }

    private void formatChatCustom(AsyncPlayerChatEvent event, Player player, Config cfg, @Nullable GameMap currentMap, String chatType, ChatIntent chatIntent) {
        String vaultPrefix = null;
        if (cfg.economyEnabled()) vaultPrefix = this.getVaultChatPrefix(player);

        // Apply color formatting
        String message = event.getMessage();

        // Strip if no permission
        if (!player.hasPermission("sw.colorchat") && message.contains("&")) {
            message = message.replaceAll("&[0-9a-fA-F]", "");
        }

        // Apply custom formatting
        String customFormat = formatConfigString(player, currentMap, chatType, vaultPrefix, message);
        if (customFormat == null) customFormat = "";

        // Apply placeholders
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            customFormat = PlaceholderAPI.setPlaceholders(player, customFormat);
        }

        // Apply color
        customFormat = ChatColor.translateAlternateColorCodes('&', customFormat);

        // Escape chars
        customFormat = customFormat.replace("%", "%%");

        // Apply format to event
        event.setFormat(customFormat);
    }

    private String formatConfigString(Player player, @Nullable GameMap currentMap, String chatType, String vaultPrefix, String message) {
        PlayerStat ps = PlayerStat.getPlayerStats(player);
        if (ps != null) {
            return new Messaging.MessageFormatter()
                    .setVariable("player", player.getName())
                    .setVariable("displayname", player.getDisplayName())
                    .setVariable("wins", Integer.toString(ps.getWins()))
                    .setVariable("losses", Integer.toString(ps.getLosses()))
                    .setVariable("kills", Integer.toString(ps.getKills()))
                    .setVariable("deaths", Integer.toString(ps.getDeaths()))
                    .setVariable("xp", Integer.toString(ps.getXp()))
                    .setVariable("level", String.valueOf(Util.get().getPlayerLevel(player, false)))
                    .setVariable("prefix", vaultPrefix == null ? "" : vaultPrefix)
                    .setVariable("mapname", currentMap == null ? "" : currentMap.getDisplayName())
                    .setVariable("message", message)
                    .format("chat." + chatType);
        }

        return null;
    }

    private void formatChatExternal(AsyncPlayerChatEvent event, Config cfg, GameMap currentMap, ChatIntent chatIntent) {
        String prefix = "";

        Player player = event.getPlayer();
        if (cfg.addPrefix()) {
            String vaultChatPrefix = this.getVaultChatPrefix(player);
            prefix = this.formatConfigString(player, currentMap, "externalPrefix", vaultChatPrefix, "");
        }

        // Apply placeholders
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            prefix = PlaceholderAPI.setPlaceholders(player, prefix);
        }

        // Apply color
        prefix = ChatColor.translateAlternateColorCodes('&', prefix);

        // Escape chars
        prefix = prefix.replace("%", "%%");

        event.setFormat(prefix + event.getFormat());
    }

    private String getVaultChatPrefix(Player player) {
        Chat vaultChat = VaultUtils.get().getChat();
        if (vaultChat != null) {
            String vaultChatPrefix = vaultChat.getPlayerPrefix(player);
            if (vaultChatPrefix != null) {
                return vaultChatPrefix;
            }
        }
        return null;
    }

    private class ChatIntent {
        public boolean wantsGameChat = false;
        public boolean forceGameChat = false;
        public boolean isLobbyChat = false;
    }
}