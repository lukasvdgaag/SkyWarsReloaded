package com.walrusone.skywarsreloaded.utilities;

import com.alessiodp.parties.api.Parties;
import com.alessiodp.parties.api.interfaces.PartiesAPI;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import com.walrusone.skywarsreloaded.events.SkyWarsJoinEvent;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.game.TeamCard;
import com.walrusone.skywarsreloaded.managers.MatchManager;
import com.walrusone.skywarsreloaded.managers.PlayerStat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.Objects;
import java.util.UUID;

public class PartiesAlessioDPHook implements Listener {
    private final PartiesAPI partiesAPI = Parties.getApi();

    public void SkyWarsJoinEvent(SkyWarsJoinEvent e) {
        if (e.getGame().getTeamSize() == 1) return;
        Player target = e.getPlayer();

        PartyPlayer pl = partiesAPI.getPartyPlayer(target.getUniqueId());
        UUID partyUniqueId = pl.getPartyId();
        if (partyUniqueId != null) {
            Party party = partiesAPI.getParty(partyUniqueId);
            if (!Objects.equals(party.getLeader(), target.getUniqueId())) {
                // player is not the owner
                Player owner = Bukkit.getPlayer(party.getLeader());
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

            for (UUID p : party.getMembers()) {
                Player player = Bukkit.getPlayer(p);
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
