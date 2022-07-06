package net.gcnt.skywarsreloaded.party;

import java.util.UUID;

public class CoreSWParty implements SWParty {

    private final UUID leader;

    public CoreSWParty(UUID leaderIn) {
        this.leader = leaderIn;
    }

    @Override
    public UUID getLeaderUUID(UUID member) {
        return this.leader;
    }
}
