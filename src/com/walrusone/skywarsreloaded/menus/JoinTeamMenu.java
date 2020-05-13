package com.walrusone.skywarsreloaded.menus;

import com.google.common.collect.Lists;
import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.enums.GameType;
import com.walrusone.skywarsreloaded.enums.MatchState;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.managers.MatchManager;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.Party;
import com.walrusone.skywarsreloaded.utilities.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

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
            if ((SkyWarsReloaded.getIC().hasViewers("jointeammenu") || SkyWarsReloaded.getIC().hasViewers("spectateteammenu"))) {
                arenaSlots.clear();
                ArrayList<GameMap> games = GameMap.getPlayableArenas(GameType.TEAM);
                ArrayList<Inventory> invs1 = SkyWarsReloaded.getIC().getMenu("jointeammenu").getInventories();

                for (Inventory inv : invs1) {
                    for (int i = 0; i < menuSize; i++) {
                        inv.setItem(i, new ItemStack(Material.AIR, 1));
                    }
                }

                for (int iii = 0; iii < games.size(); iii++) {
                    int invent = Math.floorDiv(iii, menuSize);
                    if (invs1.isEmpty() || invs1.size() < invent + 1) {
                        invs1.add(Bukkit.createInventory(null, menuSize + 9, menuName));
                    }

                    GameMap gMap = games.get(iii);

                    List<String> loreList = Lists.newLinkedList();
                    if (gMap.getMatchState() != MatchState.OFFLINE) {
                        if (gMap.getMatchState() == MatchState.WAITINGSTART) {
                            for (String a : SkyWarsReloaded.getMessaging().getFile().getStringList("menu.join_menu.lore.waiting-start")) {
                                loreList.add(ChatColor.translateAlternateColorCodes('&',
                                        a.replace("{playercount}", "" + gMap.getAlivePlayers().size())
                                                .replace("{maxplayers}", "" + gMap.getMaxPlayers())
                                                .replace("{arena}", gMap.getDisplayName())
                                                .replace("{teamsize}", gMap.getTeamSize() + "")
                                                .replace("{aliveplayers}", gMap.getAlivePlayers().size() + "")
                                                .replace("{name}", gMap.getName())
                                ));
                            }
                            /*loreList.add((new Messaging.MessageFormatter()
                                    .setVariable("playercount", "" + gMap.getAlivePlayers().size())
                                    .setVariable("maxplayers", "" + gMap.getMaxPlayers())
                                    .setVariable("arena", gMap.getDisplayName())
                                    .setVariable("teamsize", gMap.getTeamSize() + "")
                                    .setVariable("aliveplayers", gMap.getAlivePlayers().size() + "")
                                    .setVariable("name",gMap.getName())
                                    .format("menu.join_menu.lore.waiting-start")));*/
                        } else if (gMap.getMatchState().equals(MatchState.PLAYING)) {
                            for (String a : SkyWarsReloaded.getMessaging().getFile().getStringList("menu.join_menu.lore.playing")) {
                                loreList.add(ChatColor.translateAlternateColorCodes('&',
                                        a.replace("{playercount}", "" + gMap.getAlivePlayers().size())
                                                .replace("{maxplayers}", "" + gMap.getMaxPlayers())
                                                .replace("{arena}", gMap.getDisplayName())
                                                .replace("{teamsize}", gMap.getTeamSize() + "")
                                                .replace("{aliveplayers}", gMap.getAlivePlayers().size() + "")
                                                .replace("{name}", gMap.getName())
                                ));
                            }
                        } else if (gMap.getMatchState().equals(MatchState.ENDING)) {
                            for (String a : SkyWarsReloaded.getMessaging().getFile().getStringList("menu.join_menu.lore.ending")) {
                                loreList.add(ChatColor.translateAlternateColorCodes('&',
                                        a.replace("{playercount}", "" + gMap.getAlivePlayers().size())
                                                .replace("{maxplayers}", "" + gMap.getMaxPlayers())
                                                .replace("{arena}", gMap.getDisplayName())
                                                .replace("{teamsize}", gMap.getTeamSize() + "")
                                                .replace("{aliveplayers}", gMap.getAlivePlayers().size() + "")
                                                .replace("{name}", gMap.getName())
                                ));
                            }
                        }

                        /*loreList.add((new Messaging.MessageFormatter().setVariable("teamsize", "" + gMap.getTeamSize())
                                .format("signs.teamSize")));
                        loreList.add((new Messaging.MessageFormatter().setVariable("teamcount", "" + gMap.getFullTeams())
                                .setVariable("maxteams", "" + gMap.getTeamCards().size()).format("signs.teamState")));
                        loreList.add((new Messaging.MessageFormatter().setVariable("playercount", "" + gMap.getAlivePlayers().size())
                                .setVariable("maxplayers", "" + gMap.getMaxPlayers()).format("signs.line4team")));
                        loreList.add(" ");
                        loreList.add(new Messaging.MessageFormatter().format("menu.viewteams"));*/

                        double xy = ((double) (gMap.getFullTeams() / gMap.getTeamCards().size()));

                        ItemStack gameIcon = SkyWarsReloaded.getNMS().getItemStack(SkyWarsReloaded.getIM().getItem("blockwaiting"), loreList, ChatColor.translateAlternateColorCodes('&', gMap.getDisplayName()));
                        ItemStack customIcon = null;
                        if (gMap.getCustomJoinMenuItemEnabled()) {
                            customIcon = gMap.getCustomJoinMenuItem();
                        } else {
                            if (gMap.getMatchState().equals(MatchState.PLAYING)) {
                                customIcon = SkyWarsReloaded.getIM().getItem("blockplaying");
                            } else if (gMap.getMatchState().equals(MatchState.ENDING)) {
                                customIcon = SkyWarsReloaded.getIM().getItem("blockending");
                            } else if (gMap.getMatchState().equals(MatchState.WAITINGSTART)) {
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

                        if (gMap.getMatchState().equals(MatchState.PLAYING)) {
                            gameIcon = SkyWarsReloaded.getNMS().getItemStack(customIcon, loreList, ChatColor.translateAlternateColorCodes('&',
                                    new Messaging.MessageFormatter()
                                            .setVariable("playercount", "" + gMap.getAlivePlayers().size())
                                            .setVariable("maxplayers", "" + gMap.getMaxPlayers())
                                            .setVariable("arena", gMap.getDisplayName())
                                            .setVariable("teamsize", gMap.getTeamSize() + "")
                                            .setVariable("aliveplayers", gMap.getAlivePlayers().size() + "")
                                            .setVariable("name", gMap.getName())
                                            .format("menu.join_menu.item_title.playing"))
                            );
                        } else if (gMap.getMatchState().equals(MatchState.ENDING)) {
                            gameIcon = SkyWarsReloaded.getNMS().getItemStack(customIcon, loreList, ChatColor.translateAlternateColorCodes('&',
                                    new Messaging.MessageFormatter()
                                            .setVariable("playercount", "" + gMap.getAlivePlayers().size())
                                            .setVariable("maxplayers", "" + gMap.getMaxPlayers())
                                            .setVariable("arena", gMap.getDisplayName())
                                            .setVariable("teamsize", gMap.getTeamSize() + "")
                                            .setVariable("aliveplayers", gMap.getAlivePlayers().size() + "")
                                            .setVariable("name", gMap.getName())
                                            .format("menu.join_menu.item_title.ending"))

                            );
                        } else if (gMap.getMatchState() == MatchState.WAITINGSTART) {
                            gameIcon = SkyWarsReloaded.getNMS().getItemStack(customIcon, loreList, ChatColor.translateAlternateColorCodes('&',
                                    new Messaging.MessageFormatter()
                                            .setVariable("playercount", "" + gMap.getAlivePlayers().size())
                                            .setVariable("maxplayers", "" + gMap.getMaxPlayers())
                                            .setVariable("arena", gMap.getDisplayName())
                                            .setVariable("teamsize", gMap.getTeamSize() + "")
                                            .setVariable("aliveplayers", gMap.getAlivePlayers().size() + "")
                                            .setVariable("name", gMap.getName())
                                            .format("menu.join_menu.item_title.waiting-start"))

                            );
                            if (xy < 0.75) {
                                gameIcon = SkyWarsReloaded.getNMS().getItemStack(customIcon, loreList, ChatColor.translateAlternateColorCodes('&',
                                        new Messaging.MessageFormatter()
                                                .setVariable("playercount", "" + gMap.getAlivePlayers().size())
                                                .setVariable("maxplayers", "" + gMap.getMaxPlayers())
                                                .setVariable("arena", gMap.getDisplayName())
                                                .setVariable("teamsize", gMap.getTeamSize() + "")
                                                .setVariable("aliveplayers", gMap.getAlivePlayers().size() + "")
                                                .setVariable("name", gMap.getName())
                                                .format("menu.join_menu.item_title.waiting-start"))

                                );
                            }
                            if (xy < 0.50) {
                                gameIcon = SkyWarsReloaded.getNMS().getItemStack(customIcon, loreList, ChatColor.translateAlternateColorCodes('&',
                                        new Messaging.MessageFormatter()
                                                .setVariable("playercount", "" + gMap.getAlivePlayers().size())
                                                .setVariable("maxplayers", "" + gMap.getMaxPlayers())
                                                .setVariable("arena", gMap.getDisplayName())
                                                .setVariable("teamsize", gMap.getTeamSize() + "")
                                                .setVariable("aliveplayers", gMap.getAlivePlayers().size() + "")
                                                .setVariable("name", gMap.getName())
                                                .format("menu.join_menu.item_title.waiting-start"))

                                );
                            }
                            if (xy < 0.25) {
                                gameIcon = SkyWarsReloaded.getNMS().getItemStack(customIcon, loreList, ChatColor.translateAlternateColorCodes('&',
                                        new Messaging.MessageFormatter()
                                                .setVariable("playercount", "" + gMap.getAlivePlayers().size())
                                                .setVariable("maxplayers", "" + gMap.getMaxPlayers())
                                                .setVariable("arena", gMap.getDisplayName())
                                                .setVariable("teamsize", gMap.getTeamSize() + "")
                                                .setVariable("aliveplayers", gMap.getAlivePlayers().size() + "")
                                                .setVariable("name", gMap.getName())
                                                .format("menu.join_menu.item_title.waiting-start"))

                                );
                            }
                        }
                        invs1.get(invent).setItem(iii % menuSize, gameIcon);
                        arenaSlots.put(iii % menuSize, gMap.getName());
                    }
                }
                if (SkyWarsReloaded.getCfg().spectateMenuEnabled()) {
                    ArrayList<Inventory> specs = SkyWarsReloaded.getIC().getMenu("spectateteammenu").getInventories();
                    int i = 0;
                    for (Inventory inv : invs1) {
                        if (specs.get(i) == null) {
                            specs.add(Bukkit.createInventory(null, menuSize, new Messaging.MessageFormatter().format("menu.spectateteamgame-menu-title")));
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

            gMap = GameMap.getMap(arenaSlots.get(event.getSlot()));
            if (gMap == null) {
                return;
            }

            if (gMap.getMatchState() != MatchState.WAITINGSTART) {
                Util.get().playSound(player, player.getLocation(), SkyWarsReloaded.getCfg().getErrorSound(), 1, 1);
                return;
            }

            if (player.hasPermission("sw.join")) {
                if (event.getClick() == ClickType.RIGHT && gMap.getMatchState() == MatchState.WAITINGSTART) {
                    final String n = gMap.getName();
                    if (!SkyWarsReloaded.getIC().hasViewers(n + "teamselect")) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                SkyWarsReloaded.getIC().getMenu(n + "teamselect").update();
                            }
                        }.runTaskLater(SkyWarsReloaded.get(), 5);
                    }
                    SkyWarsReloaded.getIC().show(player, n + "teamselect");
                } else {
                    boolean joined;
                    Party party = Party.getParty(player);
                    if (party != null) {
                        if (party.getLeader().equals(player.getUniqueId())) {
                            if (gMap.getMatchState() == MatchState.WAITINGSTART && gMap.canAddParty(party)) {
                                player.closeInventory();
                                joined = gMap.addPlayers(null, party);
                                if (!joined) {
                                    player.sendMessage(new Messaging.MessageFormatter().format("error.could-not-join2"));
                                }
                            }
                        } else {
                            player.closeInventory();
                            player.sendMessage(new Messaging.MessageFormatter().format("party.onlyleader"));
                        }
                    } else {
                        if (gMap.getMatchState() == MatchState.WAITINGSTART && gMap.canAddPlayer()) {
                            player.closeInventory();
                            joined = gMap.addPlayers(null, player);
                            if (!joined) {
                                player.sendMessage(new Messaging.MessageFormatter().format("error.could-not-join2"));
                            }
                        }
                    }
                }
            }
        });
        SkyWarsReloaded.getIC().getMenu("jointeammenu").setUpdate(update);
    }

}