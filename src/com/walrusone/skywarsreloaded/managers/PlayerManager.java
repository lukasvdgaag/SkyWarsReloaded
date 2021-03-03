package com.walrusone.skywarsreloaded.managers;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.enums.MatchState;
import com.walrusone.skywarsreloaded.enums.PlayerRemoveReason;
import com.walrusone.skywarsreloaded.events.SkyWarsDeathEvent;
import com.walrusone.skywarsreloaded.events.SkyWarsKillEvent;
import com.walrusone.skywarsreloaded.events.SkyWarsLeaveEvent;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.game.PlayerCard;
import com.walrusone.skywarsreloaded.game.PlayerData;
import com.walrusone.skywarsreloaded.game.TeamCard;
import com.walrusone.skywarsreloaded.menus.gameoptions.objects.CoordLoc;
import com.walrusone.skywarsreloaded.menus.playeroptions.KillSoundOption;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.Tagged;
import com.walrusone.skywarsreloaded.utilities.Util;
import com.walrusone.skywarsreloaded.utilities.VaultUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerManager {

    private SkyWarsReloaded swr;

    private boolean debug;

    private MatchManager matchManager;

    public PlayerManager(SkyWarsReloaded swrIn) {
        this.swr = swrIn;
        this.debug = SkyWarsReloaded.getCfg().debugEnabled();
        this.matchManager = swr.getMatchManager();
    }

    public void removePlayerInGame(final Player playerRemoved, PlayerRemoveReason removeReason, @Nullable EntityDamageEvent.DamageCause deathCause, boolean announceToOthers) {
        // General constants
        final UUID pUuid = playerRemoved.getUniqueId();
        final GameMap gameMap = matchManager.getPlayerMap(playerRemoved);
        // Check player is in game
        if (gameMap == null) {
            return;
        }
        final PlayerData playerData;
        final PlayerCard playerCard = gameMap.getPlayerCard(playerRemoved);
        final TeamCard teamCard = playerCard.getTeamCard();

        // Process extra constants
        PlayerData tmpPData = PlayerData.getPlayerData(pUuid);
        if (tmpPData == null) {
            this.swr.getLogger().warning("Player data was null when leaving game! Please report this.");
            playerData = new PlayerData(playerRemoved);
        } else {
            playerData = tmpPData;
        }
        tmpPData = null;

        // Remove player options no matter if in game or not
        SkyWarsReloaded.getOM().removePlayer(pUuid);

        if (gameMap.getMatchState() == MatchState.PLAYING) {

            // ---------------- TEAM & PLAYER DATA ------------------
            // Process Team data
            teamCard.getDead().add(pUuid);
            playerCard.getTeamCard().setPlace(gameMap.getTeamsLeft() + 1); // + 1 because we are counting from 1

            // Process Player tagging
            Tagged tagger = playerData.getTaggedBy();
            Player taggerPlayer;
            boolean isRecentTag;
            if (tagger != null) {
                taggerPlayer = tagger.getPlayer();
                isRecentTag = System.currentTimeMillis() - tagger.getTime() < 10000; // 10 seconds
            } else {
                taggerPlayer = null;
                isRecentTag = false;
            }
            boolean wasKilledByTagger = taggerPlayer != null && isRecentTag;

            // ------------------ KILL HANDLING & EVENTS -----------------------
            // Player was removed by other plugin reason - ex: server restart or map unload
            // In this case no death should be recorded since this action was forced
            if (removeReason.equals(PlayerRemoveReason.OTHER)) {
                // TODO ------------
            // Player died or quit the game while playing
            } else {
                // Process loser
                this.updateStatsForLoser(playerRemoved, playerCard, playerData);
                Bukkit.getPluginManager().callEvent(new SkyWarsDeathEvent(playerRemoved, deathCause, gameMap));
                // Process killer (if exists)
                if (wasKilledByTagger) {
                    this.updateStatsForKiller(taggerPlayer);
                    gameMap.increaseDisplayedKillsVar(taggerPlayer);
                    Bukkit.getPluginManager().callEvent(new SkyWarsKillEvent(taggerPlayer, playerRemoved, gameMap));
                }
            }
            Bukkit.getPluginManager().callEvent(new SkyWarsLeaveEvent(playerRemoved, gameMap));

            // ----------------- END PROCESSING ------------------
            gameMap.removePlayer(playerRemoved.getUniqueId());
            // TODO: SPEC MODE
            //  not send to lobby if spec enabled
            playerData.restoreToBeforeGameState(removeReason != PlayerRemoveReason.DEATH);
            // Remove dead / quit player from spectator inventory
            for (UUID uuid : gameMap.getSpectators()) {
                if (!uuid.equals(playerRemoved.getUniqueId())) {
                    Player spec = SkyWarsReloaded.get().getServer().getPlayer(uuid);
                    SkyWarsReloaded.get().getPlayerManager().prepareSpectateInv(spec, gameMap);
                }
            }

            // --------------------- MESSAGES ---------------------
            if (removeReason.equals(PlayerRemoveReason.DEATH)) {
                if (taggerPlayer != null) {
                    this.matchManager.message(
                            gameMap,
                            Util.get().getDeathMessage(deathCause, true, playerRemoved, taggerPlayer),
                            null
                    );
                }
            } else if (removeReason.equals(PlayerRemoveReason.PLAYER_QUIT_GAME)) {
                if (wasKilledByTagger) {
                    this.matchManager.message(gameMap, new Messaging.MessageFormatter()
                            .withPrefix()
                            .setVariable("player", playerRemoved.getName())
                            .setVariable("killer", taggerPlayer.getName())
                            .format("game.death.quit-while-tagged"), null
                    );
                } else {
                    matchManager.message(gameMap, new Messaging.MessageFormatter()
                            .setVariable("player", playerRemoved.getName())
                            .format("game.left-the-game"), playerRemoved);
                }
            }
            new BukkitRunnable() {
                public void run() {
                    if (playerRemoved.isOnline()) {
                        playerRemoved.sendMessage(new Messaging.MessageFormatter()
                                .setVariable("arena", gameMap.getDisplayName())
                                .setVariable("map", gameMap.getName()).format("game.lost"));
                    }
                }
            }.runTaskLater(SkyWarsReloaded.get(), 10L);
        }
    }

    public void addSpectator(final GameMap gameMap, final Player player) {
        if (player != null) {
            World world = gameMap.getCurrentWorld();
            CoordLoc ss = gameMap.getSpectateSpawn();
            Location spectateSpawn = new Location(world, ss.getX(), ss.getY(), ss.getZ());
            player.teleport(spectateSpawn, PlayerTeleportEvent.TeleportCause.END_PORTAL);

            new BukkitRunnable() {
                @Override
                public void run() {
                    PlayerData pd = PlayerData.getPlayerData(player.getUniqueId());
                    if (pd == null) {
                        PlayerData.getAllPlayerData().add(new PlayerData(player));
                    }
                    Util.get().clear(player);
                    // Armor was individually set, moved to null array because it's more concise
                    player.getInventory().setArmorContents(new ItemStack[] {null, null, null, null});
                    player.setGameMode(GameMode.SPECTATOR);
                    gameMap.getGameBoard().updateScoreboard(player);

                    prepareSpectateInv(player, gameMap);

                    ItemStack exitItem = new ItemStack(Material.IRON_DOOR, 1);
                    ItemMeta exit = exitItem.getItemMeta();
                    exit.setDisplayName(new Messaging.MessageFormatter().format("spectate.exititemname"));
                    List<String> lore = new ArrayList<>();
                    lore.add(new Messaging.MessageFormatter().format("spectate.exititemlore"));
                    exit.setLore(lore);
                    exitItem.setItemMeta(exit);
                    player.getInventory().setItem(8, exitItem);
                    player.sendMessage(new Messaging.MessageFormatter().format("spectate.startmessage"));
                    player.sendMessage(new Messaging.MessageFormatter().format("spectate.startmessage2"));
                    if (debug) {
                        Util.get().logToFile(matchManager.getDebugName(gameMap) + ChatColor.YELLOW + player.getName() + " has been added to spectators");
                    }
                }

            }.runTaskLater(SkyWarsReloaded.get(), 3);
            gameMap.getSpectators().add(player.getUniqueId());
        }
    }

    public void prepareSpectateInv(Player player, GameMap gameMap) {
        int slot = 9;
        if (player != null) {
            for (int i = 9; i < player.getInventory().getSize() - 1; i++) {
                player.getInventory().setItem(i, null);
            }
        } else {
            return;
        }

        for (Player player1 : gameMap.getAlivePlayers()) {
            if (player1 != null) {
                ItemStack playerhead1 = SkyWarsReloaded.getNMS().getBlankPlayerHead();
                SkullMeta meta1 = (SkullMeta) playerhead1.getItemMeta();
                SkyWarsReloaded.getNMS().updateSkull(meta1, player1);
                meta1.setDisplayName(ChatColor.YELLOW + player1.getName());
                List<String> lore = new ArrayList<>();
                lore.add(new Messaging.MessageFormatter().setVariable("player", player1.getName()).format("spectate.playeritemlore"));
                meta1.setLore(lore);
                playerhead1.setItemMeta(meta1);
                if (player != null) {
                    player.getInventory().setItem(slot, playerhead1);
                } else {
                    break;
                }
                slot++;
            }
        }
    }

    public void updateStatsForLoser(Player player, PlayerCard pCard, PlayerData playerData) {
        PlayerStat loserData = PlayerStat.getPlayerStats(player.getUniqueId().toString());
        if (loserData != null) {
            loserData.setDeaths(loserData.getDeaths() + 1);
        }
    }

    public void updateStatsForKiller(Player killer) {
        PlayerStat killerData = PlayerStat.getPlayerStats(killer);
        int multiplier = Util.get().getMultiplier(killer);
        if (killerData != null) {
            killerData.setKills(killerData.getKills() + 1);
            killerData.setXp(killerData.getXp() + (multiplier * SkyWarsReloaded.getCfg().getKillerXP()));
            KillSoundOption sound = (KillSoundOption) KillSoundOption.getPlayerOptionByKey(killerData.getKillSound());
            if (sound != null) {
                sound.playSound(killer.getLocation());
            }
        }
        if (SkyWarsReloaded.getCfg().economyEnabled()) {
            VaultUtils.get().give(killer, multiplier * SkyWarsReloaded.getCfg().getKillerEco());
        }
        Util.get().sendActionBar(killer, new Messaging.MessageFormatter().setVariable("xp", "" + multiplier * SkyWarsReloaded.getCfg().getKillerXP()).format("game.kill-actionbar"));
        Util.get().doCommands(SkyWarsReloaded.getCfg().getKillCommands(), killer);
    }

}
