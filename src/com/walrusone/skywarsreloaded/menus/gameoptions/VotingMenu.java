package com.walrusone.skywarsreloaded.menus.gameoptions;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
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
                    Util.get().playSound(player, player.getLocation(), SkyWarsReloaded.getCfg().getOpenChestMenuSound(), 1, 1);
                } else if (name.equalsIgnoreCase(new Messaging.MessageFormatter().format("items.health-item"))) {
                    SkyWarsReloaded.getIC().show(player, gMap.getHealthOption().getKey());
                    Util.get().playSound(player, player.getLocation(), SkyWarsReloaded.getCfg().getOpenHealthMenuSound(), 1, 1);
                } else if (name.equalsIgnoreCase(new Messaging.MessageFormatter().format("items.time-item"))) {
                    SkyWarsReloaded.getIC().show(player, gMap.getTimeOption().getKey());
                    Util.get().playSound(player, player.getLocation(), SkyWarsReloaded.getCfg().getOpenTimeMenuSound(), 1, 1);
                } else if (name.equalsIgnoreCase(new Messaging.MessageFormatter().format("items.weather-item"))) {
                    SkyWarsReloaded.getIC().show(player, gMap.getWeatherOption().getKey());
                    Util.get().playSound(player, player.getLocation(), SkyWarsReloaded.getCfg().getOpenWeatherMenuSound(), 1, 1);
                } else if (name.equalsIgnoreCase(new Messaging.MessageFormatter().format("items.modifier-item"))) {
                    SkyWarsReloaded.getIC().show(player, gMap.getModifierOption().getKey());
                    Util.get().playSound(player, player.getLocation(), SkyWarsReloaded.getCfg().getOpenModifierMenuSound(), 1, 1);
                } else if (name.equalsIgnoreCase(new Messaging.MessageFormatter().format("items.exit-menu-item"))) {
                    player.closeInventory();
                }
            });

            SkyWarsReloaded.getIC().show(player, null);
        }

    }
}