package com.walrusone.skywarsreloaded.menus.gameoptions;

import com.google.common.collect.Lists;
import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.events.SkyWarsSelectKitEvent;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.managers.MatchManager;
import com.walrusone.skywarsreloaded.menus.gameoptions.objects.GameKit;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class KitSelectionMenu {

    private static final String menuName = new Messaging.MessageFormatter().format("menu.kit-section-menu");
    private static int menuSize = SkyWarsReloaded.getCfg().getKitMenuSize();

    public KitSelectionMenu(final Player player) {
        GameMap gMap = MatchManager.get().getPlayerMap(player);
        List<GameKit> availableItems = GameKit.getAvailableKits();
        if (availableItems.size() > 0) {
            ArrayList<Inventory> invs = new ArrayList<>();

            for (GameKit kit : availableItems) {
                int pos = kit.getPosition();
                int page = kit.getPage() - 1;

                if (invs.isEmpty() || invs.size() < page + 1) {
                    while (invs.size() < page + 1) {
                        invs.add(Bukkit.createInventory(null, menuSize + 9, menuName));
                    }
                }
                List<String> loreList = Lists.newLinkedList();
                ItemStack item;
                boolean hasPermission = true;

                // If permission is required but player doesn't have the permission for that kit
                if (kit.needPermission() && !player.hasPermission("sw.kit." + kit.getFilename())) {
                    hasPermission = false;
                }

                // Select icon based on player allowed to use the kit or not
                if (hasPermission) {
                    item = kit.getIcon();
                    if (item == null) item = new ItemStack(Material.DIRT, 1);
                    loreList.addAll(kit.getColorLores());
                } else {
                    item = kit.getLIcon();
                    if (item == null) item = new ItemStack(Material.BARRIER, 1);
                    loreList.add(kit.getColoredLockedLore());
                }

                // Apply in gui
                ItemStack itemFinal = SkyWarsReloaded.getNMS().getItemStack(
                        item,
                        loreList,
                        ChatColor.translateAlternateColorCodes('&', kit.getName()));
                invs.get(page).setItem(pos, itemFinal);
            }

            if (gMap != null) {
                SkyWarsReloaded.getIC().create(player, invs, event -> {
                    String name = event.getName();
                    if (name.equalsIgnoreCase(SkyWarsReloaded.getNMS().getItemName(SkyWarsReloaded.getIM().getItem("exitMenuItem")))) {
                        player.closeInventory();
                        return;
                    }
                    GameKit kit = GameKit.getKit(name);
                    if (kit == null) {
                        return;
                    }

                    if (kit.needPermission()) {
                        if (!player.hasPermission("sw.kit." + kit.getFilename())) {
                            Util.get().playSound(player, player.getLocation(), SkyWarsReloaded.getCfg().getErrorSound(), 1, 1);
                            return;
                        }
                    }

                    player.closeInventory();
                    Util.get().playSound(player, player.getLocation(), SkyWarsReloaded.getCfg().getConfirmeSelctionSound(), 1, 1);
                    gMap.setKitVote(player, kit);
                    Bukkit.getPluginManager().callEvent(new SkyWarsSelectKitEvent(player, gMap, kit));
                    player.sendMessage(new Messaging.MessageFormatter().setVariable("kit", kit.getColorName()).format("game.select-kit"));
                });
            }
            if (player != null) {
                SkyWarsReloaded.getIC().show(player, null);
            }
        }
    }
}