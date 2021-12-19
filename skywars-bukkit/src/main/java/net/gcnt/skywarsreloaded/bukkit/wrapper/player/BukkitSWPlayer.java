package net.gcnt.skywarsreloaded.bukkit.wrapper.player;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.utils.BukkitItem;
import net.gcnt.skywarsreloaded.bukkit.wrapper.world.BukkitSWWorld;
import net.gcnt.skywarsreloaded.utils.CoreSWCoord;
import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.wrapper.player.AbstractSWPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
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

    @Override
    public SWCoord getLocation() {
        final Location location = player.getLocation();
        return new CoreSWCoord(new BukkitSWWorld(plugin, location.getWorld()), location.getBlockX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    @Override
    public void teleport(SWCoord coord) {
        teleport(coord.world().getName(), coord.xPrecise(), coord.yPrecise(), coord.zPrecise(), coord.yaw(), coord.pitch());
    }

    @Override
    public void teleport(String world, double x, double y, double z) {
        World bworld = Bukkit.getWorld(world);
        if (bworld == null) return;
        player.teleport(new Location(bworld, x, y, z));
    }

    @Override
    public void teleport(String world, double x, double y, double z, float yaw, float pitch) {
        World bworld = Bukkit.getWorld(world);
        if (bworld == null) return;
        player.teleport(new Location(bworld, x, y, z, yaw, pitch));
    }

    @Override
    public void teleport(SWCoord coord) {
        this.teleport(coord.world().getName(), coord.x(), coord.y(), coord.z());
    }

    @Override
    public void sendTitle(String title, String subtitle) {
        sendTitle(title, subtitle, 20, 50, 20);
    }

    @Override
    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        // todo check for older versions that do not support the fade in, stay, and fade out.
        player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }
}
