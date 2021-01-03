package com.walrusone.skywarsreloaded.listeners;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.managers.PlayerStat;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerJoinListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(final PlayerJoinEvent a1) {
        if (SkyWarsReloaded.get().getUpdater().getUpdateStatus() == 1 && (a1.getPlayer().isOp() || a1.getPlayer().hasPermission("sw.admin"))) {
            BaseComponent base = new TextComponent("§d§l[SkyWarsReloaded] §aA new update has been found: §b" + SkyWarsReloaded.get().getUpdater().getLatestVersion() + "§a. Click here to update!");
            base.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, SkyWarsReloaded.get().getUpdater().getUpdateURL()));
            base.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent("§7Click here to update to the latest version!")}));
            SkyWarsReloaded.getNMS().sendJSON(a1.getPlayer(), "[\"\",{\"text\":\"§d§l[SkyWarsReloaded] §aA new update has been found: §b" + SkyWarsReloaded.get().getUpdater().getLatestVersion() + "§a. Click here to update!\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + SkyWarsReloaded.get().getUpdater().getUpdateURL() + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"§7Click here to update to the latest version!\"}]}}}]");
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if (SkyWarsReloaded.getCfg().getSpawn() != null && SkyWarsReloaded.getCfg().teleportOnJoin()) {
                    a1.getPlayer().teleport(SkyWarsReloaded.getCfg().getSpawn());
                }
            }
        }.runTaskLater(SkyWarsReloaded.get(), 1);

        if (SkyWarsReloaded.getCfg().promptForResource()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    a1.getPlayer().setResourcePack(SkyWarsReloaded.getCfg().getResourceLink());
                }
            }.runTaskLater(SkyWarsReloaded.get(), 20);
        }

        if (PlayerStat.getPlayerStats(a1.getPlayer()) != null) {
            PlayerStat.removePlayer(a1.getPlayer().getUniqueId().toString());
        }
        //new BukkitRunnable() {
        // @Override
        //public void run() {
        if (!SkyWarsReloaded.getCfg().bungeeMode()) {
            for (GameMap gMap : GameMap.getMaps()) {
                if (gMap.getCurrentWorld() != null && gMap.getCurrentWorld().equals(a1.getPlayer().getWorld())) {
                    if (SkyWarsReloaded.getCfg().getSpawn() != null) {
                        a1.getPlayer().teleport(SkyWarsReloaded.getCfg().getSpawn());
                    }
                }
            }
        }
        //    }
        // }.runTaskLater(SkyWarsReloaded.get(), 1);

        PlayerStat player = new PlayerStat(a1.getPlayer());
        PlayerStat.getPlayers().add(player);
    }
}