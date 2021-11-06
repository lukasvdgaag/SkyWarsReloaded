package net.gcnt.skywarsreloaded.manager;

import net.gcnt.skywarsreloaded.wrapper.SWPlayer;

import java.util.UUID;

public interface SWPlayerManager {

    SWPlayer getPlayerByUUID(UUID uuid);

}
