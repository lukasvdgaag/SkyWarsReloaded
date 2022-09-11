package net.gcnt.skywarsreloaded.manager;

import net.gcnt.skywarsreloaded.party.SWParty;

import java.util.UUID;

public interface SWPartyManager {

    SWParty initParty(UUID leader);
}
