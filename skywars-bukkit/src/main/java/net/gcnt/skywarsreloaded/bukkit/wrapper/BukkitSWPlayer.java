package net.gcnt.skywarsreloaded.bukkit.wrapper;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.utils.BukkitItem;
import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.wrapper.AbstractSWPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class BukkitSWPlayer extends AbstractSWPlayer {

    private final Player player;

    public BukkitSWPlayer(SkyWarsReloaded plugin, Player playerIn, boolean online) {
        super(plugin, playerIn.getUniqueId(), online);
        this.player = playerIn;
    }

    public BukkitSWPlayer(SkyWarsReloaded plugin, UUID uuid, boolean online) {
        this(plugin, Bukkit.getPlayer(uuid), online);
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
        ItemStack item;
        if (this.plugin.getUtils().getServerVersion() >= 9) {
            if (offHand) item = player.getInventory().getItemInOffHand();
            else item = player.getInventory().getItemInMainHand();
        } else item = player.getInventory().getItemInHand();
        if (item == null || item.getType() == Material.AIR) return null;
        return BukkitItem.fromBukkit(plugin, item);
    }

    @Override
    public Item[] getInventory() {
        final ItemStack[] contents = player.getInventory().getContents();
        Item[] items = new Item[contents.length];
        for (int i = 0; i < 36; i++) {
            ItemStack item = contents[i];
            items[i] = BukkitItem.fromBukkit(plugin, item == null || item.getType() == Material.AIR ? null : item);
        }
        return items;
    }

    @Override
    public void setSlot(int slot, Item item) {
        player.getInventory().setItem(slot, ((BukkitItem) item).getBukkitItem());
    }

    @Override
    public Item getSlot(int slot) {
        return BukkitItem.fromBukkit(plugin, player.getInventory().getItem(slot));
    }

    @Override
    public Item getHelmet() {
        return BukkitItem.fromBukkit(plugin, player.getInventory().getHelmet());
    }

    @Override
    public void setHelmet(Item helmet) {
        player.getInventory().setHelmet(((BukkitItem) helmet).getBukkitItem());
    }

    @Override
    public Item getChestplate() {
        return BukkitItem.fromBukkit(plugin, player.getInventory().getChestplate());
    }

    @Override
    public void setChestplate(Item chestplate) {
        player.getInventory().setHelmet(((BukkitItem) chestplate).getBukkitItem());
    }

    @Override
    public Item getLeggings() {
        return BukkitItem.fromBukkit(plugin, player.getInventory().getLeggings());
    }

    @Override
    public void setLeggings(Item leggings) {
        player.getInventory().setHelmet(((BukkitItem) leggings).getBukkitItem());
    }

    @Override
    public Item getBoots() {
        return BukkitItem.fromBukkit(plugin, player.getInventory().getBoots());
    }

    @Override
    public void setBoots(Item boots) {
        player.getInventory().setHelmet(((BukkitItem) boots).getBukkitItem());
    }

    @Override
    public void clearInventory() {
        player.getInventory().clear();
    }
}
