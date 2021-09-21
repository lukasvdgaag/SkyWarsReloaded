package net.gcnt.skywarsreloaded.bukkit;

import net.gcnt.skywarsreloaded.AbstractSkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.data.config.BukkitYAMLManager;
import net.gcnt.skywarsreloaded.bukkit.data.player.BukkitSWPlayerDataManager;
import net.gcnt.skywarsreloaded.data.player.SWPlayerDataManager;

import java.io.File;
import java.util.logging.Logger;

public class BukkitSkyWarsReloaded extends AbstractSkyWarsReloaded {

    @Override
    public void onEnable() {
        super.onEnable();
    }

    // Internal Utils

    @Override
    public BukkitYAMLManager createYAMLManager() {
        return new BukkitYAMLManager(this);
    }

    @Override
    public BukkitSWPlayerDataManager createPlayerDataManager() {
        return new BukkitSWPlayerDataManager(this);
    }

    // Getters

    @Override
    public File getDataFolder() {
        return null;
    }

    @Override
    public SWPlayerDataManager getPlayerDataManager() {
        return null;
    }

    @Override
    public Logger getLogger() {
        return null;
    }

    @Override
    public void disableSkyWars() {

    }
}
