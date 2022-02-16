package net.gcnt.skywarsreloaded.bukkit.managers;

import net.gcnt.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.wrapper.player.BukkitSWPlayer;
import net.gcnt.skywarsreloaded.manager.AbstractPlayerManager;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class BukkitPlayerManager extends AbstractPlayerManager {

    public BukkitPlayerManager(BukkitSkyWarsReloaded plugin) {
        super(plugin);
    }

    @Override
    public SWPlayer createSWPlayerForPlatform(UUID uuid) {
        System.out.println(uuid.toString());
        Player player = Bukkit.getPlayer(uuid);
        System.out.println(player);
        if (player == null) return new BukkitSWPlayer((BukkitSkyWarsReloaded) plugin, uuid, true);
        else return this.createSWPlayerForPlatform(player);
    }

    public SWPlayer createSWPlayerForPlatform(Player player) {
        return new BukkitSWPlayer((BukkitSkyWarsReloaded) plugin, player, true);
    }

    @Override
    public void initAllPlayers() {
        final List<SWPlayer> players = this.getPlayers();
        for (Player player : Bukkit.getOnlinePlayers()) {
            players.add(createSWPlayerForPlatform(player));
        }
    }
}
