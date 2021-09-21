package net.gcnt.skywarsreloaded.data;

import java.util.UUID;

public abstract class SWPlayer {

    private boolean online;
    private final UUID uuid;

    private int soloWins;
    private int soloKills;
    private int soloGames;
    private int teamWins;
    private int teamKills;
    private int teamGames;

    private String selectedSoloCage;
    private String selectedTeamCage;
    private String selectedParticle;
    private String selectedKillEffect;
    private String selectedWinEffect;
    private String selectedProjectileEffect;

    public SWPlayer(UUID uuid, boolean online) {
        this.uuid = uuid;
        this.online = online;

        this.selectedSoloCage
                = this.selectedParticle
                = this.selectedTeamCage
                = this.selectedKillEffect
                = this.selectedWinEffect
                = this.selectedProjectileEffect
                = null;
    }


    public UUID getUuid() {
        return uuid;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public boolean isOnline() {
        return online;
    }
}
