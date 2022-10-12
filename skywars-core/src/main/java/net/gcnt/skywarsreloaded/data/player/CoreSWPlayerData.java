package net.gcnt.skywarsreloaded.data.player;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.data.player.stats.SWPlayerData;
import net.gcnt.skywarsreloaded.data.player.stats.SWPlayerStats;
import net.gcnt.skywarsreloaded.data.player.stats.SWPlayerUnlockables;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;

/**
 * Stats and player cosmetics for a currently connected player
 */
public class CoreSWPlayerData implements SWPlayerData {

    private final SkyWarsReloaded plugin;
    private final SWPlayer player;
    private boolean initialized;

    private SWPlayerStats stats;
    private SWPlayerUnlockables unlockables;

    private String selectedSoloCage;
    private String selectedTeamCage;
    private String selectedParticle;
    private String selectedKillEffect;
    private String selectedWinEffect;
    private String selectedProjectileEffect;
    private String killMessagesTheme;
    private String selectedKit;

    public CoreSWPlayerData(SkyWarsReloaded plugin, SWPlayer player) {
        this.plugin = plugin;
        this.player = player;
        this.initialized = false;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void initData(SWPlayerStats statsIn,
                         SWPlayerUnlockables unlockablesIn,
                         String selectedSoloCageIn,
                         String selectedTeamCageIn,
                         String selectedParticleIn,
                         String selectedKillEffectIn,
                         String selectedWinEffectIn,
                         String selectedProjectileEffectIn,
                         String killMessagesThemeIn,
                         String selectedKit) {
        this.stats = statsIn;
        this.unlockables = unlockablesIn;
        this.selectedSoloCage = selectedSoloCageIn;
        this.selectedTeamCage = selectedTeamCageIn;
        this.selectedParticle = selectedParticleIn;
        this.selectedKillEffect = selectedKillEffectIn;
        this.selectedWinEffect = selectedWinEffectIn;
        this.selectedProjectileEffect = selectedProjectileEffectIn;
        this.killMessagesTheme = killMessagesThemeIn;
        this.selectedKit = selectedKit;
        this.initialized = true;
    }

    @Override
    public SWPlayerStats getStats() {
        return stats;
    }

    @Override
    public SWPlayerUnlockables getUnlockables() {
        return unlockables;
    }

    @Override
    public String getSoloCage() {
        return selectedSoloCage;
    }

    @Override
    public void setSoloCage(String value) {
        selectedSoloCage = value;
        plugin.getPlayerStorage().saveData(player);
    }

    @Override
    public String getTeamCage() {
        return selectedTeamCage;
    }

    @Override
    public void setTeamCage(String value) {
        selectedTeamCage = value;
        plugin.getPlayerStorage().saveData(player);
    }

    @Override
    public String getParticle() {
        return selectedParticle;
    }

    @Override
    public void setParticle(String value) {
        selectedParticle = value;
        plugin.getPlayerStorage().saveData(player);
    }

    @Override
    public String getKillEffect() {
        return selectedKillEffect;
    }

    @Override
    public void setKillEffect(String value) {
        selectedKillEffect = value;
        plugin.getPlayerStorage().saveData(player);
    }

    @Override
    public String getWinEffect() {
        return selectedWinEffect;
    }

    @Override
    public void setWinEffect(String value) {
        selectedWinEffect = value;
        plugin.getPlayerStorage().saveData(player);
    }

    @Override
    public String getProjectileParticle() {
        return selectedProjectileEffect;
    }

    @Override
    public void setProjectileParticle(String value) {
        selectedProjectileEffect = value;
        plugin.getPlayerStorage().saveData(player);
    }

    @Override
    public String getKillMessagesTheme() {
        return killMessagesTheme;
    }

    @Override
    public void setKillMessagesTheme(String value) {
        killMessagesTheme = value;
        plugin.getPlayerStorage().saveData(player);
    }

    @Override
    public String getKit() {
        return this.selectedKit;
    }

    @Override
    public void setKit(String kit) {
        this.selectedKit = kit;
        plugin.getPlayerStorage().saveData(player);
    }
}
