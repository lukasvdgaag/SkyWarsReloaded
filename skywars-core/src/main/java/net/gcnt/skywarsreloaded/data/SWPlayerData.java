package net.gcnt.skywarsreloaded.data;

public abstract class SWPlayerData {

    private SWPlayer player;
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

    public SWPlayerData(SWPlayer player) {
        this.player = player;
        this.selectedSoloCage = this.selectedParticle = this.selectedTeamCage = this.selectedKillEffect = this.selectedWinEffect = this.selectedProjectileEffect = null;
    }

    /**
     * Loads all data from storage (MySQL/MongoDB/YAML) into cache.
     */
    public abstract void loadData();

    /**
     * Saves all cache data to storage (MySQL/MongoDB/YAML).
     */
    public abstract void saveData();

    public SWPlayer getPlayer() {
        return player;
    }

    public void setPlayer(SWPlayer player) {
        this.player = player;
    }

    public int getSoloWins() {
        return soloWins;
    }

    public void setSoloWins(int soloWins) {
        this.soloWins = soloWins;
    }

    public int getSoloKills() {
        return soloKills;
    }

    public void setSoloKills(int soloKills) {
        this.soloKills = soloKills;
    }

    public int getSoloGames() {
        return soloGames;
    }

    public void setSoloGames(int soloGames) {
        this.soloGames = soloGames;
    }

    public int getTeamWins() {
        return teamWins;
    }

    public void setTeamWins(int teamWins) {
        this.teamWins = teamWins;
    }

    public int getTeamKills() {
        return teamKills;
    }

    public void setTeamKills(int teamKills) {
        this.teamKills = teamKills;
    }

    public int getTeamGames() {
        return teamGames;
    }

    public void setTeamGames(int teamGames) {
        this.teamGames = teamGames;
    }

    public void setSelectedSoloCage(String selectedSoloCage) {
        this.selectedSoloCage = selectedSoloCage;
    }

    public void setSelectedTeamCage(String selectedTeamCage) {
        this.selectedTeamCage = selectedTeamCage;
    }

    public void setSelectedParticle(String selectedParticle) {
        this.selectedParticle = selectedParticle;
    }

    public void setSelectedKillEffect(String selectedKillEffect) {
        this.selectedKillEffect = selectedKillEffect;
    }

    public void setSelectedWinEffect(String selectedWinEffect) {
        this.selectedWinEffect = selectedWinEffect;
    }

    public void setSelectedProjectileEffect(String selectedProjectileEffect) {
        this.selectedProjectileEffect = selectedProjectileEffect;
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
}
