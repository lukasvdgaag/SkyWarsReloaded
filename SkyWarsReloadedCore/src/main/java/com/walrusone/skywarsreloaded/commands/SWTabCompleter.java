package com.walrusone.skywarsreloaded.commands;

import com.google.common.collect.Lists;
import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.enums.ChestType;
import com.walrusone.skywarsreloaded.enums.LeaderType;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.menus.gameoptions.objects.GameKit;
import com.walrusone.skywarsreloaded.utilities.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SWTabCompleter implements TabCompleter {

    private final SkyWarsReloaded plugin;

    public SWTabCompleter(SkyWarsReloaded plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        List<String> possibilities = Lists.newArrayList();
        List<String> responses = Lists.newArrayList();

        if (command.getName().equalsIgnoreCase("swmap")) {
            if (args.length == 1) {
                for (BaseCmd cmd : plugin.getMapCmdManager().getCommands()) {
                    if (Util.get().hasPerm(cmd.getType(), commandSender, cmd.cmdName)) {
                        possibilities.add(cmd.cmdName);
                    }
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("edit") || args[0].equalsIgnoreCase("register") || args[0].equalsIgnoreCase("unregister") ||
                        args[0].equalsIgnoreCase("refresh") || args[0].equalsIgnoreCase("teamsize") ||
                        args[0].equalsIgnoreCase("name") || args[0].equalsIgnoreCase("delete") ||
                        args[0].equalsIgnoreCase("min") || args[0].equalsIgnoreCase("creator") ||
                        args[0].equalsIgnoreCase("debug") || args[0].equalsIgnoreCase("legacyload")) {
                    if (Util.get().hasPerm("map", commandSender, args[0].toLowerCase())) {
                        for (GameMap map : SkyWarsReloaded.getGameMapMgr().getMapsCopy()) possibilities.add(map.getName());
                    }
                } else if (args[0].equalsIgnoreCase("spawn") && Util.get().hasPerm("map", commandSender, "spawn")) {
                    possibilities = Lists.newArrayList("player", "spec", "look", "lobby", "deathmatch");
                }
            }
        }
        else if (command.getName().equalsIgnoreCase("swkit")) {
            if (args.length == 1) {
                for (BaseCmd cmd : plugin.getKitCmdManager().getCommands()) {
                    if (Util.get().hasPerm(cmd.getType(), commandSender, cmd.cmdName)) {
                        possibilities.add(cmd.cmdName);
                    }
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("enable") || args[0].equalsIgnoreCase("icon") ||
                        args[0].equalsIgnoreCase("lockicon") || args[0].equalsIgnoreCase("load") || args[0].equalsIgnoreCase("lore") ||
                        args[0].equalsIgnoreCase("name") || args[0].equalsIgnoreCase("position") || args[0].equalsIgnoreCase("perm") ||
                        args[0].equalsIgnoreCase("update")) {
                    if (Util.get().hasPerm("kit", commandSender, args[0].toLowerCase())) {
                        for (GameKit kit : GameKit.getKits()) {
                            possibilities.add(kit.getName());
                        }
                    }

                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("lore") && Util.get().hasPerm("kit", commandSender, "lore")) {
                    possibilities = Lists.newArrayList("locked", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17");
                }
            }
        }
        else if (command.getName().equalsIgnoreCase("skywars")) {

            if (args.length == 1) {
                for (BaseCmd cmd : plugin.getMainCmdManager().getCommands()) {
                    if (Util.get().hasPerm(cmd.getType(), commandSender, cmd.cmdName)) {
                        possibilities.add(cmd.cmdName);
                    }
                }
            } else if (args.length == 2) {
                if ((args[0].equalsIgnoreCase("chestadd") || args[0].equalsIgnoreCase("chestedit")) && Util.get().hasPerm("sw", commandSender, args[0].toLowerCase())) {
                    for (ChestType ct : ChestType.values()) {
                        possibilities.add(ct.toString().toLowerCase());
                    }
                } else if ((args[0].equalsIgnoreCase("stats") || args[0].equalsIgnoreCase("stat") ||
                        args[0].equalsIgnoreCase("clearstats")) && Util.get().hasPerm("sw", commandSender, args[0].toLowerCase())) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        possibilities.add(p.getName());
                    }
                } else if ((args[0].equalsIgnoreCase("top") || args[0].equalsIgnoreCase("hologram")) && Util.get().hasPerm("sw", commandSender, args[0].toLowerCase())) {
                    for (String leaderType : SkyWarsReloaded.get().getLeaderTypes()) {
                        possibilities.add(leaderType.toLowerCase());
                    }
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("chestadd") && Util.get().hasPerm("sw", commandSender, "chestadd")) {
                    possibilities = Lists.newArrayList("hand", "inv");
                } else if (args[0].equalsIgnoreCase("stat") && Util.get().hasPerm("sw", commandSender, "stat")) {
                    possibilities = Lists.newArrayList("wins", "losses", "kills", "deaths", "xp", "pareffect", "proeffect", "glasscolor", "killsound", "winsound");
                } else if (args[0].equalsIgnoreCase("hologram") && Util.get().hasPerm("sw", commandSender, "hologram")) {
                    if (plugin.getHologramManager() != null) {
                        LeaderType lt = LeaderType.matchType(args[1].toUpperCase());
                        return plugin.getHologramManager().getFormats(lt);
                    }

                    return new ArrayList<>();
                }
            } else if (args.length == 4) {
                if (args[0].equalsIgnoreCase("stat") && Util.get().hasPerm("sw", commandSender, "stat")) {
                    possibilities = Lists.newArrayList("set", "add", "remove");
                }
            }
        }
        else {
            return null;
        }

        String currentUserInput = args[args.length - 1].toLowerCase();
        if (currentUserInput.isEmpty()) {
            responses = possibilities;
        } else {
            for (String possibility : possibilities) {
                if (possibility.toLowerCase().startsWith(currentUserInput)) responses.add(possibility);
            }
        }
        return responses;
    }
}
