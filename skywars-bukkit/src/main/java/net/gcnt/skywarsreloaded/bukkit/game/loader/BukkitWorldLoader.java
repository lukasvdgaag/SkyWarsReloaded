package net.gcnt.skywarsreloaded.bukkit.game.loader;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.loader.AbstractWorldLoader;
import org.bukkit.block.Biome;

public abstract class BukkitWorldLoader extends AbstractWorldLoader {

    protected Biome voidBiome;

    public BukkitWorldLoader(SkyWarsReloaded plugin) {
        super(plugin);

        try {
            voidBiome = Biome.valueOf("THE_VOID");
        } catch (Exception e) {
            voidBiome = Biome.valueOf("VOID");
        }

    }
}
