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
import org.bukkit.inventory.Inventory;
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

    private UUID uuid;
    private Scoreboard sbBeforeGame;
    private Tagged taggedBy;
    private Inventory inv;
    private double healthBeforeGame;
    private int foodBeforeGame;
    private float saturationBeforeGame;
    private float experienceBeforeGame;
    private boolean beingRestored;

    public PlayerData(final Player p) {
        if (SkyWarsReloaded.getCfg().debugEnabled()) {
            Util.get().logToFile(ChatColor.RED + "[skywars] " + ChatColor.YELLOW + "Creating " + p.getName() + "'s Datafile");
        }
        this.beingRestored = false;
        this.uuid = p.getUniqueId();
        this.sbBeforeGame = p.getScoreboard();
        this.healthBeforeGame = p.getHealth();
        this.foodBeforeGame = p.getFoodLevel();
        this.saturationBeforeGame = p.getSaturation();
        if (!SkyWarsReloaded.getCfg().displayPlayerExeperience()) {
            experienceBeforeGame = p.getExp();
        }
        inv = Bukkit.createInventory(null, InventoryType.PLAYER, p.getName());
        inv.setContents(p.getInventory().getContents());
        if (SkyWarsReloaded.getCfg().debugEnabled()) {
            Util.get().logToFile(ChatColor.RED + "[skywars] " + ChatColor.YELLOW + p.getName() + "'s Datafile has been created");
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
        if (!beingRestored) {
            beingRestored = true;
            final Player player = this.getPlayer();
            if (player == null) {
                return;
            }

            if (SkyWarsReloaded.getCfg().debugEnabled()) {
                Util.get().logToFile(ChatColor.RED + "[skywars] " + ChatColor.YELLOW + "Restoring " + player.getName());
            }

            // Reset player
            PlayerStat pStats = PlayerStat.getPlayerStats(player);
            player.closeInventory();
            player.setGameMode(GameMode.SURVIVAL);
            if (SkyWarsReloaded.getCfg().displayPlayerExeperience()) {
                if (pStats != null) {
                    Util.get().setPlayerExperience(player, pStats.getXp());
                }
            }
            Util.get().clear(player);
            player.getInventory().clear();
            player.getInventory().setContents(inv.getContents());
            SkyWarsReloaded.getNMS().setMaxHealth(player, 20);
            Util.get().respawnPlayer(player);
            if (healthBeforeGame <= 0 || healthBeforeGame > 20) {
                player.setHealth(20);
            } else {
                player.setHealth(healthBeforeGame);
            }
            player.setFoodLevel(foodBeforeGame);
            player.setSaturation(saturationBeforeGame);
            player.resetPlayerTime();
            player.resetPlayerWeather();
            player.setAllowFlight(false);
            player.setFlying(false);
            player.setFireTicks(0);
            player.setVelocity(new Vector(0,0,0));
            player.setFallDistance(0.0f);
            player.setNoDamageTicks(1);
            if (!SkyWarsReloaded.getCfg().displayPlayerExeperience()) {
                player.setExp(experienceBeforeGame);
            }

            // Send back to lobby
            sendToLobby(player, restoreInstantly);

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

    public static void sendToLobby(Player player, boolean instantRemove) {
        // General vars
        boolean isBungee = SkyWarsReloaded.getCfg().bungeeMode();
        boolean isPluginEnabled = SkyWarsReloaded.get().isEnabled();

        if (SkyWarsReloaded.getCfg().debugEnabled()) {
            SkyWarsReloaded.get().getLogger().info(
                    "#debug PlayerData::sendToLobby: Now sending player to lobby (bungee: " + isBungee + ", quit: " + instantRemove + ", isEnabled: " + isPluginEnabled + ")");
        }

        // If needs instant removal of game
        if (instantRemove || !isPluginEnabled) {
            sendToLobbyNow(player);
        // If removal can be done later
        } else {
            new BukkitRunnable() {
                @Override
                public void run() {
                    sendToLobbyNow(player);
                }
            }.runTaskLater(SkyWarsReloaded.get(), 2);
        }
    }

    public static void sendToLobbyNow(Player player) {
        if (SkyWarsReloaded.getCfg().bungeeMode())
            connectToBungeeLobby(player);
        else
            teleportToBukkitLobby(player);
    }

    public static void connectToBungeeLobby(Player player) {
        Bukkit.getConsoleSender().sendMessage("Now connecting player to lobby (1)");
        SkyWarsReloaded.get().sendBungeeMsg(player, "Connect", SkyWarsReloaded.getCfg().getBungeeLobby());

    }

    public static void teleportToBukkitLobby(Player player) {
        final Location respawn = SkyWarsReloaded.getCfg().getSpawn();
        player.teleport(respawn, TeleportCause.END_PORTAL);
    }

}