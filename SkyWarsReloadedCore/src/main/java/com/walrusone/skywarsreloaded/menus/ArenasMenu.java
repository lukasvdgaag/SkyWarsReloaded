package com.walrusone.skywarsreloaded.menus;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.game.GameMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ArenasMenu {

    private static final String menuName = ChatColor.DARK_PURPLE + "Arenas Manager";
    private static int menuSize = 27;

    public ArenasMenu() {
        Inventory menu = Bukkit.createInventory(null, menuSize + 9, menuName);
        ArrayList<Inventory> invs = new ArrayList<>();
        invs.add(menu);

        Runnable update = () -> {
            if ((SkyWarsReloaded.getIC().hasViewers("arenasmenu"))) {
                ArrayList<GameMap> maps = GameMap.getSortedArenas();
                ArrayList<Inventory> invs1 = SkyWarsReloaded.getIC().getMenu("arenasmenu").getInventories();

                for (Inventory inv : invs1) {
                    for (int i = 0; i < menuSize; i++) {
                        inv.setItem(i, new ItemStack(Material.AIR, 1));
                    }
                }

                List<String> lores = new ArrayList<>();
                int i = 0;
                for (GameMap gMap : maps) {
                    int index = Math.floorDiv(i, menuSize);
                    if (invs1.isEmpty() || invs1.size() < index + 1) {
                        invs1.add(Bukkit.createInventory(null, menuSize + 9, menuName));
                    }
                    ItemStack item = SkyWarsReloaded.getNMS().getColorItem("WOOL", (byte) 13);
                    if (!gMap.isRegistered()) {
                        item = SkyWarsReloaded.getNMS().getColorItem("WOOL", (byte) 14);
                    }

                    lores.clear();
                    lores.add(ChatColor.AQUA + "Display Name: " + ChatColor.translateAlternateColorCodes('&', gMap.getDisplayName()));
                    lores.add(ChatColor.AQUA + "Creator: " + ChatColor.translateAlternateColorCodes('&', gMap.getDesigner()));
                    if (gMap.isRegistered()) {
                        lores.add(ChatColor.AQUA + "Status: " + ChatColor.GREEN + "REGISTERED");
                    } else {
                        lores.add(ChatColor.AQUA + "Status: " + ChatColor.RED + "UNREGISTERED");
                    }
                    lores.add(ChatColor.AQUA + "Match State: " + ChatColor.GOLD + gMap.getMatchState().toString());
                    lores.add(ChatColor.AQUA + "Minimum Players: " + ChatColor.GOLD + gMap.getMinTeams());
                    lores.add(ChatColor.AQUA + "Current Players: " + ChatColor.GOLD + gMap.getAlivePlayers().size() + " of " + gMap.getMaxPlayers());
                    lores.add(ChatColor.AQUA + "Number of Join Signs: " + ChatColor.GOLD + gMap.getSigns().size());
                    lores.add(ChatColor.AQUA + "Cage Type: " + ChatColor.GOLD + gMap.getCage().getType().toString());
                    invs1.get(index).setItem(i % menuSize, SkyWarsReloaded.getNMS().getItemStack(item, lores, ChatColor.DARK_PURPLE + gMap.getName()));
                    Bukkit.getConsoleSender().sendMessage("DEBUG #3 : inv " + index + ", slot " + (i % menuSize) + " has been set to arena: " + gMap.getName());
                    i++;
                }
            }
        };

        SkyWarsReloaded.getIC().create("arenasmenu", invs, event -> {
            Player player = event.getPlayer();
            String name = event.getName();
            if (name.equalsIgnoreCase(SkyWarsReloaded.getNMS().getItemName(SkyWarsReloaded.getIM().getItem("exitMenuItem")))) {
                player.closeInventory();
                return;
            }

            if (!player.hasPermission("sw.map.arenas")) {
                return;
            }

            if (SkyWarsReloaded.getNMS().getVersion() < 13) {
                if (event.getClick().equals(ClickType.LEFT) && event.getItem().getType().equals(Material.valueOf("WOOL"))) {
                    attemptUpdate(name, player);
                }
            } else {
                if (event.getClick().equals(ClickType.LEFT) && (event.getItem().getType().equals(Material.valueOf("GREEN_WOOL")) || event.getItem().getType().equals(Material.valueOf("RED_WOOL")))) {
                    attemptUpdate(name, player);
                }
            }
        });


        SkyWarsReloaded.getIC().getMenu("arenasmenu").setUpdate(update);
    }

    private void attemptUpdate(String name, Player player) {
        GameMap gMap = GameMap.getMap(name);
        if (gMap != null) {
            SkyWarsReloaded.getIC().show(player, gMap.getArenaKey());
            gMap.updateArenaManager();
        }
    }

}