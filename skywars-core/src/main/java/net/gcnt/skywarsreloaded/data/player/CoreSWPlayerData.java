package net.gcnt.skywarsreloaded.data.player;

/**
 * Stats and player cosmetics for a currently connected player
 */
public class CoreSWPlayerData implements SWPlayerData {

    private boolean initialized;

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
    private String killMessagesTheme;

    public CoreSWPlayerData() {
        this.initialized = false;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void initData(int soloWinsIn,
                         int soloKillsIn,
                         int soloGamesIn,
                         int teamWinsIn,
                         int teamKillsIn,
                         int teamGamesIn,
                         String selectedSoloCageIn,
                         String selectedTeamCageIn,
                         String selectedParticleIn,
                         String selectedKillEffectIn,
                         String selectedWinEffectIn,
                         String selectedProjectileEffectIn,
                         String killMessagesThemeIn) {
        this.soloWins = soloWinsIn;
        this.soloKills = soloKillsIn;
        this.soloGames = soloGamesIn;
        this.teamWins = teamWinsIn;
        this.teamKills = teamKillsIn;
        this.teamGames = teamGamesIn;
        this.selectedSoloCage = selectedSoloCageIn;
        this.selectedTeamCage = selectedTeamCageIn;
        this.selectedParticle = selectedParticleIn;
        this.selectedKillEffect = selectedKillEffectIn;
        this.selectedWinEffect = selectedWinEffectIn;
        this.selectedProjectileEffect = selectedProjectileEffectIn;
        this.killMessagesTheme = killMessagesThemeIn;
    }

    @Override
    public int getSoloWins() {
        return soloWins;
    }

    @Override
    public void setSoloWins(int amount) {
        soloWins = amount;
    }

    @Override
    public int getSoloKills() {
        return soloKills;
    }

    @Override
    public void setSoloKills(int amount) {
        soloKills = amount;
    }

    @Override
    public int getSoloGamesPlayed() {
        return soloGames;
    }

    @Override
    public void setSoloGamesPlayed(int amount) {
        soloGames = amount;
    }

    @Override
    public int getTeamWins() {
        return teamWins;
    }

    @Override
    public void setTeamWins(int amount) {
        teamWins = amount;
    }

    @Override
    public int getTeamKills() {
        return teamKills;
    }

    @Override
    public void setTeamKills(int amount) {
        teamKills = amount;
    }

    @Override
    public int getTeamGamesPlayed() {
        return teamGames;
    }

    @Override
    public void setTeamGamesPlayed(int amount) {
        teamGames = amount;
    }

    @Override
    public String getSoloCage() {
        return selectedSoloCage;
    }

    @Override
    public void setSoloCage(String value) {
        selectedSoloCage = value;
    }

    @Override
    public String getTeamCage() {
        return selectedTeamCage;
    }

    @Override
    public void setTeamCage(String value) {
        selectedTeamCage = value;
    }

    @Override
    public String getParticle() {
        return selectedParticle;
    }

    @Override
    public void setParticle(String value) {
        selectedParticle = value;
    }

    @Override
    public String getKillEffect() {
        return selectedKillEffect;
    }

    @Override
    public void setKillEffect(String value) {
        selectedKillEffect = value;
    }

    @Override
    public String getWinEffect() {
        return selectedWinEffect;
    }

    @Override
    public void setWinEffect(String value) {
        selectedWinEffect = value;
    }

    @Override
    public String getProjectileParticle() {
        return selectedProjectileEffect;
    }

    @Override
    public void setProjectileParticle(String value) {
        selectedProjectileEffect = value;
    }

    @Override
    public String getKillMessagesTheme() {
        return killMessagesTheme;
    }

    @Override
    public void setKillMessagesTheme(String value) {
        killMessagesTheme = value;
    }
}
