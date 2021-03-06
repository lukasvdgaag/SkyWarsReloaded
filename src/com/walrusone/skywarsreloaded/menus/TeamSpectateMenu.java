package com.walrusone.skywarsreloaded.menus;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.enums.MatchState;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.managers.MatchManager;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class TeamSpectateMenu {

    public TeamSpectateMenu(GameMap gMap) {
        String menuName = new Messaging.MessageFormatter().setVariable("mapname", gMap.getDisplayName()).format("menu.teamspectate-menu-title");
        int menuSize = 27;
        Inventory menu = Bukkit.createInventory(null, menuSize + 9, menuName);
        ArrayList<Inventory> invs = new ArrayList<>();
        invs.add(menu);

        SkyWarsReloaded.getIC().create(gMap.getName() + "teamspectate", invs, event -> {
            Player player = event.getPlayer();

            String name = event.getName();
            if (name.equalsIgnoreCase(SkyWarsReloaded.getNMS().getItemName(SkyWarsReloaded.getIM().getItem("exitMenuItem")))) {
                if (!SkyWarsReloaded.getIC().hasViewers("spectateteammenu")) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            SkyWarsReloaded.getIC().getMenu("jointeammenu").update();
                        }
                    }.runTaskLater(SkyWarsReloaded.get(), 5);
                }
                SkyWarsReloaded.getIC().show(player, "spectateteammenu");
                return;
            }

            if (player.hasPermission("sw.spectate")) {
                player.closeInventory();
                if (gMap.getMatchState() != MatchState.OFFLINE && gMap.getMatchState() != MatchState.ENDING) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            SkyWarsReloaded.get().getPlayerManager().addSpectator(gMap, player);
                        }
                    }.runTaskLater(SkyWarsReloaded.get(), 5);
                }
            }
        });
    }

}