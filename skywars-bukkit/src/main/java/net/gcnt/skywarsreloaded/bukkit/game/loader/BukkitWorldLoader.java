package net.gcnt.skywarsreloaded.bukkit.game.loader;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.game.BukkitLocalGameInstance;
import net.gcnt.skywarsreloaded.game.gameinstance.LocalGameInstance;
import net.gcnt.skywarsreloaded.game.loader.AbstractWorldLoader;
import org.bukkit.World;

public abstract class BukkitWorldLoader extends AbstractWorldLoader {

    public BukkitWorldLoader(SkyWarsReloaded plugin) {
        super(plugin);
    }

    @Override
    public void updateWorldBorder(LocalGameInstance gameWorld) {
        World world = ((BukkitLocalGameInstance) gameWorld).getBukkitWorld();
        if (world == null) return;

        world.getWorldBorder().setCenter(0, 0);
        world.getWorldBorder().setSize(gameWorld.getTemplate().getBorderRadius());
    }
}
