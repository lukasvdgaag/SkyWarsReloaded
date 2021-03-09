package com.walrusone.skywarsreloaded.menus.gameoptions;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.enums.Vote;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.managers.MatchManager;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

public class VotingMenu {

    private static final String menuName = new Messaging.MessageFormatter().format("menu.options-menu-title");

    public VotingMenu(final Player player) {
        int menuSize = 27;
        GameMap gMap = MatchManager.get().getPlayerMap(player);
        if (gMap != null && player != null) {
            Inventory inv = Bukkit.createInventory(null, menuSize + 9, menuName);

            if (SkyWarsReloaded.getCfg().isChestVoteEnabled()) {
                if (player.hasPermission("sw.chestvote")) {
                    inv.setItem(SkyWarsReloaded.getCfg().getChestVotePos(), SkyWarsReloaded.getIM().getItem("chestvote"));
                } else {
                    inv.setItem(SkyWarsReloaded.getCfg().getChestVotePos(), SkyWarsReloaded.getIM().getItem("nopermission"));
                }
            }
            if (SkyWarsReloaded.getCfg().isHealthVoteEnabled()) {
                if (player.hasPermission("sw.healthvote")) {
                    inv.setItem(SkyWarsReloaded.getCfg().getHealthVotePos(), SkyWarsReloaded.getIM().getItem("healthvote"));
                } else {
                    inv.setItem(SkyWarsReloaded.getCfg().getHealthVotePos(), SkyWarsReloaded.getIM().getItem("nopermission"));
                }
            }
            if (SkyWarsReloaded.getCfg().isTimeVoteEnabled()) {
                if (player.hasPermission("sw.timevote")) {
                    inv.setItem(SkyWarsReloaded.getCfg().getTimeVotePos(), SkyWarsReloaded.getIM().getItem("timevote"));
                } else {
                    inv.setItem(SkyWarsReloaded.getCfg().getTimeVotePos(), SkyWarsReloaded.getIM().getItem("nopermission"));
                }
            }
            if (SkyWarsReloaded.getCfg().isWeatherVoteEnabled()) {
                if (player.hasPermission("sw.weathervote")) {
                    inv.setItem(SkyWarsReloaded.getCfg().getWeatherVotePos(), SkyWarsReloaded.getIM().getItem("weathervote"));
                } else {
                    inv.setItem(SkyWarsReloaded.getCfg().getWeatherVotePos(), SkyWarsReloaded.getIM().getItem("nopermission"));
                }
            }
            if (SkyWarsReloaded.getCfg().isModifierVoteEnabled()) {
                if (player.hasPermission("sw.modifiervote")) {
                    inv.setItem(SkyWarsReloaded.getCfg().getModifierVotePos(), SkyWarsReloaded.getIM().getItem("modifiervote"));
                } else {
                    inv.setItem(SkyWarsReloaded.getCfg().getModifierVotePos(), SkyWarsReloaded.getIM().getItem("nopermission"));
                }
            }

            ArrayList<Inventory> invs = new ArrayList<>();
            invs.add(inv);

            SkyWarsReloaded.getIC().create(player, invs, event -> {
                String name = event.getName();

                if (name.equalsIgnoreCase(new Messaging.MessageFormatter().format("items.chest-item"))) {
                    SkyWarsReloaded.getIC().show(player, gMap.getChestOption().getKey());

                    Vote vote = gMap.getChestOption().getVote(gMap.getPlayerCard(player));
                    if (vote == Vote.CHESTBASIC)
                        Util.get().glowItem(player.getOpenInventory().getTopInventory(),11);
                    else if (vote == Vote.CHESTNORMAL)
                        Util.get().glowItem(player.getOpenInventory().getTopInventory(),13);
                    else if (vote == Vote.CHESTOP)
                        Util.get().glowItem(player.getOpenInventory().getTopInventory(),15);
                    else if (vote == Vote.CHESTSCAVENGER)
                        Util.get().glowItem(player.getOpenInventory().getTopInventory(),17);
                    Util.get().playSound(player, player.getLocation(), SkyWarsReloaded.getCfg().getOpenChestMenuSound(), 1, 1);
                } else if (name.equalsIgnoreCase(new Messaging.MessageFormatter().format("items.health-item"))) {
                    SkyWarsReloaded.getIC().show(player, gMap.getHealthOption().getKey());

                    Vote vote = gMap.getHealthOption().getVote(gMap.getPlayerCard(player));
                    if (vote == Vote.HEALTHFIVE)
                        Util.get().glowItem(player.getOpenInventory().getTopInventory(),11);
                    else if (vote == Vote.HEALTHTEN)
                        Util.get().glowItem(player.getOpenInventory().getTopInventory(),13);
                    else if (vote == Vote.HEALTHFIFTEEN)
                        Util.get().glowItem(player.getOpenInventory().getTopInventory(),15);
                    else if (vote == Vote.HEALTHTWENTY)
                        Util.get().glowItem(player.getOpenInventory().getTopInventory(),17);
                    Util.get().playSound(player, player.getLocation(), SkyWarsReloaded.getCfg().getOpenHealthMenuSound(), 1, 1);
                } else if (name.equalsIgnoreCase(new Messaging.MessageFormatter().format("items.time-item"))) {
                    SkyWarsReloaded.getIC().show(player, gMap.getTimeOption().getKey());

                    Vote vote = gMap.getTimeOption().getVote(gMap.getPlayerCard(player));
                    if (vote == Vote.TIMEDAWN)
                        Util.get().glowItem(player.getOpenInventory().getTopInventory(),11);
                    else if (vote == Vote.TIMENOON)
                        Util.get().glowItem(player.getOpenInventory().getTopInventory(),13);
                    else if (vote == Vote.TIMEDUSK)
                        Util.get().glowItem(player.getOpenInventory().getTopInventory(),15);
                    else if (vote == Vote.TIMEMIDNIGHT)
                        Util.get().glowItem(player.getOpenInventory().getTopInventory(),17);
                    Util.get().playSound(player, player.getLocation(), SkyWarsReloaded.getCfg().getOpenTimeMenuSound(), 1, 1);
                } else if (name.equalsIgnoreCase(new Messaging.MessageFormatter().format("items.weather-item"))) {
                    SkyWarsReloaded.getIC().show(player, gMap.getWeatherOption().getKey());

                    Vote vote = gMap.getWeatherOption().getVote(gMap.getPlayerCard(player));
                    if (vote == Vote.WEATHERSUN)
                        Util.get().glowItem(player.getOpenInventory().getTopInventory(),11);
                    else if (vote == Vote.WEATHERRAIN)
                        Util.get().glowItem(player.getOpenInventory().getTopInventory(),13);
                    else if (vote == Vote.WEATHERTHUNDER)
                        Util.get().glowItem(player.getOpenInventory().getTopInventory(),15);
                    else if (vote == Vote.WEATHERSNOW)
                        Util.get().glowItem(player.getOpenInventory().getTopInventory(),17);
                    Util.get().playSound(player, player.getLocation(), SkyWarsReloaded.getCfg().getOpenWeatherMenuSound(), 1, 1);
                } else if (name.equalsIgnoreCase(new Messaging.MessageFormatter().format("items.modifier-item"))) {
                    SkyWarsReloaded.getIC().show(player, gMap.getModifierOption().getKey());

                    Vote vote = gMap.getModifierOption().getVote(gMap.getPlayerCard(player));
                    if (vote == Vote.MODIFIERSPEED)
                        Util.get().glowItem(player.getOpenInventory().getTopInventory(),11);
                    else if (vote == Vote.MODIFIERJUMP)
                        Util.get().glowItem(player.getOpenInventory().getTopInventory(),13);
                    else if (vote == Vote.MODIFIERSTRENGTH)
                        Util.get().glowItem(player.getOpenInventory().getTopInventory(),15);
                    else if (vote == Vote.MODIFIERNONE)
                        Util.get().glowItem(player.getOpenInventory().getTopInventory(),17);

                    Util.get().playSound(player, player.getLocation(), SkyWarsReloaded.getCfg().getOpenModifierMenuSound(), 1, 1);
                } else if (name.equalsIgnoreCase(new Messaging.MessageFormatter().format("items.exit-menu-item"))) {
                    player.closeInventory();
                }
            });

            SkyWarsReloaded.getIC().show(player, null);
        }

    }
}