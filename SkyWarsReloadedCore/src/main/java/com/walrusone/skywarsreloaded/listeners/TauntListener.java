package com.walrusone.skywarsreloaded.listeners;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.managers.MatchManager;
import com.walrusone.skywarsreloaded.managers.PlayerStat;
import com.walrusone.skywarsreloaded.menus.playeroptions.TauntOption;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.HashMap;

public class TauntListener implements org.bukkit.event.Listener {
    private final HashMap<String, Long> lastHandSwap = new HashMap<>();
    private final HashMap<String, Long> lastTaunt = new HashMap<>();

    public TauntListener() {
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void pressedTauntKey(PlayerToggleSneakEvent e) {
        Player player = e.getPlayer();
        GameMap gameMap = MatchManager.get().getPlayerMap(player);
        if (gameMap == null || MatchManager.get().getSpectatorMap(player) != null) {
            return;
        }
        String uuid = e.getPlayer().getUniqueId().toString();
        if (!player.isSneaking()) {
            if (lastHandSwap.containsKey(uuid)) {
                if (System.currentTimeMillis() - ((Long)lastHandSwap.get(uuid)) < 500L) {
                    if (lastTaunt.containsKey(uuid)) {
                        if (System.currentTimeMillis() - ((Long) lastTaunt.get(uuid)) < SkyWarsReloaded.getCfg().getCooldown() * 1000L) {
                            int cooldown = (int) ((SkyWarsReloaded.getCfg().getCooldown() * 1000 - (System.currentTimeMillis() - ((Long) lastTaunt.get(uuid)))) / 1000L);
                            int seconds = cooldown % 60 + 1;
                            int minutes = (cooldown - (seconds - 1)) / 60;
                            String cooldownText = "";
                            if (minutes > 0) {
                                cooldownText = cooldownText + minutes + " Minutes ";
                            }
                            if (seconds > 0) {
                                cooldownText = cooldownText + seconds + " Seconds";
                            }

                            e.getPlayer().sendMessage(new Messaging.MessageFormatter().setVariable("timeleft", cooldownText).format("error.cooldown"));
                            return;
                        }
                        lastTaunt.remove(uuid);
                    }

                    PlayerStat ps = PlayerStat.getPlayerStats(e.getPlayer());
                    if (ps != null) {
                        String tauntName = ps.getTaunt();
                        TauntOption taunt = (TauntOption) TauntOption.getPlayerOptionByKey(tauntName);
                        if ((taunt != null) &&
                                (!taunt.getKey().equals("none"))) {
                            taunt.performTaunt(e.getPlayer());
                        }

                        lastHandSwap.remove(uuid);
                        lastTaunt.put(uuid, System.currentTimeMillis());
                    }
                } else {
                    lastHandSwap.put(uuid, System.currentTimeMillis());
                }
            } else {
                lastHandSwap.put(uuid, System.currentTimeMillis());
            }
        }
    }
}
