package net.gcnt.skywarsreloaded.bukkit.wrapper.world;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;
import net.gcnt.skywarsreloaded.wrapper.world.AbstractSWWorld;
import org.bukkit.World;

import java.util.List;
import java.util.stream.Collectors;

public class BukkitSWWorld extends AbstractSWWorld {

    private final SkyWarsReloaded plugin;
    private final World bukkitWorld;

    public BukkitSWWorld(SkyWarsReloaded pluginIn, World worldIn) {
        this.plugin = pluginIn;
        this.bukkitWorld = worldIn;
    }

    @Override
    public List<SWPlayer> getAllPlayers() {
        return this.bukkitWorld.getPlayers().stream().map(
                (bPlayer) -> this.plugin.getPlayerManager().getPlayerByUUID(bPlayer.getUniqueId())
        ).collect(Collectors.toList());
    }
}
