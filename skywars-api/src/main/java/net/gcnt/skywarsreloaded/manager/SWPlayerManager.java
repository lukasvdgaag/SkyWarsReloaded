package net.gcnt.skywarsreloaded.manager;

import net.gcnt.skywarsreloaded.wrapper.player.SWEntity;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;
import net.gcnt.skywarsreloaded.wrapper.world.SWWorld;

import java.util.List;
import java.util.UUID;

public interface SWPlayerManager {

    SWEntity getEntityFromUUID(UUID uuid);

    SWPlayer getPlayerByUUID(UUID uuid);

    SWPlayer initPlayer(UUID uuid);

    List<SWPlayer> getPlayers();

    void removePlayer(SWPlayer player);

    void initAllPlayers();
}
