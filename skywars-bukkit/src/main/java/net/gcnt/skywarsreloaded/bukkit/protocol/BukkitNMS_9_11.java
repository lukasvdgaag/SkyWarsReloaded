package net.gcnt.skywarsreloaded.bukkit.protocol;

import net.gcnt.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import org.bukkit.block.Biome;

public class BukkitNMS_9_11 extends BukkitNMS_8 {

    public BukkitNMS_9_11(BukkitSkyWarsReloaded plugin, String serverPackage) {
        super(plugin, serverPackage);
    }

    @Override
    public void initVersionedAPI() {
        super.initVersionedAPI();

        // Versioned enums
        voidBiome = Biome.valueOf("VOID");
    }
}
