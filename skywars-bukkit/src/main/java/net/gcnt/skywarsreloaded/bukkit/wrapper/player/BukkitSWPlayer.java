package net.gcnt.skywarsreloaded.bukkit.wrapper.player;

import io.papermc.lib.PaperLib;
import net.gcnt.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.utils.BukkitItem;
import net.gcnt.skywarsreloaded.bukkit.wrapper.server.BukkitSWInventory;
import net.gcnt.skywarsreloaded.bukkit.wrapper.world.BukkitSWWorld;
import net.gcnt.skywarsreloaded.utils.*;
import net.gcnt.skywarsreloaded.wrapper.entity.AbstractSWPlayer;
import net.gcnt.skywarsreloaded.wrapper.server.SWInventory;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class BukkitSWPlayer extends AbstractSWPlayer {

    @Nullable
    private Player player;
    private SWInventory inventory;

    public BukkitSWPlayer(BukkitSkyWarsReloaded plugin, UUID uuid, boolean online) {
        super(plugin, uuid, online);
    }

    public BukkitSWPlayer(BukkitSkyWarsReloaded plugin, Player playerIn, boolean online) {
        this(plugin, playerIn.getUniqueId(), online);
        this.player = playerIn;
        this.inventory = new BukkitSWInventory(plugin, player.getInventory(), "Inventory");
    }

    @Nullable
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public void sendMessage(String message) throws NullPointerException {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        player.sendMessage(message);
    }

    @Override
    public boolean hasPermission(String permission) throws NullPointerException {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        return player.hasPermission(permission);
    }

    @Override
    public Item getItemInHand(boolean offHand) throws NullPointerException {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        ItemStack item;
        if (this.plugin.getUtils().getServerVersion() >= 9) {
            if (offHand) item = player.getInventory().getItemInOffHand();
            else item = player.getInventory().getItemInMainHand();
        } else item = player.getInventory().getItemInHand();
        if (item == null || item.getType() == Material.AIR) return null;
        return new BukkitItem(plugin, item);
    }

    @Override
    public SWInventory getInventory() throws NullPointerException {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");

        return this.inventory;
    }

    @Override
    public void setSlot(int slot, Item item) throws NullPointerException {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        player.getInventory().setItem(slot, ((BukkitItem) item).getBukkitItem());
    }

    @Override
    public Item getSlot(int slot) throws NullPointerException {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        return new BukkitItem(plugin, player.getInventory().getItem(slot));
    }

    @Override
    public Item getHelmet() throws NullPointerException {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        return new BukkitItem(plugin, player.getInventory().getHelmet());
    }

    @Override
    public void setHelmet(Item helmet) throws NullPointerException {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        player.getInventory().setHelmet(((BukkitItem) helmet).getBukkitItem());
    }

    @Override
    public Item getChestplate() throws NullPointerException {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        return new BukkitItem(plugin, player.getInventory().getChestplate());
    }

    @Override
    public void setChestplate(Item chestplate) throws NullPointerException {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        player.getInventory().setHelmet(((BukkitItem) chestplate).getBukkitItem());
    }

    @Override
    public Item getLeggings() throws NullPointerException {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        return new BukkitItem(plugin, player.getInventory().getLeggings());
    }

    @Override
    public void setLeggings(Item leggings) throws NullPointerException {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        player.getInventory().setHelmet(((BukkitItem) leggings).getBukkitItem());
    }

    @Override
    public Item getBoots() throws NullPointerException {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        return new BukkitItem(plugin, player.getInventory().getBoots());
    }

    @Override
    public void setBoots(Item boots) throws NullPointerException {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        player.getInventory().setHelmet(((BukkitItem) boots).getBukkitItem());
    }

    @Override
    public void clearInventory() throws NullPointerException {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        player.getInventory().clear();
    }

    @Override
    public SWCoord getLocation() throws NullPointerException {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        final Location location = player.getLocation();
        return new CoreSWCoord(new BukkitSWWorld((BukkitSkyWarsReloaded) plugin, location.getWorld()), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    @Override
    public void teleport(SWCoord coord) {
        String worldName = coord.getWorld() == null ? getLocation().getWorld().getName() : coord.getWorld().getName();
        teleport(worldName, coord.xPrecise(), coord.yPrecise(), coord.zPrecise(), coord.yaw(), coord.pitch());
    }

    @Override
    public void teleport(String world, double x, double y, double z) throws NullPointerException {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        World bukkitWorld = Bukkit.getWorld(world);
        if (bukkitWorld == null) return;
        player.teleport(new Location(bukkitWorld, x, y, z));
    }

    @Override
    public void teleport(String world, double x, double y, double z, float yaw, float pitch) throws NullPointerException {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        World bukkitWorld = Bukkit.getWorld(world);
        if (bukkitWorld == null) return;
        player.teleport(new Location(bukkitWorld, x, y, z, yaw, pitch));
    }

    @Override
    public void setExp(int level, float exp) {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        this.player.setLevel(level);
        this.player.setExp(exp);
    }

    @Override
    public void playSound(SWCoord coord, String sound, float volume, float pitch) {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        this.player.playSound(
                new Location(player.getWorld(), coord.xPrecise(), coord.yPrecise(), coord.zPrecise()),
                Sound.valueOf(sound.toUpperCase()),
                volume, pitch
        );
    }

    @Override
    public SWCompletableFuture<Boolean> teleportAsync(SWCoord coord) {
        return this.teleportAsync(coord.getWorld().getName(), coord.xPrecise(), coord.yPrecise(), coord.zPrecise());
    }

    @Override
    public SWCompletableFuture<Boolean> teleportAsync(String world, double x, double y, double z) {
        World bukkitWorld = Bukkit.getWorld(world);
        CoreSWCCompletableFuture<Boolean> successFuture = new CoreSWCCompletableFuture<>(this.plugin);
        if (bukkitWorld == null || player == null) {
            successFuture.complete(false);
            return successFuture;
        }
        final Location location = new Location(bukkitWorld, x, y, z);
        PaperLib.teleportAsync(player, location).thenAccept(successFuture::complete);
        return successFuture;

    }

    @Override
    public void sendTitle(String title, String subtitle) {
        sendTitle(title, subtitle, 20, 50, 20);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) throws NullPointerException {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        if (plugin.getUtils().getServerVersion() >= 11) player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
        else player.sendTitle(title, subtitle);
    }

    @Override
    public String getName() throws NullPointerException {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        return player.getName();
    }

    @Override
    public void setGameMode(int gamemode) throws NullPointerException {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        switch (gamemode) {
            case 0:
                player.setGameMode(GameMode.SURVIVAL);
                break;
            case 1:
                player.setGameMode(GameMode.CREATIVE);
                break;
            case 2:
                player.setGameMode(GameMode.ADVENTURE);
                break;
            case 3:
                player.setGameMode(GameMode.SPECTATOR);
                break;
        }
    }

    @Override
    public void freeze() throws NullPointerException {
        super.freeze();
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        this.player.setAllowFlight(true);
        this.player.setFlying(true);
    }

    @Override
    public void unfreeze() throws NullPointerException {
        super.unfreeze();
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        this.player.setAllowFlight(false);
        this.player.setFlying(false);
    }

    @Override
    public void fetchParentPlayer() {
        this.player = Bukkit.getPlayer(this.getUuid());
        if (this.player != null) this.inventory = new BukkitSWInventory(plugin, player.getInventory(), "Inventory");
    }

    @Override
    public int getFoodLevel() {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        return this.player.getFoodLevel();
    }

    @Override
    public void setFoodLevel(int foodLevel) {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        this.player.setFoodLevel(foodLevel);
    }

    @Override
    public double getHealth() {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        return this.player.getHealth();
    }

    @Override
    public void setHealth(double health) {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        this.player.setHealth(health);
    }

    @Override
    public boolean isAllowFlight() {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        return this.player.getAllowFlight();
    }

    @Override
    public void setAllowFlight(boolean allowFlight) {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        this.player.setAllowFlight(allowFlight);
    }

    @Override
    public void setFlying(boolean flying) {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        this.player.setFlying(flying);
    }

    @Override
    public boolean isFlying() {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        return this.player.isFlying();
    }

    @Override
    public void setFireTicks(int ticks) {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        this.player.setFireTicks(ticks);
    }

    @Override
    public void clearBodyArrows() {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        this.player.setArrowsInBody(0);
    }

    @Override
    public String getType() {
        return "PLAYER";
    }

    @Override
    public void openInventory(SWInventory inventory) {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        this.player.openInventory(((BukkitSWInventory) inventory).getBukkitInventory());
    }

    @Override
    public void closeInventory() {
        if (this.player == null) throw new NullPointerException("Bukkit player is null");
        this.player.closeInventory();
    }
}
