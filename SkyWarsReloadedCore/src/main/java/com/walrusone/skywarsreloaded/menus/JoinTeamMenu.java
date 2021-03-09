package com.walrusone.skywarsreloaded.menus;

import com.google.common.collect.Lists;
import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.enums.GameType;
import com.walrusone.skywarsreloaded.enums.MatchState;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.managers.MatchManager;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.Party;
import com.walrusone.skywarsreloaded.utilities.SWRServer;
import com.walrusone.skywarsreloaded.utilities.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JoinTeamMenu {

    private static final String menuName = new Messaging.MessageFormatter().format("menu.jointeamgame-menu-title");
    private static int menuSize = 45;
    public static Map<Integer, String> arenaSlots = new HashMap<>();


    public JoinTeamMenu() {
        Inventory menu = Bukkit.createInventory(null, menuSize + 9, menuName);
        ArrayList<Inventory> invs = new ArrayList<>();
        invs.add(menu);

        Runnable update = () -> {
            if ((SkyWarsReloaded.getIC().hasViewers("jointeammenu") || SkyWarsReloaded.getIC().hasViewers("spectateteam"))) {
                ArrayList<GameMap> normalGames = GameMap.getPlayableArenas(GameType.TEAM);
                ArrayList<SWRServer> bungeeGames = Lists.newArrayList();
                
                for (SWRServer s : SWRServer.getServersCopy()) {
                    if (s.getTeamSize() > 1) {
                        bungeeGames.add(s);
                    }
                }
                
                ArrayList<Inventory> invs1 = SkyWarsReloaded.getIC().getMenu("jointeammenu").getInventories();

                for (Inventory inv : invs1) {
                    for (int i = 0; i < menuSize; i++) {
                        inv.setItem(i, new ItemStack(Material.AIR, 1));
                    }
                }

                int gameSize = SkyWarsReloaded.getCfg().bungeeMode() && SkyWarsReloaded.getCfg().isLobbyServer() ? bungeeGames.size() : normalGames.size();
                
                for (int iii = 0; iii < gameSize; iii++) {
                    int invent = Math.floorDiv(iii, menuSize);
                    if (invs1.isEmpty() || invs1.size() < invent + 1) {
                        invs1.add(Bukkit.createInventory(null, menuSize + 9, menuName));
                    }

                    MatchState state;
                    int alivePlayers = 0;
                    int maxPlayers = 0;
                    String displayName = "";
                    int teamsize = 1;
                    String name = "";
                    
                    GameMap gMap = null;
                    SWRServer server = null;
                    if (!SkyWarsReloaded.getCfg().bungeeMode() || !SkyWarsReloaded.getCfg().isLobbyServer()) {
                        gMap = normalGames.get(iii);
                        state = gMap.getMatchState();

                        if (state == MatchState.WAITINGLOBBY) {
                            alivePlayers = gMap.getWaitingPlayers().size();
                        }
                        else {
                            alivePlayers = gMap.getAlivePlayers().size();
                        }

                        maxPlayers = gMap.getMaxPlayers();
                        displayName = gMap.getDisplayName();
                        teamsize = gMap.getTeamSize();
                        name = gMap.getName();
                    }
                    else {
                        server = bungeeGames.get(iii);
                        state = server.getMatchState();
                        alivePlayers = server.getPlayerCount();
                        maxPlayers = server.getMaxPlayers();
                        displayName = server.getDisplayName();
                        teamsize = server.getTeamSize();
                        name = server.getServerName();
                    }

                    

                    List<String> loreList = Lists.newLinkedList();
                    if (state != MatchState.OFFLINE) {
                        if (state == MatchState.WAITINGSTART || state == MatchState.WAITINGLOBBY) {
                            for (String a : SkyWarsReloaded.getMessaging().getFile().getStringList("menu.join_menu.lore.waiting-start")) {
                                loreList.add(ChatColor.translateAlternateColorCodes('&',
                                        a.replace("{playercount}", "" + alivePlayers)
                                                .replace("{maxplayers}", "" + maxPlayers)
                                                .replace("{arena}", displayName)
                                                .replace("{teamsize}", teamsize + "")
                                                .replace("{aliveplayers}", alivePlayers + "")
                                                .replace("{name}", name)
                                ));
                            }
                        } else if (state.equals(MatchState.PLAYING)) {
                            for (String a : SkyWarsReloaded.getMessaging().getFile().getStringList("menu.join_menu.lore.playing")) {
                                loreList.add(ChatColor.translateAlternateColorCodes('&',
                                        a.replace("{playercount}", "" + alivePlayers)
                                                .replace("{maxplayers}", "" + maxPlayers)
                                                .replace("{arena}", displayName)
                                                .replace("{teamsize}", teamsize + "")
                                                .replace("{aliveplayers}", alivePlayers + "")
                                                .replace("{name}", name)
                                ));
                            }
                        } else if (state.equals(MatchState.ENDING)) {
                            for (String a : SkyWarsReloaded.getMessaging().getFile().getStringList("menu.join_menu.lore.ending")) {
                                loreList.add(ChatColor.translateAlternateColorCodes('&',
                                        a.replace("{playercount}", "" + alivePlayers)
                                                .replace("{maxplayers}", "" + maxPlayers)
                                                .replace("{arena}", displayName)
                                                .replace("{teamsize}", teamsize + "")
                                                .replace("{aliveplayers}", alivePlayers + "")
                                                .replace("{name}", name)
                                ));
                            }
                        }

                        double xy = ((double) (alivePlayers / maxPlayers));

                        ItemStack gameIcon = SkyWarsReloaded.getNMS().getItemStack(SkyWarsReloaded.getIM().getItem("blockwaiting"), loreList, ChatColor.translateAlternateColorCodes('&', displayName));

                        ItemStack customIcon = null;
                        if (gMap != null && gMap.getCustomJoinMenuItemEnabled()) {
                            customIcon = gMap.getCustomJoinMenuItem();
                        } else {
                            if (state.equals(MatchState.PLAYING)) {
                                customIcon = SkyWarsReloaded.getIM().getItem("blockplaying");
                            } else if (state.equals(MatchState.ENDING)) {
                                customIcon = SkyWarsReloaded.getIM().getItem("blockending");
                            } else if (state.equals(MatchState.WAITINGSTART) || state.equals(MatchState.WAITINGLOBBY)) {
                                customIcon = SkyWarsReloaded.getIM().getItem("almostfull");
                                if (xy < 0.25) {
                                    customIcon = SkyWarsReloaded.getIM().getItem("almostempty");
                                } else if (xy < 0.5) {
                                    customIcon = SkyWarsReloaded.getIM().getItem("halffull");
                                } else if (xy < 0.75) {
                                    customIcon = SkyWarsReloaded.getIM().getItem("threefull");
                                }
                            }
                        }

                        if (state.equals(MatchState.PLAYING)) {
                            gameIcon = SkyWarsReloaded.getNMS().getItemStack(customIcon, loreList, ChatColor.translateAlternateColorCodes('&',
                                    new Messaging.MessageFormatter()
                                    .setVariable("playercount", "" + alivePlayers)
                                            .setVariable("maxplayers", "" + maxPlayers)
                                            .setVariable("arena", displayName)
                                            .setVariable("teamsize", teamsize + "")
                                            .setVariable("aliveplayers", alivePlayers + "")
                                            .setVariable("name", name)
                                            .format("menu.join_menu.item_title.playing"))
                            );
                        } else if (state.equals(MatchState.ENDING)) {
                            gameIcon = SkyWarsReloaded.getNMS().getItemStack(customIcon, loreList, ChatColor.translateAlternateColorCodes('&',
                                    new Messaging.MessageFormatter()
                                            .setVariable("playercount", "" + alivePlayers)
                                            .setVariable("maxplayers", "" + maxPlayers)
                                            .setVariable("arena", displayName)
                                            .setVariable("teamsize", teamsize + "")
                                            .setVariable("aliveplayers", alivePlayers + "")
                                            .setVariable("name", name)
                                            .format("menu.join_menu.item_title.ending"))

                            );
                        } else if (state == MatchState.WAITINGSTART || state == MatchState.WAITINGLOBBY) {
                            gameIcon = SkyWarsReloaded.getNMS().getItemStack(customIcon, loreList, ChatColor.translateAlternateColorCodes('&',
                                    new Messaging.MessageFormatter()
                                            .setVariable("playercount", "" + alivePlayers)
                                            .setVariable("maxplayers", "" + maxPlayers)
                                            .setVariable("arena", displayName)
                                            .setVariable("teamsize", teamsize + "")
                                            .setVariable("aliveplayers", alivePlayers + "")
                                            .setVariable("name", name)
                                            .format("menu.join_menu.item_title.waiting-start"))

                            );
                            if (xy < 0.75) {
                                gameIcon = SkyWarsReloaded.getNMS().getItemStack(customIcon, loreList, ChatColor.translateAlternateColorCodes('&',
                                        new Messaging.MessageFormatter()
                                                .setVariable("playercount", "" + alivePlayers)
                                                .setVariable("maxplayers", "" + maxPlayers)
                                                .setVariable("arena", displayName)
                                                .setVariable("teamsize", teamsize + "")
                                                .setVariable("aliveplayers", alivePlayers + "")
                                                .setVariable("name", name)
                                                .format("menu.join_menu.item_title.waiting-start"))

                                );
                            }
                            if (xy < 0.50) {
                                gameIcon = SkyWarsReloaded.getNMS().getItemStack(customIcon, loreList, ChatColor.translateAlternateColorCodes('&',
                                        new Messaging.MessageFormatter()
                                                .setVariable("playercount", "" + alivePlayers)
                                                .setVariable("maxplayers", "" + maxPlayers)
                                                .setVariable("arena", displayName)
                                                .setVariable("teamsize", teamsize + "")
                                                .setVariable("aliveplayers", alivePlayers + "")
                                                .setVariable("name", name)
                                                .format("menu.join_menu.item_title.waiting-start"))

                                );
                            }
                            if (xy < 0.25) {
                                gameIcon = SkyWarsReloaded.getNMS().getItemStack(customIcon, loreList, ChatColor.translateAlternateColorCodes('&',
                                        new Messaging.MessageFormatter()
                                                .setVariable("playercount", "" + alivePlayers)
                                                .setVariable("maxplayers", "" + maxPlayers)
                                                .setVariable("arena", displayName)
                                                .setVariable("teamsize", teamsize + "")
                                                .setVariable("aliveplayers", alivePlayers + "")
                                                .setVariable("name", name)
                                                .format("menu.join_menu.item_title.waiting-start"))

                                );
                            }
                        }
                        invs1.get(invent).setItem(iii % menuSize, gameIcon);
                        arenaSlots.put(iii % menuSize, name);
                    }
                }
                if (SkyWarsReloaded.getCfg().spectateMenuEnabled() && !SkyWarsReloaded.getCfg().bungeeMode() || !SkyWarsReloaded.getCfg().isLobbyServer()) {
                    ArrayList<Inventory> specs = SkyWarsReloaded.getIC().getMenu("spectateteammenu").getInventories();
                    int i = 0;
                    for (Inventory inv : invs1) {
                        if (specs.get(i) == null) {
                            specs.add(Bukkit.createInventory(null, menuSize, new Messaging.MessageFormatter().format("menu.spectateteammenu-menu-title")));
                        }
                        specs.get(0).setContents(inv.getContents());
                        i++;
                    }
                }
            }
        };

        SkyWarsReloaded.getIC().create("jointeammenu", invs, event -> {
            Player player = event.getPlayer();
            GameMap gMap = MatchManager.get().getPlayerMap(player);
            if (gMap != null) {
                return;
            }

            String name = event.getName();
            if (name.equalsIgnoreCase(SkyWarsReloaded.getNMS().getItemName(SkyWarsReloaded.getIM().getItem("exitMenuItem")))) {
                player.closeInventory();
                return;
            }

            if (!arenaSlots.containsKey(event.getSlot())) {
                return;
            }

            gMap = null;
            SWRServer server = null;
            MatchState state;

            if (SkyWarsReloaded.getCfg().bungeeMode() && SkyWarsReloaded.getCfg().isLobbyServer()) {
                server = SWRServer.getServer(arenaSlots.get(event.getSlot()));
                if (server == null) {
                    return;
                }
                state = server.getMatchState();
            }
            else {
                gMap= GameMap.getMap(arenaSlots.get(event.getSlot()));
                if (gMap == null) {
                    return;
                }
                state = gMap.getMatchState();
            }



            if (state != MatchState.WAITINGSTART && state != MatchState.WAITINGLOBBY) {
                Util.get().playSound(player, player.getLocation(), SkyWarsReloaded.getCfg().getErrorSound(), 1, 1);
                return;
            }

            if (player.hasPermission("sw.join")) {
                boolean joined;
                Party party = Party.getParty(player);
                if (party != null) {
                    if (party.getLeader().equals(player.getUniqueId())) {
                        if (gMap != null && gMap.canAddParty(party)) {
                            player.closeInventory();
                            joined = gMap.addPlayers(null, party);
                            if (!joined) {
                                player.sendMessage(new Messaging.MessageFormatter().format("error.could-not-join2"));
                            }
                        }
                        else if (server != null && server.canAddParty(party)) {
                            player.closeInventory();
                            server.setPlayerCount(server.getPlayerCount() + party.getSize()-1);
                            server.updateSigns();
                            for (int i =0; i<party.getSize();i++) {
                                SkyWarsReloaded.get().sendBungeeMsg(Bukkit.getPlayer(party.getMembers().get(i)), "Connect", server.getServerName());
                            }
                        }
                    } else {
                        player.closeInventory();
                        player.sendMessage(new Messaging.MessageFormatter().format("party.onlyleader"));
                    }
                } else {
                    if (gMap != null && gMap.canAddPlayer()) {
                        player.closeInventory();
                        joined = gMap.addPlayers(null, player);
                        if (!joined) {
                            player.sendMessage(new Messaging.MessageFormatter().format("error.could-not-join2"));
                        }
                    }
                    else if (server != null && server.canAddPlayer()) {
                        player.closeInventory();
                        server.setPlayerCount(server.getPlayerCount() + 1);
                        server.updateSigns();
                        SkyWarsReloaded.get().sendBungeeMsg(player, "Connect", server.getServerName());
                    }
                }
            }
        });
        SkyWarsReloaded.getIC().getMenu("jointeammenu").setUpdate(update);
    }
}