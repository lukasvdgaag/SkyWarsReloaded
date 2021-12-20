package net.gcnt.skywarsreloaded.bukkit.game.loader;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.game.BukkitGameWorld;
import net.gcnt.skywarsreloaded.game.GameWorld;
import net.gcnt.skywarsreloaded.game.loader.AbstractWorldLoader;
import org.bukkit.World;
import org.bukkit.block.Biome;

public abstract class BukkitWorldLoader extends AbstractWorldLoader {

    protected Biome voidBiome;

    public BukkitWorldLoader(SkyWarsReloaded plugin) {
        super(plugin);

        final int version = plugin.getUtils().getServerVersion();

        if (version >= 13) Biome.valueOf("THE_VOID");
        else if (version >= 9) voidBiome = Biome.valueOf("VOID");
        else voidBiome = Biome.valueOf("FOREST");
    }

    @Override
    public void updateWorldBorder(GameWorld gameWorld) {
        World world = ((BukkitGameWorld) gameWorld).getBukkitWorld();
        if (world == null) return;

        world.getWorldBorder().setCenter(0, 0);
        world.getWorldBorder().setSize(gameWorld.getTemplate().getBorderRadius());
    }
}
