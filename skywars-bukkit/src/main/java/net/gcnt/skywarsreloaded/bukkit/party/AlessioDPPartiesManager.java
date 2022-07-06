package net.gcnt.skywarsreloaded.bukkit.party;

import com.alessiodp.parties.api.interfaces.PartiesAPI;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import net.gcnt.skywarsreloaded.party.SWParty;

import java.util.UUID;

public class AlessioDPPartiesManager implements SWParty {

    //Support for Parties by AlessioDP
    PartiesAPI api = com.alessiodp.parties.api.Parties.getApi();
    @Override
    public UUID getLeaderUUID(UUID member) {
        PartyPlayer pp = api.getPartyPlayer(member);
        if (pp == null) return null;
        if (pp.getPartyId() == null) return null;
        com.alessiodp.parties.api.interfaces.Party party = api.getParty(pp.getPartyId());
        if (party == null) return null;
        if (party.getLeader() == null) return null;
        return party.getLeader();
    }
}
