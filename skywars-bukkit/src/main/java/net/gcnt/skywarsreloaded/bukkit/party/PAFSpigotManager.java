package net.gcnt.skywarsreloaded.bukkit.party;

import de.simonsator.partyandfriends.api.pafplayers.OnlinePAFPlayer;
import de.simonsator.partyandfriends.api.pafplayers.PAFPlayerManager;
import de.simonsator.partyandfriends.api.party.PartyManager;
import de.simonsator.partyandfriends.api.party.PlayerParty;
import net.gcnt.skywarsreloaded.party.SWParty;
import org.bukkit.Bukkit;

import java.util.UUID;

public class PAFSpigotManager implements SWParty {

    private PlayerParty getPAFParty(UUID p) {
        OnlinePAFPlayer pafPlayer = PAFPlayerManager.getInstance().getPlayer(Bukkit.getPlayer(p));
        return PartyManager.getInstance().getParty(pafPlayer);
    }

    @Override
    public UUID getLeaderUUID(UUID member) {
        return getPAFParty(member).getLeader().getUniqueId();
    }
}
