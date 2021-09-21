package net.gcnt.skywarsreloaded.bukkit.wrapper;

import net.gcnt.skywarsreloaded.wrapper.AbstractSWPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BukkitSWPlayer extends AbstractSWPlayer {

    private final Player player;

    public BukkitSWPlayer(Player playerIn, boolean online) {
        super(playerIn.getUniqueId(), online);
        this.player = playerIn;
    }

    public BukkitSWPlayer(UUID uuid, boolean online) {
        this(Bukkit.getPlayer(uuid), online);
    }

    public Player getPlayer() {
        return player;
    }
}
