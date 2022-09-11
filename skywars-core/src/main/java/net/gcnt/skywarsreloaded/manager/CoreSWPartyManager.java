package net.gcnt.skywarsreloaded.manager;

import net.gcnt.skywarsreloaded.party.CoreSWParty;
import net.gcnt.skywarsreloaded.party.SWParty;

import java.util.UUID;

public class CoreSWPartyManager implements SWPartyManager {

    public SWParty initParty(UUID uuid) {
        return new CoreSWParty(uuid);
    }

}
