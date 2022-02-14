package net.gcnt.skywarsreloaded.party;

import java.util.UUID;

public class CoreSWPartyManager implements SWPartyManager {

    public SWParty initParty(UUID uuid) {
        return new CoreSWParty(uuid);
    }

}
