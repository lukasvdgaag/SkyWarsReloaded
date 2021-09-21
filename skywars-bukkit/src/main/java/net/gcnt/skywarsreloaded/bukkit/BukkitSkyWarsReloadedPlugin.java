package net.gcnt.skywarsreloaded.bukkit;

import org.bukkit.plugin.java.JavaPlugin;

public class BukkitSkyWarsReloadedPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        new BukkitSkyWarsReloaded().onEnable();
    }
}
