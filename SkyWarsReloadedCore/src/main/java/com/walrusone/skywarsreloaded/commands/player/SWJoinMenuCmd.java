package com.walrusone.skywarsreloaded.commands.player;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.commands.BaseCmd;
import com.walrusone.skywarsreloaded.enums.GameType;
import com.walrusone.skywarsreloaded.enums.MatchState;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.Util;
import me.gaagjescraft.network.team.skywarsreloaded.extension.NoArenaAction;
import me.gaagjescraft.network.team.skywarsreloaded.extension.SWExtension;
import me.gaagjescraft.network.team.skywarsreloaded.extension.menus.SingleJoinMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class SWJoinMenuCmd extends BaseCmd {

    public SWJoinMenuCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "joinmenu";
        alias = new String[]{"jm"};
        argLength = 1; // /sw joinmenu <solo|team>
    }

    public boolean run(CommandSender sender, Player player, String[] args) {
        if (args.length < 2) {
            // Sin argumento: abrir menú general para elegir modo (solo o team)
            openGeneralJoinMenu(player);
            return true;
        }

        String mode = args[1].toLowerCase();

        if (!(mode.equals("solo") || mode.equals("team"))) {
            player.sendMessage((new Messaging.MessageFormatter()).format("helpList.sw.joinmenu"));
            return true;
        }

        if (Bukkit.getPluginManager().isPluginEnabled("Skywars-Extension")) {
            NoArenaAction action = NoArenaAction.valueOf(
                    SWExtension.get().getConfig().getString("no_arena_specified_action", "OPEN_CUSTOM_JOIN_MENU")
            );

            if (action == NoArenaAction.OPEN_CUSTOM_JOIN_MENU) {
                new SingleJoinMenu().openMenu(player, 1);
                return true;
            } else if (action == NoArenaAction.JOIN_RANDOM) {
                GameType gameType = mode.equals("solo") ? GameType.SINGLE : GameType.TEAM;

                List<GameMap> maps = SkyWarsReloaded.getGameMapMgr().getPlayableArenas(gameType).stream()
                        .filter(map -> (map.getMatchState() == MatchState.WAITINGSTART || map.getMatchState() == MatchState.WAITINGLOBBY))
                        .filter(map -> map.canAddPlayer(player))
                        .collect(Collectors.toList());


                if (maps.isEmpty()) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            SWExtension.get().getConfig().getString("joingame-menu-no-arenas", "&cNo arenas available")));
                } else {
                    GameMap map = maps.get(new Random().nextInt(maps.size()));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            SWExtension.get().getConfig().getString("joingame-menu-join-noname", "&aJoining arena")));

                    boolean success = map.addPlayers(null, player);
                    if (success) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                SWExtension.get().getConfig().getString("joingame-menu-join", "&aJoining arena {arena}").replace("{arena}", map.getName())));
                    } else {
                        player.sendMessage((new Messaging.MessageFormatter()).format("error.could-not-join2"));
                    }
                }
                return true;
            }
        }

        // Abrir menú correspondiente
        if (mode.equals("solo")) {
            if (!SkyWarsReloaded.getIC().hasViewers("joinsinglemenu")) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        SkyWarsReloaded.getIC().getMenu("joinsinglemenu").update();
                    }
                }.runTaskLater(SkyWarsReloaded.get(), 5);
            }
            SkyWarsReloaded.getIC().show(player, "joinsinglemenu");
        } else {
            if (!SkyWarsReloaded.getIC().hasViewers("jointeammenu")) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        SkyWarsReloaded.getIC().getMenu("jointeammenu").update();
                    }
                }.runTaskLater(SkyWarsReloaded.get(), 5);
            }
            SkyWarsReloaded.getIC().show(player, "jointeammenu");
        }

        return true;
    }

    private void openGeneralJoinMenu(Player player) {
        // Abre el menú que permita elegir entre solo o team
        if (SkyWarsReloaded.getIC().has("joinmenu")) {
            Util.get().playSound(player, player.getLocation(), SkyWarsReloaded.getCfg().getOpenJoinMenuSound(), 1, 1);
            SkyWarsReloaded.getIC().show(player, "joinmenu");
        } else {
            player.sendMessage(ChatColor.RED + "No se encontró el menú de selección.");
        }
    }
}
