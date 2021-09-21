package net.gcnt.skywarsreloaded.wrapper;

import net.gcnt.skywarsreloaded.data.player.AbstractSWPlayerData;

import java.util.UUID;

public abstract class AbstractSWPlayer implements SWPlayer {

    private final UUID uuid;
    private boolean online;

    // Stats
    private int soloWins;
    private int soloKills;
    private int soloGames;
    private int teamWins;
    private int teamKills;
    private int teamGames;

    // Cosmetics
    private String selectedSoloCage;
    private String selectedTeamCage;
    private String selectedParticle;
    private String selectedKillEffect;
    private String selectedWinEffect;
    private String selectedProjectileEffect;

    public AbstractSWPlayer(UUID uuid, boolean online) {
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

    public void insertData(int soloWins, int soloKills, int soloGames, int teamWins, int teamKills, int teamGames, String selectedSoloCage, String selectedTeamCage,
                           String selectedParticle, String selectedKillEffect, String selectedWinEffect, String selectedProjectileEffect) {
        this.soloWins = soloWins;
        this.soloKills = soloKills;
        this.soloGames = soloGames;
        this.teamWins = teamWins;
        this.teamKills = teamKills;
        this.teamGames = teamGames;
        this.selectedSoloCage = selectedSoloCage;
        this.selectedTeamCage = selectedTeamCage;
        this.selectedParticle = selectedParticle;
        this.selectedKillEffect = selectedKillEffect;
        this.selectedWinEffect = selectedWinEffect;
        this.selectedProjectileEffect = selectedProjectileEffect;
    }

    /*
    Get methods
     */

    public UUID getUuid() {
        return uuid;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public int getSoloWins() {
        return soloWins;
    }

    public int getSoloKills() {
        return soloKills;
    }

    public int getSoloGames() {
        return soloGames;
    }

    public int getTeamWins() {
        return teamWins;
    }

    public int getTeamKills() {
        return teamKills;
    }

    public int getTeamGames() {
        return teamGames;
    }

    public String getSelectedSoloCage() {
        return selectedSoloCage;
    }

    public String getSelectedTeamCage() {
        return selectedTeamCage;
    }

    public String getSelectedParticle() {
        return selectedParticle;
    }

    public String getSelectedKillEffect() {
        return selectedKillEffect;
    }

    public String getSelectedWinEffect() {
        return selectedWinEffect;
    }

    public String getSelectedProjectileEffect() {
        return selectedProjectileEffect;
    }

    @Override
    public AbstractSWPlayerData getPlayerData() {
        return null;
    }
}
