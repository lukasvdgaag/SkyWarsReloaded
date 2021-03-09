package com.walrusone.skywarsreloaded.managers;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.enums.MatchState;
import com.walrusone.skywarsreloaded.enums.PlayerRemoveReason;
import com.walrusone.skywarsreloaded.events.SkyWarsDeathEvent;
import com.walrusone.skywarsreloaded.events.SkyWarsKillEvent;
import com.walrusone.skywarsreloaded.events.SkyWarsLeaveEvent;
import com.walrusone.skywarsreloaded.game.*;
import com.walrusone.skywarsreloaded.game.cages.schematics.SchematicCage;
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

    private final SkyWarsReloaded swr;

    private final boolean debug;

    private final MatchManager matchManager;

    public PlayerManager(SkyWarsReloaded swrIn) {
        this.swr = swrIn;
        this.debug = SkyWarsReloaded.getCfg().debugEnabled();
        this.matchManager = swr.getMatchManager();
    }

    public void removePlayer(final Player playerRemoved, PlayerRemoveReason removeReason, @Nullable EntityDamageEvent.DamageCause deathCause, boolean announceToOthers) {
        // General constants
        final UUID pUuid = playerRemoved.getUniqueId();
        final GameMap gameMap = matchManager.getPlayerMap(playerRemoved);
        final PlayerData playerData;

        // Remove player options (no matter if in game or not)
        SkyWarsReloaded.getOM().removePlayer(pUuid);

        // Process extra constants
        PlayerData tmpPData = PlayerData.getPlayerData(pUuid);
        if (tmpPData == null) {
            this.swr.getLogger().warning("Player data was null when leaving game!");
            playerData = new PlayerData(playerRemoved);
        } else {
            playerData = tmpPData;
        }

        // Filter which type of remove we should perform
        // Player is in an arena - else we don't handle removes
        if (gameMap != null) {
            MatchState mState = gameMap.getMatchState();
            boolean shouldRestorePlayerToLobby = true;
            // Player is waiting before game start
            if (mState.equals(MatchState.WAITINGSTART) ||
                    mState.equals(MatchState.WAITINGLOBBY) ||
                    mState.equals(MatchState.ENDING)
            ) {
                // Process main remove
                this.removeWaitingPlayer(
                        playerRemoved,
                        pUuid,
                        gameMap,
                        playerData,
                        removeReason,
                        announceToOthers
                );
                // Player is in game and died, quit or was removed
            } else if (mState.equals(MatchState.PLAYING)) {
                if (gameMap.getSpectators().contains(pUuid)) {
                    this.removeSpectatorPlayer(
                            playerRemoved,
                            pUuid,
                            gameMap,
                            playerData,
                            removeReason,
                            announceToOthers
                    );
                } else if (gameMap.getAlivePlayers().contains(playerRemoved)) {
                    shouldRestorePlayerToLobby =
                            this.removePlayerInGame(
                                    playerRemoved,
                                    pUuid,
                                    gameMap,
                                    playerData,
                                    removeReason,
                                    deathCause,
                                    announceToOthers
                            );
                } else {
                    this.swr.getLogger().warning(
                            "Tried to remove a player from a playing game that was neither a spectator nor an alive player!");
                }
            }
            // Should player be restored back to how they were before entering the game
            if (shouldRestorePlayerToLobby) {
                // Restore and delete data
                playerData.restoreToBeforeGameState(removeReason != PlayerRemoveReason.DEATH);
                // Remove scoreboard from saved data
                PlayerStat.resetScoreboard(playerRemoved);
            }
        } else {
            SkyWarsReloaded.get().getLogger().warning("PlayerManager::removePlayer Attempted to remove player but player is not in a game map!");
        }
    }

    /**
     * Remove player waiting or player waiting for game end.
     * @param playerRemoved The player to be removed
     * @param pUuid The UUID of the player to remvoe
     * @param gameMap The game map object from which this was fired
     * @param playerData The player data of the leaving player
     * @param removeReason The reason the player was removed
     * @param announceToOthers Whther this leave should be announced to others
     */
    private void removeWaitingPlayer(
            final Player playerRemoved,
            UUID pUuid, GameMap gameMap,
            PlayerData playerData,
            PlayerRemoveReason removeReason,
            boolean announceToOthers
    ) {
        // Check player is in a game map
        if (gameMap == null) {
            return;
        }
        // General vars
        boolean isWaitingForGameStart =
                gameMap.getMatchState() == MatchState.WAITINGSTART ||
                        gameMap.getMatchState() == MatchState.WAITINGLOBBY;
        boolean isGameEnding =
                gameMap.getMatchState() == MatchState.ENDING;

        // ------------- LEAVE HANDLING & EVENTS -------------
        // If player is waiting before game
        if (isWaitingForGameStart) {
            // Remove cage for player
            PlayerStat ps = PlayerStat.getPlayerStats(playerRemoved);
            if (ps != null && !removeReason.equals(PlayerRemoveReason.DEATH)) {
                String cageName = ps.getGlassColor();
                if (gameMap.getTeamSize() == 1 || SkyWarsReloaded.getCfg().isUseSeparateCages()) {
                    // If is custom cage, use WorldEdit to undo that operation.
                    if (cageName.startsWith("custom-")) {
                        new SchematicCage().removeSpawnPlatform(gameMap, playerRemoved);
                    } else {
                        gameMap.getCage().removeSpawnHousing(gameMap, gameMap.getTeamCard(playerRemoved), false);
                    }
                }
            }
            // Play leave sound for players still present
            for (final Player p : gameMap.getAlivePlayers()) {
                Util.get().playSound(p, p.getLocation(), SkyWarsReloaded.getCfg().getLeaveSound(), 1, 1);
            }
        } else if (isGameEnding) {
            // nothing to do right now, all general data is handled in parent method
        }
        Bukkit.getPluginManager().callEvent(new SkyWarsLeaveEvent(playerRemoved, gameMap));

        // ---------------- GAME MAP UPDATES ----------------
        // Remove waiting player
        gameMap.removePlayer(pUuid);
        // Update lobby signs with new amount of players waiting
        gameMap.updateSigns();

        // ---------------- MESSAGES ------------------
        // Removed while waiting is cages
        if (announceToOthers) {
            if (isWaitingForGameStart) {
                if (SkyWarsReloaded.getCfg().titlesEnabled()) {
                    // Get all players to send leave title to
                    List<Player> alivePlayers = gameMap.getAlivePlayers();
                    // Don't include the leaving player
                    alivePlayers.remove(playerRemoved);
                    // Send titles
                    for (final Player p : alivePlayers) {
                        Util.get().sendTitle(p, 2, 20, 2, "",
                                new Messaging.MessageFormatter().setVariable("player", playerRemoved.getDisplayName())
                                        .setVariable("players", "" + gameMap.getPlayerCount())
                                        .setVariable("playercount", "" + gameMap.getPlayerCount())
                                        .setVariable("maxplayers", "" + gameMap.getMaxPlayers()).format("game.left-the-game"));
                    }
                }
            }
            // Send leave message to all players (waiting before start or during ending state)
            matchManager.message(gameMap, new Messaging.MessageFormatter().setVariable("player", playerRemoved.getDisplayName())
                    .setVariable("players", "" + gameMap.getPlayerCount())
                    .setVariable("playercount", "" + gameMap.getPlayerCount())
                    .setVariable("maxplayers", "" + gameMap.getMaxPlayers()).format("game.waitstart-left-the-game"), playerRemoved);
        }
    }

    /**
     * Remove player spectating the game
     * @param playerRemoved
     * @param pUuid
     * @param gameMap
     * @param playerData
     * @param removeReason
     * @param announceToOthers
     */
    public void removeSpectatorPlayer(
            final Player playerRemoved,
            UUID pUuid,
            GameMap gameMap,
            PlayerData playerData,
            PlayerRemoveReason removeReason,
            boolean announceToOthers
    ) {
        // Check player is in game
        if (gameMap == null) {
            return;
        }
        // Verify safety
        if (!playerRemoved.getGameMode().equals(GameMode.SPECTATOR)) {
            swr.getLogger().warning("Spectator " + playerRemoved.getName() + " is not in spectator mode! If this is not expected, please report this.");
        }
        // ---------------- GAME MAP UPDATES -----------------
        // Remove from spectators
        gameMap.removePlayer(pUuid);

        // ------------------- MESSAGES --------------------
        if (debug) {
            Util.get().logToFile(this.matchManager.getDebugName(gameMap) + ChatColor.YELLOW +
                    playerRemoved.getName() + " has been removed from spectators");
        }
    }

    /**
     * Remove a player which is currently in a game map and alive
     * @param playerRemoved Player that died or was removed
     * @param pUuid UUID of player
     * @param gameMap GameMap from which this was triggered
     * @param playerData PlayerData of the removed player
     * @param removeReason PlayerRemoveReason - why the player was removed
     * @param deathCause If the player died, the cause of death. Else, null.
     * @param announceToOthers If the messages should be sent to other players from the map
     * @return If the player should be instantly/completely removed from the map (false: spectator mode is enabled)
     */
    private boolean removePlayerInGame(
            final Player playerRemoved,
            UUID pUuid,
            GameMap gameMap,
            PlayerData playerData,
            PlayerRemoveReason removeReason,
            @Nullable EntityDamageEvent.DamageCause deathCause,
            boolean announceToOthers
    ) {
        // Check player is in game
        if (gameMap == null) {
            return true;
        }
        // Return data
        boolean shouldSendToLobby = true;
        // General vars
        final PlayerCard playerCard = gameMap.getPlayerCard(playerRemoved);
        final TeamCard teamCard = playerCard.getTeamCard();

        // ---------------- TEAM & PLAYER DATA ------------------
        // Process Team data
        teamCard.getDead().add(pUuid);
        teamCard.removePlayer(pUuid);
        // Place the first team to die, last. So we use players left (missing the current one, so +1)
        // + 1 because we are counting from 1
        if (teamCard.isElmininated())
            teamCard.setPlace(gameMap.getTeamsLeft() + 1);

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
            // Don't count in player stats since this is caused by plugin force removal
            // Player died or quit the game while playing
        } else {
            // Process loser
            this.updateStatsForLoser(playerRemoved);
            Bukkit.getPluginManager().callEvent(new SkyWarsDeathEvent(playerRemoved, deathCause, gameMap));
            // Process killer (if exists)
            if (wasKilledByTagger) {
                this.updateStatsForKiller(taggerPlayer);
                gameMap.increaseDisplayedKillsVar(taggerPlayer);
                Bukkit.getPluginManager().callEvent(new SkyWarsKillEvent(taggerPlayer, playerRemoved, gameMap));
            }
        }
        Bukkit.getPluginManager().callEvent(new SkyWarsLeaveEvent(playerRemoved, gameMap));

        // ---------------- GAME MAP UPDATES -----------------
        gameMap.removePlayer(pUuid);
        if (SkyWarsReloaded.getCfg().spectateEnable() && removeReason.equals(PlayerRemoveReason.DEATH)) {
            this.addSpectator(gameMap, playerRemoved);
            shouldSendToLobby = false;
        }
        // Remove dead / quit player from other spectator inventories
        this.updateAllSpectatorInventories(gameMap, playerRemoved);

        // --------------------- MESSAGES ---------------------
        if (announceToOthers) {
            // Killed by player or environment
            if (removeReason.equals(PlayerRemoveReason.DEATH)) {
                String message;
                if (taggerPlayer == null) {
                    message = Util.get().getDeathMessage(deathCause, false, playerRemoved, null);
                } else {
                    message = Util.get().getDeathMessage(deathCause, true, playerRemoved, taggerPlayer);
                }
                this.matchManager.message(gameMap, message, null);
                // Leaving game or server
            } else if (removeReason.equals(PlayerRemoveReason.PLAYER_QUIT_GAME) ||
                    removeReason.equals(PlayerRemoveReason.PLAYER_QUIT_SERVER)
            ) {
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
        }
        // Tell the player they lost the game
        new BukkitRunnable() {
            public void run() {
                if (playerRemoved.isOnline()) {
                    playerRemoved.sendMessage(new Messaging.MessageFormatter()
                            .setVariable("arena", gameMap.getDisplayName())
                            .setVariable("map", gameMap.getName()).format("game.lost"));
                }
            }
        }.runTaskLater(SkyWarsReloaded.get(), 10L);

        // ----------------- POST PROCESSING ------------------
        // Check map win
        matchManager.checkForWin(gameMap);

        // Return whether the player was completely removed (false: spec mode)
        return shouldSendToLobby;
    }

    public void updateAllSpectatorInventories(GameMap gameMap, @Nullable Player playerToNotUpdate) {
        for (UUID uuid : gameMap.getSpectators()) {
            if (playerToNotUpdate != null && !uuid.equals(playerToNotUpdate.getUniqueId())) {
                Player spec = SkyWarsReloaded.get().getServer().getPlayer(uuid);
                this.prepareSpectateInv(spec, gameMap);
            }
        }
    }

    public void addSpectator(final GameMap gameMap, final Player player) {
        if (player != null) {
            World world = gameMap.getCurrentWorld();
            CoordLoc ss = gameMap.getSpectateSpawn();
            Location spectateSpawn = new Location(world, ss.getX(), ss.getY(), ss.getZ());
            player.teleport(spectateSpawn, PlayerTeleportEvent.TeleportCause.END_PORTAL);

            gameMap.getSpectators().add(player.getUniqueId());

            PlayerData pd = PlayerData.getPlayerData(player.getUniqueId());
            if (pd == null) {
                PlayerData.getAllPlayerData().add(new PlayerData(player));
            }
            Util.get().clear(player);
            // Armor was individually set, moved to null array because it's more concise
            player.getInventory().setArmorContents(new ItemStack[] {null, null, null, null});
            player.setGameMode(GameMode.SPECTATOR);
            gameMap.getGameBoard().updateScoreboard(player);

            PlayerManager.this.prepareSpectateInv(player, gameMap);

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
    }

    public void prepareSpectateInv(Player spectatorPlayer, GameMap gameMap) {
        int slot = 9;
        if (spectatorPlayer == null) return;

        // Clear inventory starting at slot 9 (stop before last slot since that's for the leave item)
        for (int i = 9; i < spectatorPlayer.getInventory().getSize() - 1; i++) {
            spectatorPlayer.getInventory().setItem(i, null);
        }

        for (Player alivePlayer : gameMap.getAlivePlayers()) {
            if (alivePlayer != null) {
                // Prepare player skull as clickable item in spec inventory
                ItemStack playerhead1 = SkyWarsReloaded.getNMS().getBlankPlayerHead();
                SkullMeta meta1 = (SkullMeta) playerhead1.getItemMeta();
                SkyWarsReloaded.getNMS().updateSkull(meta1, alivePlayer);
                meta1.setDisplayName(ChatColor.YELLOW + alivePlayer.getName());
                List<String> lore = new ArrayList<>();
                lore.add(new Messaging.MessageFormatter().setVariable("player", alivePlayer.getName()).format("spectate.playeritemlore"));
                meta1.setLore(lore);
                playerhead1.setItemMeta(meta1);
                // Add item to spectator
                spectatorPlayer.getInventory().setItem(slot, playerhead1);
                // Next slot
                slot++;
            }
        }
    }

    public void updateStatsForLoser(Player player) {
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