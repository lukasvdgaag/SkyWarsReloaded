package net.gcnt.skywarsreloaded.hook;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.milkbowl.vault.economy.Economy;

public abstract class AbstractSWVaultHook implements SWVaultHook {

    protected final SkyWarsReloaded plugin;
    protected Economy economy;
    private boolean enabled;

    public AbstractSWVaultHook(SkyWarsReloaded plugin) {
        this.plugin = plugin;
        this.enabled = false;
    }

    @Override
    public void enable() {
        if (!plugin.getServer().isPluginEnabled("Vault")) return;

        if (!setupEconomy()) return;
        plugin.getLogger().info("Vault hook has been enabled. You can now use economy features such as costs and purchasing unlockables.");
        this.setEnabled(true);
    }

    @Override
    public void disable() {

    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public abstract boolean setupEconomy();
}
