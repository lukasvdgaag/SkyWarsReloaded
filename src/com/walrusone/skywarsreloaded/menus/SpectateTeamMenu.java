package com.walrusone.skywarsreloaded.menus;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.enums.MatchState;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.managers.MatchManager;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class SpectateTeamMenu {

    private static final String menuName = new Messaging.MessageFormatter().format("menu.spectateteamgame-menu-title");

    public SpectateTeamMenu() {
        int menuSize = 45;
        Inventory menu = Bukkit.createInventory(null, menuSize + 9, menuName);
        ArrayList<Inventory> invs = new ArrayList<>();
        invs.add(menu);

        SkyWarsReloaded.getIC().create("spectateteammenu", invs, event -> {
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

            if (!JoinTeamMenu.arenaSlots.containsKey(event.getSlot())) {
                return;
            }

            gMap = GameMap.getMap(JoinTeamMenu.arenaSlots.get(event.getSlot()));
            if (gMap == null) {
                return;
            }

            if (event.getClick() == ClickType.RIGHT) {
                final String n = gMap.getName();
                if (!SkyWarsReloaded.getIC().hasViewers(n + "teamspectate")) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            SkyWarsReloaded.getIC().getMenu(n + "teamselect").update();
                        }
                    }.runTaskLater(SkyWarsReloaded.get(), 5);
                }
                SkyWarsReloaded.getIC().show(player, n + "teamspectate");
            } else {
                if (player.hasPermission("sw.spectate")) {
                    player.closeInventory();
                    if (gMap.getMatchState() != MatchState.OFFLINE && gMap.getMatchState() != MatchState.ENDING) {
                        SkyWarsReloaded.get().getPlayerManager().addSpectator(gMap, player);
                    }
                }
            }
        });
    }

}