package net.gcnt.skywarsreloaded.manager;

import net.gcnt.skywarsreloaded.wrapper.SWPlayer;

import java.util.List;
import java.util.UUID;

public abstract class AbstractPlayerManager implements SWPlayerManager {

    public abstract SWPlayer getPlayerByUUID(UUID uuid);

    public abstract SWPlayer initPlayer(UUID uuid);

    public abstract List<SWPlayer> getPlayers();
}
