package net.gcnt.skywarsreloaded.bukkit.game.kits;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.kits.AbstractKitManager;
import net.gcnt.skywarsreloaded.game.kits.SWKit;
import net.gcnt.skywarsreloaded.utils.properties.FolderProperties;

import java.io.File;

public class BukkitKitManager extends AbstractKitManager {

    public BukkitKitManager(SkyWarsReloaded plugin) {
        super(plugin);
    }

    @Override
    public SWKit initKit(String id) {
        return new BukkitSWKit(plugin, id);
    }
}