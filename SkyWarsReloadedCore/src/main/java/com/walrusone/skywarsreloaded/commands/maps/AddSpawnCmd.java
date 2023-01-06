package com.walrusone.skywarsreloaded.commands.maps;

import com.google.common.collect.Lists;
import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.game.TeamCard;
import com.walrusone.skywarsreloaded.menus.gameoptions.objects.CoordLoc;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.Util;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddSpawnCmd extends com.walrusone.skywarsreloaded.commands.BaseCmd {
    public AddSpawnCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "spawn";
        alias = new String[]{"sp", "setspawn"};
        argLength = 3;
        maxArgs = 3;
    }

    public boolean run(CommandSender sender, Player player, String[] args) {
        if (SkyWarsReloaded.getCfg().getSpawn() != null) {
            if (args.length < 2) { return false; }

            String type = args[1];
            GameMap gMap = GameMap.getMap(player.getLocation().getWorld().getName());
            if ((gMap == null) || (!gMap.isEditing())) {
                player.sendMessage(new Messaging.MessageFormatter().format("error.map-not-editing"));
                return true;
            }

            if (args.length == 2) {

                if ((type.equalsIgnoreCase("player")) || (type.equalsIgnoreCase("p"))) {
                    if (gMap.getTeamSize() == 1 || !SkyWarsReloaded.getCfg().isUseSeparateCages()) {

                        // Get index to add by getting current size (size = maxIndex + 1)
                        int newTeamNumber = gMap.getTeamCards().size();
                        TeamCard team = gMap.getTeamCardByIndex(newTeamNumber);

                        // Save location to referenced team or create new team with this location
                        if (team == null) gMap.addTeamCard(Lists.newArrayList(new CoordLoc(player.getLocation())));
                        else gMap.addSpawnLocationForTeam(team, player.getLocation());

                        // Display feedback to player
                        player.getLocation().getBlock().setType(Material.DIAMOND_BLOCK);
                        player.sendMessage(new Messaging.MessageFormatter()
                                .setVariable("num", "" + gMap.getMaxPlayers())
                                .setVariable("mapname", gMap.getDisplayName())
                                .format("maps.addSpawn"));
                    } else {
                        player.sendMessage(ChatColor.RED + "You have team mode enabled! You must specify a team to add this spawn to.");
                    }

                } else if ((type.equalsIgnoreCase("spec")) || (type.equalsIgnoreCase("s"))) {
                    gMap.setSpectateSpawn(player.getLocation());
                    player.sendMessage(new Messaging.MessageFormatter()
                            .setVariable("mapname", gMap.getDisplayName())
                            .format("maps.specSpawn"));

                } else if ((type.equalsIgnoreCase("deathmatch")) || (type.equalsIgnoreCase("dm")) || (type.equalsIgnoreCase("d"))) {
                    gMap.addDeathMatchSpawn(player.getLocation());
                    player.getLocation().getBlock().setType(Material.EMERALD_BLOCK);
                    player.sendMessage(new Messaging.MessageFormatter()
                            .setVariable("num", "" + gMap.getDeathMatchSpawns().size())
                            .setVariable("mapname", gMap.getDisplayName())
                            .format("maps.addDeathSpawn"));

                } else if ((type.equalsIgnoreCase("look")) || type.equals("eye")) {
                    gMap.setLookDirection(player.getLocation());
                    player.sendMessage(new Messaging.MessageFormatter()
                            .setVariable("mapname", gMap.getDisplayName())
                            .format("maps.setLookDirection"));

                } else if (type.equalsIgnoreCase("lobby") || type.equalsIgnoreCase("waiting")) {
                    gMap.setWaitingLobbySpawn(player.getLocation());
                    player.sendMessage(new Messaging.MessageFormatter()
                            .setVariable("mapname", gMap.getDisplayName())
                            .format("maps.waitingLobbySpawn"));

                } else {
                    player.sendMessage(net.md_5.bungee.api.ChatColor.RED + "Type must be: player, spec, eye, deathmatch or lobby");
                }
                return true;
            }
            else if (args.length == 3) {
                if ((type.equalsIgnoreCase("player")) || (type.equalsIgnoreCase("p"))) {
                    if (!Util.get().isInteger(args[2]) || Integer.parseInt(args[2]) <= 0) {
                        player.sendMessage(ChatColor.RED + "You must enter a valid number greater than 0");
                        return true;
                    }
                    if (gMap.getTeamSize() > 1 && SkyWarsReloaded.getCfg().isUseSeparateCages()) {
                        // Convert user input to index
                        int newTeamNumber = Integer.parseInt(args[2]) - 1;
                        TeamCard team = gMap.getTeamCardByIndex(newTeamNumber);
                        if (team == null) gMap.addTeamCard(Lists.newArrayList(new CoordLoc(player.getLocation())));
                        else gMap.addSpawnLocationForTeam(team, player.getLocation());

                        // Display feedback to player
                        player.getLocation().getBlock().setType(Material.DIAMOND_BLOCK);
                        player.sendMessage(new Messaging.MessageFormatter()
                                .setVariable("num", "" + gMap.getMaxPlayers())
                                .setVariable("mapname", gMap.getDisplayName())
                                .format("maps.addSpawn"));

                    } else {
                        if (gMap.getTeamSize() == 1) {
                            player.sendMessage(ChatColor.RED + "You have solo mode enabled! There are no teams to add a spawn to. Use: /swm spawn player");
                        } else {
                            player.sendMessage(ChatColor.RED + "You do not have separate cages enabled! You can only add one spawn per team. Use: /swm spawn player");
                        }
                    }
                }
                else {
                    player.sendMessage(ChatColor.RED + "Correct usage: /swm spawn <type> [team]");
                }
                return true;
            }
        }
        return true;
    }
}
