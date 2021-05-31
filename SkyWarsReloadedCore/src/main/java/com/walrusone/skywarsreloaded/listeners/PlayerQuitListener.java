package com.walrusone.skywarsreloaded.listeners;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.enums.PlayerRemoveReason;
import com.walrusone.skywarsreloaded.managers.PlayerStat;
import com.walrusone.skywarsreloaded.utilities.Party;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerQuitListener implements org.bukkit.event.Listener {
    public PlayerQuitListener() {
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();
        Party party = Party.getParty(player);
        if (party != null) {
            party.removeMember(player);
        }

        if (SkyWarsReloaded.get().getMatchManager().getPlayerMap(player) != null) {
            SkyWarsReloaded.get().getPlayerManager().removePlayer(
                    player, PlayerRemoveReason.PLAYER_QUIT_SERVER, null, true);
        }

        PlayerStat pStats = PlayerStat.getPlayerStats(uuid);
        if (pStats != null) {
            pStats.saveStats(() -> {
                PlayerStat.removePlayer(uuid.toString());
            });
        }
        PlayerStat.resetScoreboard(player);
    }
}
