package net.gcnt.skywarsreloaded.bukkit.party;

import de.simonsator.partyandfriends.spigot.api.pafplayers.PAFPlayer;
import de.simonsator.partyandfriends.spigot.api.pafplayers.PAFPlayerManager;
import de.simonsator.partyandfriends.spigot.api.party.PartyManager;
import de.simonsator.partyandfriends.spigot.api.party.PlayerParty;
import net.gcnt.skywarsreloaded.party.SWParty;

import java.util.UUID;

public class PAFBungeeManager implements SWParty {

    private PlayerParty getPAFParty(UUID p) {
        PAFPlayer pafPlayer = PAFPlayerManager.getInstance().getPlayer(p);
        return PartyManager.getInstance().getParty(pafPlayer);
    }

    @Override
    public UUID getLeaderUUID(UUID member) {
        return getPAFParty(member).getLeader().getUniqueId();
    }
}
