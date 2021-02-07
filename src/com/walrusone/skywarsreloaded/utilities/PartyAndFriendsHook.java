package com.walrusone.skywarsreloaded.utilities;

import com.walrusone.skywarsreloaded.events.SkyWarsJoinEvent;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.game.TeamCard;
import com.walrusone.skywarsreloaded.managers.MatchManager;
import com.walrusone.skywarsreloaded.managers.PlayerStat;
import de.simonsator.partyandfriends.spigot.api.pafplayers.PAFPlayer;
import de.simonsator.partyandfriends.spigot.api.pafplayers.PAFPlayerManager;
import de.simonsator.partyandfriends.spigot.api.party.PartyManager;
import de.simonsator.partyandfriends.spigot.api.party.PlayerParty;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class PartyAndFriendsHook implements Listener {

    public void SkyWarsJoinEvent(SkyWarsJoinEvent e) {
        if (e.getGame().getTeamSize() == 1) return;
        Player target = e.getPlayer();

        PAFPlayer pl = PAFPlayerManager.getInstance().getPlayer(target.getUniqueId());
        PlayerParty party = PartyManager.getInstance().getParty(pl);
        if (party != null) {

            if (!party.getLeader().getUniqueId().equals(target.getUniqueId())) {
                // player is not the owner
                Player owner = Bukkit.getPlayer(party.getLeader().getUniqueId());
                GameMap map = MatchManager.get().getPlayerMap(owner);
                if (map != null) {
                    TeamCard tc = map.getTeamCard(owner);
                    if (tc != null) {
                        if (tc.getFullCount() != 0) {
                            tc.sendReservation(target, PlayerStat.getPlayerStats(target));
                            return;
                        }
                    }
                }
            }

            for (PAFPlayer p : party.getAllPlayers()) {
                Player player = Bukkit.getPlayer(p.getUniqueId());
                if (player != null) {
                    GameMap map = MatchManager.get().getPlayerMap(player);
                    if (map != null && map.getTeamCard(player) == null) {
                        int size = 0;
                        for (TeamCard tc : map.getTeamCards()) {
                            if (tc.getPlayersSize() <= size && tc.getFullCount() != 0) {
                                tc.sendReservation(player, PlayerStat.getPlayerStats(player));
                                break;
                            }
                            size = tc.getPlayersSize();
                        }
                    }
                }
            }

        }

    }

}
