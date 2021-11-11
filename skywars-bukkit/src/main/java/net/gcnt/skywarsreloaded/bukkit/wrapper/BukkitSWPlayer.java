package net.gcnt.skywarsreloaded.bukkit.wrapper;

import net.gcnt.skywarsreloaded.utils.Item;
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

    @Override
    public void sendMessage(String message) {
        player.sendMessage(message);
    }

    @Override
    public boolean hasPermission(String permission) {
        return player.hasPermission(permission);
    }

    @Override
    public Item getItemInHand(boolean offHand) {
        return null;
    }

    @Override
    public Item[] getInventory() {
        return new Item[0];
    }

    @Override
    public void setSlot(int slot, Item item) {

    }

    @Override
    public Item getSlot(int slot) {
        return null;
    }
}
