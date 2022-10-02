package net.gcnt.skywarsreloaded.wrapper.entity;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.data.player.SWPlayerData;
import net.gcnt.skywarsreloaded.game.gameinstance.GameInstance;
import net.gcnt.skywarsreloaded.party.SWParty;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractSWPlayer extends AbstractSWEntity implements SWPlayer {

    public final SkyWarsReloaded plugin;

    private final AtomicBoolean online;
    private final AtomicBoolean frozen;

    private SWPlayerData playerData;
    private GameInstance gameWorld;
    private SWParty party;

    public AbstractSWPlayer(SkyWarsReloaded plugin, UUID uuid, boolean online) {
        super(uuid);
        this.plugin = plugin;
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
    public SWPlayerData getPlayerData() {
        return this.playerData;
    }

    @Override
    public void setPlayerData(SWPlayerData playerData) {
        this.playerData = playerData;
    }

    @Override
    public GameInstance getGameWorld() {
        return this.gameWorld;
    }

    @Override
    public void setGameWorld(GameInstance world) {
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

    @Override
    public SkyWarsReloaded getPlugin() {
        return plugin;
    }
}
