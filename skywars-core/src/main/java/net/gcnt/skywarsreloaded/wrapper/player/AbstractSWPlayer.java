package net.gcnt.skywarsreloaded.wrapper.player;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.data.player.SWPlayerData;
import net.gcnt.skywarsreloaded.game.GameWorld;
import net.gcnt.skywarsreloaded.party.SWParty;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractSWPlayer implements SWPlayer {

    public final SkyWarsReloaded plugin;
    private final UUID uuid;

    private final AtomicBoolean online;
    private final AtomicBoolean frozen;

    private SWPlayerData playerData;
    private GameWorld gameWorld;
    private SWParty party;

    public AbstractSWPlayer(SkyWarsReloaded plugin, UUID uuid, boolean online) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.online = new AtomicBoolean(online);
        this.gameWorld = null;
        this.frozen = new AtomicBoolean(false);
    }

    @Override
    public boolean isOnline() {
        return online.get();
    }

    @Override
    public void setOnline(boolean online) {
        this.online.set(online);
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public SWPlayerData getPlayerData() {
        return this.playerData;
    }

    @Override
    public void setPlayerData(SWPlayerData playerData) {
        this.playerData = playerData;
    }

    @Override
    public GameWorld getGameWorld() {
        return this.gameWorld;
    }

    @Override
    public void setGameWorld(GameWorld world) {
        this.gameWorld = world;
    }

    @Override
    public @Nullable SWParty getParty() {
        return this.party;
    }

    @Override
    public void setParty(@Nullable SWParty partyIn) {
        this.party = partyIn;
    }

    @Override
    public void freeze() {
        this.frozen.set(true);
    }

    @Override
    public void unfreeze() {
        this.frozen.set(false);
    }

    @Override
    public boolean isFrozen() {
        return this.frozen.get();
    }
}
