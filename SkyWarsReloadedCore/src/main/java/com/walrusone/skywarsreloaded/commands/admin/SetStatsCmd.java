package com.walrusone.skywarsreloaded.commands.admin;

import com.walrusone.skywarsreloaded.commands.BaseCmd;
import com.walrusone.skywarsreloaded.database.DataStorage;
import com.walrusone.skywarsreloaded.managers.PlayerStat;
import com.walrusone.skywarsreloaded.menus.playeroptions.ParticleEffectOption;
import com.walrusone.skywarsreloaded.menus.playeroptions.ProjectileEffectOption;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.Util;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SetStatsCmd extends BaseCmd {
    public SetStatsCmd(String t) {
        type = t;
        forcePlayer = false;
        cmdName = "stat";
        alias = new String[]{"st"};
        argLength = 5;
    }

    public boolean run() {
        Player swPlayer = null;
        for (Player playerMatch : org.bukkit.Bukkit.getOnlinePlayers()) {
            if (ChatColor.stripColor(playerMatch.getName()).equalsIgnoreCase(ChatColor.stripColor(args[1]))) {
                swPlayer = playerMatch;
            }
        }

        if (swPlayer != null) {
            String method = args[3];
            String stat = args[2];
            if (Util.get().isInteger(args[4])) {
                PlayerStat pStat = PlayerStat.getPlayerStats(swPlayer);
                if (pStat != null) {
                    if (stat.equalsIgnoreCase("wins")) {
                        if (Util.get().isInteger(args[4])) {
                            if ((method.equalsIgnoreCase("set")) || (method.equalsIgnoreCase("add")) || (method.equalsIgnoreCase("remove"))) {
                                int value = Integer.parseInt(args[4]);
                                int currentValue = pStat.getWins();
                                int newValue = getNewValue(method, currentValue, value);
                                pStat.setWins(newValue);
                                DataStorage.get().saveStats(pStat);
                                sender.sendMessage(new Messaging.MessageFormatter().setVariable("player", args[1])
                                        .setVariable("stat", stat).setVariable("amount", "" + newValue).format("command.setstat"));
                            } else {
                                sender.sendMessage(new Messaging.MessageFormatter().format("command.method-must-be"));
                            }
                        } else {
                            sender.sendMessage(new Messaging.MessageFormatter().format("command.must-be-int"));
                        }
                    } else if (stat.equalsIgnoreCase("losses")) {
                        if (Util.get().isInteger(args[4])) {
                            if ((method.equalsIgnoreCase("set")) || (method.equalsIgnoreCase("add")) || (method.equalsIgnoreCase("remove"))) {
                                int value = Integer.parseInt(args[4]);
                                int currentValue = pStat.getLosses();
                                int newValue = getNewValue(method, currentValue, value);
                                pStat.setLosts(newValue);
                                DataStorage.get().saveStats(pStat);
                                sender.sendMessage(new Messaging.MessageFormatter().setVariable("player", args[1])
                                        .setVariable("stat", stat).setVariable("amount", "" + newValue).format("command.setstat"));
                            } else {
                                sender.sendMessage(new Messaging.MessageFormatter().format("command.method-must-be"));
                            }
                        } else {
                            sender.sendMessage(new Messaging.MessageFormatter().format("command.must-be-int"));
                        }
                    } else if (stat.equalsIgnoreCase("kills")) {
                        if (Util.get().isInteger(args[4])) {
                            if ((method.equalsIgnoreCase("set")) || (method.equalsIgnoreCase("add")) || (method.equalsIgnoreCase("remove"))) {
                                int value = Integer.parseInt(args[4]);
                                int currentValue = pStat.getKills();
                                int newValue = getNewValue(method, currentValue, value);
                                pStat.setKills(newValue);
                                DataStorage.get().saveStats(pStat);
                                sender.sendMessage(new Messaging.MessageFormatter().setVariable("player", args[1])
                                        .setVariable("stat", stat).setVariable("amount", "" + newValue).format("command.setstat"));
                            } else {
                                sender.sendMessage(new Messaging.MessageFormatter().format("command.method-must-be"));
                            }
                        } else {
                            sender.sendMessage(new Messaging.MessageFormatter().format("command.must-be-int"));
                        }
                    } else if (stat.equalsIgnoreCase("deaths")) {
                        if (Util.get().isInteger(args[4])) {
                            if ((method.equalsIgnoreCase("set")) || (method.equalsIgnoreCase("add")) || (method.equalsIgnoreCase("remove"))) {
                                int value = Integer.parseInt(args[4]);
                                int currentValue = pStat.getDeaths();
                                int newValue = getNewValue(method, currentValue, value);
                                pStat.setDeaths(newValue);
                                DataStorage.get().saveStats(pStat);
                                sender.sendMessage(new Messaging.MessageFormatter().setVariable("player", args[1])
                                        .setVariable("stat", stat).setVariable("amount", "" + newValue).format("command.setstat"));
                            } else {
                                sender.sendMessage(new Messaging.MessageFormatter().format("command.method-must-be"));
                            }
                        } else {
                            sender.sendMessage(new Messaging.MessageFormatter().format("command.must-be-int"));
                        }
                    }  else if (stat.equalsIgnoreCase("xp")) {
                        if (Util.get().isInteger(args[4])) {
                            if ((method.equalsIgnoreCase("set")) || (method.equalsIgnoreCase("add")) || (method.equalsIgnoreCase("remove"))) {
                                int value = Integer.parseInt(args[4]);
                                int currentValue = pStat.getXp();
                                int newValue = getNewValue(method, currentValue, value);
                                pStat.setXp(newValue);
                                DataStorage.get().saveStats(pStat);
                                if (com.walrusone.skywarsreloaded.SkyWarsReloaded.getCfg().displayPlayerExeperience()) {
                                    Util.get().setPlayerExperience(swPlayer, newValue);
                                }
                                sender.sendMessage(new Messaging.MessageFormatter().setVariable("player", args[1])
                                        .setVariable("stat", stat).setVariable("amount", "" + newValue).format("command.setstat"));
                            } else {
                                sender.sendMessage(new Messaging.MessageFormatter().format("command.method-must-be"));
                            }
                        } else {
                            sender.sendMessage(new Messaging.MessageFormatter().format("command.must-be-int"));
                        }
                    } else if (args[2].equalsIgnoreCase("pareffect")) {
                        if (ParticleEffectOption.getPlayerOptionByKey(args[3]) != null) {
                            pStat.setParticleEffect(args[3].toLowerCase());
                            DataStorage.get().saveStats(pStat);
                            sender.sendMessage(new Messaging.MessageFormatter().setVariable("player", args[1])
                                    .setVariable("stat", args[2]).setVariable("amount", args[3]).format("command.setstat"));
                        } else {
                            sender.sendMessage(new Messaging.MessageFormatter().format("command.invalid-effect"));
                        }
                    } else if (args[2].equalsIgnoreCase("proeffect")) {
                        if (ProjectileEffectOption.getPlayerOptionByKey(args[3]) != null) {
                            pStat.setProjectileEffect(args[3].toLowerCase());
                            DataStorage.get().saveStats(pStat);
                            sender.sendMessage(new Messaging.MessageFormatter().setVariable("player", args[1])
                                    .setVariable("stat", args[2]).setVariable("amount", args[3]).format("command.setstat"));
                        } else {
                            sender.sendMessage(new Messaging.MessageFormatter().format("command.invalid-effect"));
                        }
                    } else if (args[2].equalsIgnoreCase("glasscolor")) {
                        if (Util.get().getByteFromColor(args[3]) != -1) {
                            pStat.setGlassColor(args[3].toLowerCase());
                            DataStorage.get().saveStats(pStat);
                            sender.sendMessage(new Messaging.MessageFormatter().setVariable("player", args[1])
                                    .setVariable("stat", args[2]).setVariable("amount", args[3]).format("command.setstat"));
                        } else {
                            sender.sendMessage(new Messaging.MessageFormatter().format("command.invalid-color"));
                        }
                    } else if (args[2].equalsIgnoreCase("killsound")) {
                        try {
                            Sound sound = Sound.valueOf(args[3].toUpperCase());
                            pStat.setKillSound(args[3].toUpperCase());
                            DataStorage.get().saveStats(pStat);
                            sender.sendMessage(new Messaging.MessageFormatter().setVariable("player", args[1])
                                    .setVariable("stat", args[2]).setVariable("amount", args[3]).format("command.setstat"));
                        } catch (IllegalArgumentException e) {
                            sender.sendMessage(new Messaging.MessageFormatter().format("command.invalid-sound"));
                        }
                    } else if (args[2].equalsIgnoreCase("winsound")) {
                        try {
                            Sound sound = Sound.valueOf(args[3].toUpperCase());
                            pStat.setWinSound(args[3].toUpperCase());
                            DataStorage.get().saveStats(pStat);
                            sender.sendMessage(new Messaging.MessageFormatter().setVariable("player", args[1])
                                    .setVariable("stat", args[2]).setVariable("amount", args[3]).format("command.setstat"));
                        } catch (IllegalArgumentException e) {
                            sender.sendMessage(new Messaging.MessageFormatter().format("command.invalid-sound"));
                        }
                    } else {
                        sender.sendMessage(new Messaging.MessageFormatter().format("command.stat-types"));
                    }
                }
            } else {
                sender.sendMessage(new Messaging.MessageFormatter().format("command.must-be-int"));
            }
        } else {
            sender.sendMessage(new Messaging.MessageFormatter().format("command.must-be-online"));
        }

        return true;
    }

    private int getNewValue(String method, int currentValue, int value) {
        if (method.equalsIgnoreCase("set"))
            return value;
        if (method.equalsIgnoreCase("add")) {
            return currentValue + value;
        }
        return currentValue - value;
    }
}
