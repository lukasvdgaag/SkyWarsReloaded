package com.walrusone.skywarsreloaded.listeners;

import com.google.common.collect.Maps;
import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.menus.IconMenu;
import com.walrusone.skywarsreloaded.menus.IconMenu.OptionClickEventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Map;

public class IconMenuController
        implements Listener {
    private final Map<Player, IconMenu> menu = Maps.newHashMap();
    private final Map<String, IconMenu> persistantMenus = Maps.newHashMap();

    public IconMenuController() {
    }

    public void create(Player player, ArrayList<Inventory> invs, OptionClickEventHandler optionClickEventHandler) {
        if (player != null) {
            menu.put(player, new IconMenu(invs, optionClickEventHandler));
        }
    }

    public void create(String key, ArrayList<Inventory> invs, OptionClickEventHandler optionClickEventHandler) {
        if (key != null) {
            persistantMenus.put(key, new IconMenu(invs, optionClickEventHandler));
        }
    }

    public IconMenu getMenu(String string) {
        return (IconMenu) persistantMenus.get(string);
    }

    public boolean hasViewers(String key) {
        if (persistantMenus.get(key) != null) {
            for (Inventory inv : ((IconMenu) persistantMenus.get(key)).getInventories()) {
                if (!inv.getViewers().isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    public void show(Player player, @Nullable String key) {
        if (key != null) {
            if (persistantMenus.containsKey(key)) {
                ((IconMenu) persistantMenus.get(key)).openInventory(player, 0);
            }
        } else if (menu.containsKey(player)) {
            ((IconMenu) menu.get(player)).openInventory(player, 0);
        }
    }

    private void destroy(Player key) {
        menu.remove(key);
    }

    public boolean has(Player player) {
        return menu.containsKey(player);
    }

    public boolean has(String key) {
        return persistantMenus.containsKey(key);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (((event.getWhoClicked() instanceof Player)) && (this.menu.containsKey(event.getWhoClicked()))) {
            ((IconMenu) this.menu.get(event.getWhoClicked())).onInventoryClick(event);
        }
        for (IconMenu menu : persistantMenus.values()) {
            if (menu.getInventories().contains(event.getInventory())) {
                menu.onInventoryClick(event);
                break;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryClose(final InventoryCloseEvent event) {
        if (((event.getPlayer() instanceof Player)) && (menu.containsKey(event.getPlayer()))) {


            new BukkitRunnable() {
                public void run() {
                    if (((IconMenu) menu.get(event.getPlayer())).getInventories().contains(event.getPlayer().getOpenInventory())) {
                        IconMenuController.this.destroy((Player) event.getPlayer());
                    }
                }
            }.runTaskLater(SkyWarsReloaded.get(), 5L);
        }
    }
}
