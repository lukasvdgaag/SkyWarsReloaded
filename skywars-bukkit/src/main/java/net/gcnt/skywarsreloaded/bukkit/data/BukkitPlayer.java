package net.gcnt.skywarsreloaded.bukkit.data;

import net.gcnt.skywarsreloaded.data.SWPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BukkitPlayer extends SWPlayer {

    private Player player;

    public BukkitPlayer(UUID uuid, boolean online) {
        super(uuid, online);
        this.player = Bukkit.getPlayer(uuid);
    }

    public Player getPlayer() {
        return player;
    }

}
