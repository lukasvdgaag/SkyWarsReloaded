package net.gcnt.skywarsreloaded.bukkit;

import org.bukkit.plugin.java.JavaPlugin;

public class BukkitSkyWarsReloadedPlugin extends JavaPlugin {

    /**
     * Do not put anything in this class other than the onEnable / onDisable methods.
     * Use {@link BukkitSkyWarsReloaded} instead.
     */

    @Override
    public void onEnable() {
        new BukkitSkyWarsReloaded().onEnable();
    }
}
