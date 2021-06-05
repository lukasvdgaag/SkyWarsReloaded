package com.walrusone.skywarsreloaded.game;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.managers.PlayerStat;
import com.walrusone.skywarsreloaded.utilities.Tagged;
import com.walrusone.skywarsreloaded.utilities.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerData {
    private static ArrayList<PlayerData> playerData;

    static {
        PlayerData.playerData = new ArrayList<>();
    }

    private final UUID uuid;
    private final Scoreboard sbBeforeGame;
    private Tagged taggedBy;
    private final ItemStack[] savedInv;
    private final ItemStack[] savedArmor;
    private final double healthBeforeGame;
    private final int foodBeforeGame;
    private final float saturationBeforeGame;
    private float experienceBeforeGame;

    private boolean beingRestored;
    private Location locationBeforeRespawn = null;

    public PlayerData(final Player p) {
        if (SkyWarsReloaded.getCfg().debugEnabled()) {
            Util.get().logToFile(ChatColor.RED + "[skywars] " + ChatColor.YELLOW + "Creating " + p.getName() + "'s data");
        }
        this.beingRestored = false;
        this.uuid = p.getUniqueId();
        this.sbBeforeGame = p.getScoreboard();
        this.healthBeforeGame = p.getHealth();
        this.foodBeforeGame = p.getFoodLevel();
        this.saturationBeforeGame = p.getSaturation();
        if (!SkyWarsReloaded.getCfg().displayPlayerExeperience()) {
            this.experienceBeforeGame = p.getExp();
        }
        PlayerInventory pInv = p.getInventory();

        /*this.savedInv = (PlayerInventory) Bukkit.createInventory(null, InventoryType.PLAYER, p.getName());*/
        this.savedInv = pInv.getContents();
        this.savedArmor = pInv.getArmorContents();

        if (SkyWarsReloaded.getCfg().debugEnabled()) {
            Util.get().logToFile(ChatColor.RED + "[skywars] " + ChatColor.YELLOW + p.getName() + "'s data has been saved");
        }
    }

    public static ArrayList<PlayerData> getAllPlayerData() {
        return PlayerData.playerData;
    }

    public static PlayerData getPlayerData(final UUID uuid) {
        for (final PlayerData pData : getAllPlayerData()) {
            if (pData.getUuid().toString().equals(uuid.toString())) {
                return pData;
            }
        }
        return null;
    }

    public void restoreToBeforeGameState(boolean restoreInstantly) {
        this.restoreToBeforeGameState(restoreInstantly, true);
    }

    public void restoreToBeforeGameState(boolean restoreInstantly, boolean sendToLobby) {
        if (!beingRestored) {
            beingRestored = true;
            final Player player = this.getPlayer();
            if (player == null) {
                return;
            }

            if (SkyWarsReloaded.getCfg().debugEnabled()) {
                Util.get().logToFile(ChatColor.RED + "[skywars] " + ChatColor.YELLOW + "Restoring player to before game state: " + player.getName());
            }

            // Reset player
            PlayerStat pStats = PlayerStat.getPlayerStats(player);
            player.closeInventory();
            // Gamemode reset
            player.setGameMode(GameMode.SURVIVAL);
            if (SkyWarsReloaded.getCfg().displayPlayerExeperience()) {
                if (pStats != null) {
                    Util.get().setPlayerExperience(player, pStats.getXp());
                }
            }
            // Reset inventory
            Util.get().clear(player);
            PlayerInventory pInv = player.getInventory();
            pInv.setContents(savedInv);
            pInv.setArmorContents(savedArmor);
            SkyWarsReloaded.getNMS().setMaxHealth(player, 20);
            // Remove death screen for player - this will send them to spawn so we undo that by TP back
            this.locationBeforeRespawn = player.getLocation();
            Util.get().respawnPlayer(player);
            // Set health
            if (healthBeforeGame <= 0 || healthBeforeGame > 20) {
                player.setHealth(20);
            } else {
                player.setHealth(healthBeforeGame);
            }
            // Other data to reset
            player.setFoodLevel(foodBeforeGame);
            player.setSaturation(saturationBeforeGame);
            player.resetPlayerTime();
            player.resetPlayerWeather();
            // We set flying to false later when moving back to lobby
            player.setAllowFlight(true);
            player.setFlying(true);
            player.setFireTicks(0);
            player.setVelocity(new Vector(0,0,0));
            player.setFallDistance(0.0f);
            player.setNoDamageTicks(1);
            if (!SkyWarsReloaded.getCfg().displayPlayerExeperience()) {
                player.setExp(experienceBeforeGame);
            }
            if (player.isOnline()) {
                player.updateInventory();
            }

            // Send back to lobby
            if (sendToLobby) {
                sendToLobby(restoreInstantly);
            }

            // Reset scoreboard
            PlayerStat.resetScoreboard(player);
            player.setScoreboard(sbBeforeGame);
            if (SkyWarsReloaded.getCfg().lobbyBoardEnabled() && !SkyWarsReloaded.getCfg().bungeeMode()) {
                PlayerStat.updateScoreboard(player, "lobbyboard");
            }

            if (SkyWarsReloaded.getCfg().debugEnabled()) {
                Util.get().logToFile(ChatColor.RED + "[skywars] " + ChatColor.YELLOW + "Finished restoring " + player.getName() + ". Teleporting to Spawn");
            }
            if (SkyWarsReloaded.getCfg().bungeeMode()) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        String uuid = player.getUniqueId().toString();
                        //SkyWarsReloaded.get().sendBungeeMsg(player, "Connect", SkyWarsReloaded.getCfg().getBungeeLobby());
                        // line above has been moved
                        PlayerStat remove = PlayerStat.getPlayerStats(uuid);
                        PlayerStat.getPlayers().remove(remove);
                    }
                }.runTaskLater(SkyWarsReloaded.get(), 5);
            }

            // Clear cached data from self
            getAllPlayerData().remove(this);
        }
    }

    public Player getPlayer() {
        return SkyWarsReloaded.get().getServer().getPlayer(this.uuid);
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public Tagged getTaggedBy() {
        return taggedBy;
    }

    public void setTaggedBy(Player player) {
        taggedBy = new Tagged(player, System.currentTimeMillis());
    }

    // UTILS

    public void sendToLobby(boolean instantRemove) {
        // General vars
        boolean isBungee = SkyWarsReloaded.getCfg().bungeeMode();
        boolean isPluginEnabled = SkyWarsReloaded.get().isEnabled();

        if (SkyWarsReloaded.getCfg().debugEnabled()) {
            SkyWarsReloaded.get().getLogger().info(
                    "#debug PlayerData::sendToLobby: Now sending player to lobby (bungee: " + isBungee +
                            ", quit: " + instantRemove + ", isEnabled: " + isPluginEnabled + ")");
        }

        // If needs instant removal of game
        if (instantRemove || !isPluginEnabled) {
            sendToLobbyNow();
            // If removal can be done later
        } else {
            new BukkitRunnable() {
                @Override
                public void run() {
                    sendToLobbyNow();
                }
            }.runTaskLater(SkyWarsReloaded.get(), 2);
        }
    }

    public void sendToLobbyNow() {
        Player player = getPlayer();
        player.setAllowFlight(false);
        player.setFlying(false);
        beingRestored = false;
        if (!player.isOnline()) return;
        if (SkyWarsReloaded.getCfg().bungeeMode())
            connectToBungeeLobby(player);
        else
            teleportToBukkitLobby(player);
    }

    public void connectToBungeeLobby(Player player) {
        player.teleport(locationBeforeRespawn);
        Bukkit.getConsoleSender().sendMessage("Now connecting player to lobby (1)");
        SkyWarsReloaded.get().sendBungeeMsg(player, "Connect", SkyWarsReloaded.getCfg().getBungeeLobby());
    }

    public static void teleportToBukkitLobby(Player player) {
        final Location respawn = SkyWarsReloaded.getCfg().getSpawn();
        if (respawn == null) {
            SkyWarsReloaded.get().getLogger().severe("Skywars lobby spawn is not set!");
            return;
        }
        player.teleport(respawn, TeleportCause.END_PORTAL);
    }

}