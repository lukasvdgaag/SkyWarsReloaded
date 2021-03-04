package com.walrusone.skywarsreloaded.managers;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.database.DataStorage;
import com.walrusone.skywarsreloaded.enums.GameType;
import com.walrusone.skywarsreloaded.enums.MatchState;
import com.walrusone.skywarsreloaded.enums.PlayerRemoveReason;
import com.walrusone.skywarsreloaded.enums.ScoreVar;
import com.walrusone.skywarsreloaded.events.SkyWarsDeathEvent;
import com.walrusone.skywarsreloaded.events.SkyWarsKillEvent;
import com.walrusone.skywarsreloaded.events.SkyWarsLeaveEvent;
import com.walrusone.skywarsreloaded.events.SkyWarsWinEvent;
import com.walrusone.skywarsreloaded.game.*;
import com.walrusone.skywarsreloaded.game.cages.schematics.SchematicCage;
import com.walrusone.skywarsreloaded.matchevents.MatchEvent;
import com.walrusone.skywarsreloaded.menus.gameoptions.objects.CoordLoc;
import com.walrusone.skywarsreloaded.menus.gameoptions.objects.GameKit;
import com.walrusone.skywarsreloaded.menus.playeroptions.ParticleEffectOption;
import com.walrusone.skywarsreloaded.menus.playeroptions.WinSoundOption;
import com.walrusone.skywarsreloaded.menus.playeroptions.objects.ParticleEffect;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.Party;
import com.walrusone.skywarsreloaded.utilities.Util;
import com.walrusone.skywarsreloaded.utilities.VaultUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.logging.Level;

public class MatchManager {

    private static MatchManager instance = null;

    private int waitTime;
    private int gameTime;
    //private String debugName;
    private boolean debug;

    public MatchManager() {
        debug = SkyWarsReloaded.getCfg().debugEnabled();
        instance = this;
    }

    public static MatchManager get() {
        if (MatchManager.instance == null) {
            MatchManager.instance = new MatchManager();
        }
        return MatchManager.instance;
    }

    /**
     * Include online player into game
     * @param player The player to add
     * @param type Type of game (teams, single, etc..)
     * @return Was successfully joined
     */
    public boolean joinGame(Player player, GameType type) {
        GameMap.shuffle();
        GameMap map = null;
        int highest = -1;
        ArrayList<GameMap> games = GameMap.getPlayableArenas(type);
        boolean wasJoined = false;

        Collections.shuffle((games));

        for (final GameMap gameMap : games) {
            if (SkyWarsReloaded.getCfg().debugEnabled())
                Bukkit.getLogger().log(Level.WARNING, "#joinGame: --game: " + gameMap.getName());
            if (gameMap.canAddPlayer() && gameMap.getPlayerCount() > highest ) {
                if (SkyWarsReloaded.getCfg().debugEnabled()) {
                    Bukkit.getLogger().log(Level.WARNING, "#joinGame: canAddPlayer: " + gameMap.canAddPlayer());
                    Bukkit.getLogger().log(Level.WARNING, "#joinGame: playerCount: " + gameMap.getPlayerCount());
                    Bukkit.getLogger().log(Level.WARNING, "#joinGame: highest: " + highest);
                }
                map = gameMap;
                highest = gameMap.getPlayerCount();
            }
        }

        if (map == null) {
            if (SkyWarsReloaded.getCfg().debugEnabled()) {
                Bukkit.getLogger().log(Level.WARNING, "#joinGame: --map = null:");
            }
            // Allow joining as spec if you have permission
            if (player.hasPermission("sw.admin.joinBypass")) { // TODO: Not fully tested, issues may arise
                if (games.size() > 0) {
                    SkyWarsReloaded.get().getPlayerManager().addSpectator(games.get(0), player);
                    wasJoined = true;
                }
            }
            // else wasJoined is still false
        } else {
            wasJoined = map.addPlayers(null, player);
        }
        return wasJoined;
    }

    /**
     * Add all players from party into game
     * @param party The party
     * @param type The game type
     * @return Was successfully joined
     */
    public boolean joinGame(Party party, GameType type) {
        GameMap.shuffle();
        GameMap map = null;
        int highest = 0;
        ArrayList<GameMap> games;
        if (type == GameType.ALL) {
            games = GameMap.getPlayableArenas(GameType.ALL);
        } else if (type == GameType.SINGLE) {
            games = GameMap.getPlayableArenas(GameType.SINGLE);
        } else {
            games = GameMap.getPlayableArenas(GameType.TEAM);
        }

        Collections.shuffle((games));

        for (final GameMap gameMap : games) {
            if (gameMap.canAddParty(party) && highest <= gameMap.getPlayerCount()) {
                map = gameMap;
                highest = gameMap.getPlayerCount();
            }
        }
        boolean joined = false;
        if (map != null) {
            joined = map.addPlayers(null, party);
        }
        return joined;
    }

    public void start(final GameMap gameMap) {
        debug = SkyWarsReloaded.getCfg().debugEnabled();
        if (gameMap == null) {
            return;
        }
        gameMap.removeDMSpawnBlocks();
        this.setWaitTime(SkyWarsReloaded.getCfg().getWaitTimer());
        this.setGameTime();
        if (gameMap.getTeamSize() == 1) {
            gameMap.setMatchState(MatchState.WAITINGSTART);
        } else {
            gameMap.setMatchState(MatchState.WAITINGLOBBY);
        }
        gameMap.update();
        gameMap.getGameBoard().updateScoreboard();
        this.waitStart(gameMap);
    }


    public void message(final GameMap gameMap, final String message) {
        this.message(gameMap, message, null);
    }

    public void message(@NotNull final GameMap gameMap, final String message, @Nullable Player skip) {
        List<Player> worldPlayers = gameMap.getCurrentWorld().getPlayers();
        if (worldPlayers != null && !worldPlayers.isEmpty()) {
            for (Player player : worldPlayers) {
                if (player != null && player != skip) {
                    player.sendMessage(message);
                }
            }
        }
    }

    public void teleportToArena(final GameMap gameMap, PlayerCard pCard) {
        if (pCard.getPlayer() == null || (!gameMap.getMatchState().equals(MatchState.WAITINGLOBBY) && !gameMap.getMatchState().equals(MatchState.WAITINGSTART)) ||
                (gameMap.getMatchState().equals(MatchState.WAITINGSTART) && pCard.getTeamCard().getSpawns() == null)) {
            pCard.reset();
            return;
        }

        Player player = pCard.getPlayer();
        if (PlayerData.getPlayerData(player.getUniqueId()) == null) {
            PlayerData.getAllPlayerData().add(new PlayerData(player));
        }
        World world = gameMap.getCurrentWorld();
        Location spawn;

        if (gameMap.getMatchState().equals(MatchState.WAITINGLOBBY)) {
            CoordLoc lobbySpawn = gameMap.getWaitingLobbySpawn();
            spawn = new Location(world, lobbySpawn.getX() + 0.5, lobbySpawn.getY() + 1, lobbySpawn.getZ() + 0.5);

            if (debug) {
                Util.get().logToFile(getDebugName(gameMap) + ChatColor.YELLOW + "Teleporting " + player.getName() + " to the waiting lobby on map " + gameMap.getName());
            }

            if (!world.isChunkLoaded(world.getChunkAt(spawn))) {
                world.loadChunk(world.getChunkAt(spawn));
            }
        } else {
            CoordLoc sspawn = pCard.getSpawn();
            if (debug) {
                Util.get().logToFile(getDebugName(gameMap) + ChatColor.YELLOW + "Teleporting " + player.getName() + " to Skywars on map" + gameMap.getName());
            }
            spawn = new Location(world, sspawn.getX() + 0.5, sspawn.getY() + 1, sspawn.getZ() + 0.5);
            PlayerStat pStat = PlayerStat.getPlayerStats(player);
            if (pStat != null &&
                pStat.getGlassColor() != null &&
                pStat.getGlassColor().startsWith("custom-")
            ) {
                spawn = new Location(world, sspawn.getX() + 0.5, sspawn.getY() + 0.25, sspawn.getZ() + 0.5);
            }
            //Location newSpawn = new Location(world, spawn.getX() + 0.5, spawn.getY() + 0.25, spawn.getZ() + 0.5);
        }

        player.teleport(spawn, TeleportCause.END_PORTAL);


        if (SkyWarsReloaded.getCfg().getLookDirectionEnabled() && gameMap.getMatchState().equals(MatchState.WAITINGSTART)) {
            if (gameMap.getCurrentWorld() == player.getWorld()) {
                CoordLoc a = gameMap.getLookDirection();
                Location b = new Location(gameMap.getCurrentWorld(), a.getX(), a.getY(), a.getZ());
                Vector v = b.clone().subtract(player.getEyeLocation()).toVector();
                Location l = player.getLocation().setDirection(v);
                player.teleport(l, TeleportCause.END_PORTAL);
            }
        }

        player.setGameMode(GameMode.ADVENTURE);
        player.setAllowFlight(true);
        player.setFlying(true);
        player.setFlySpeed(0f);
        new BukkitRunnable() {
            @Override
            public void run() {
                preparePlayer(player, gameMap);
            }
        }.runTaskLater(SkyWarsReloaded.get(), 5);
        new BukkitRunnable() {
            @Override
            public void run() {
                player.setFlying(false);
                player.setAllowFlight(false);
                player.setFlySpeed(0.1f);
            }
        }.runTaskLater(SkyWarsReloaded.get(), 40);
        PlayerStat ps = PlayerStat.getPlayerStats(player.getUniqueId());
        if (ps != null) {
            String key = ps.getParticleEffect();
            ParticleEffectOption effect = (ParticleEffectOption) ParticleEffectOption.getPlayerOptionByKey(key);
            if (effect != null) {
                List<ParticleEffect> effects = effect.getEffects();
                SkyWarsReloaded.getOM().addPlayer(player.getUniqueId(), effects);
            }
        }
        Util.get().clear(player);
        if (!gameMap.getAlivePlayers().contains(player) || gameMap.getTeamSize() == 1) {
            for (final Player p : gameMap.getAllPlayers()) {
                p.sendMessage(new Messaging.MessageFormatter().setVariable("player", player.getDisplayName())
                        .setVariable("players", "" + gameMap.getAllPlayers().size())
                        .setVariable("playercount", gameMap.getAllPlayers().size() + "")
                        .setVariable("maxplayers", "" + gameMap.getMaxPlayers()).format("game.waitstart-joined-the-game"));
            }
        }

        for (final Player p : gameMap.getAlivePlayers()) {
            if (!p.equals(player)) {
                Util.get().playSound(p, p.getLocation(), SkyWarsReloaded.getCfg().getJoinSound(), 1, 1);
            }
        }

        if (debug) {
            if (gameMap.getAlivePlayers().size() < gameMap.getMinTeams()) {
                Util.get().logToFile(getDebugName(gameMap) + ChatColor.YELLOW + "Waiting for More Players on map " + gameMap.getName());
            } else {
                Util.get().logToFile(getDebugName(gameMap) + ChatColor.YELLOW + "Starting Countdown for SkyWars Match on map " + gameMap.getName());
            }
        }

        gameMap.setMatchState(gameMap.getMatchState());
        if (SkyWarsReloaded.getCfg().titlesEnabled()) {
            String subtitle = new Messaging.MessageFormatter()
                    .setVariable("map", gameMap.getName())
                    .setVariable("designer", gameMap.getDesigner())
                    .setVariable("creator", gameMap.getDesigner())
                    .format("titles.join-subtitle");
            String maintitle = new Messaging.MessageFormatter()
                    .setVariable("map", gameMap.getDisplayName())
                    .setVariable("designer", gameMap.getDesigner())
                    .setVariable("creator", gameMap.getDesigner()).format("titles.join-title");


            Util.get().sendTitle(player, 15, 60, 15, maintitle,
                    subtitle);
        }
    }

    private void preparePlayer(Player player, GameMap gameMap) {
        if (debug) {
            Util.get().logToFile(getDebugName(gameMap) + ChatColor.YELLOW + "Preparing " + player.getName() + " for SkyWars");
        }
        player.setFoodLevel(20);
        player.setSaturation(20);
        player.setHealth(20.0);
        player.setExp(0.0f);
        player.setLevel(0);

        /*if (!SkyWarsReloaded.getNMS().removeFromScoreboardCollection(player.getScoreboard())) { //1.13+
            player.setScoreboard(SkyWarsReloaded.get().getServer().getScoreboardManager().getNewScoreboard());
        }*/
        gameMap.getGameBoard().updateScoreboard();

        Util.get().clear(player);
        player.getInventory().setBoots(new ItemStack(Material.AIR, 1));
        player.getInventory().setChestplate(new ItemStack(Material.AIR, 1));
        player.getInventory().setHelmet(new ItemStack(Material.AIR, 1));
        player.getInventory().setLeggings(new ItemStack(Material.AIR, 1));

        if (SkyWarsReloaded.getCfg().areKitsEnabled()) {
            ItemStack kitItem = SkyWarsReloaded.getIM().getItem("kitvote");
            player.getInventory().setItem(SkyWarsReloaded.getCfg().getKitVotePos(), kitItem);
        }

        if (SkyWarsReloaded.getCfg().votingEnabled()) {
            ItemStack timeItem = SkyWarsReloaded.getIM().getItem("votingItem");
            player.getInventory().setItem(SkyWarsReloaded.getCfg().getVotingPos(), timeItem);
        }

        if (gameMap.getTeamSize() > 1 && gameMap.getMatchState() == MatchState.WAITINGLOBBY) {
            ItemStack teamItem = SkyWarsReloaded.getIM().getItem("teamSelectItem");
            player.getInventory().setItem(SkyWarsReloaded.getCfg().getTeamSelectPos(), teamItem);
        }

        ItemStack exitItem = SkyWarsReloaded.getIM().getItem("exitGameItem");
        player.getInventory().setItem(SkyWarsReloaded.getCfg().getExitPos(), exitItem);

        if (debug) {
            Util.get().logToFile(getDebugName(gameMap) + ChatColor.YELLOW + "Finished Preparing " + player.getName() + " for SkyWars on map " + gameMap.getName());
        }
    }

    private void waitStart(final GameMap gameMap) {
        gameMap.setTimer(this.getWaitTime());
        new BukkitRunnable() {
            public void run() {

                // If we are not waiting for a game start (aka in game or ending) then cancel everything here
                if (gameMap.getMatchState() != MatchState.WAITINGSTART && gameMap.getMatchState() != MatchState.WAITINGLOBBY) {
                    this.cancel();
                    return;
                }

                if (gameMap.getMatchState().equals(MatchState.WAITINGSTART)) {
                    // if there is at least one player per team OR forcestart is triggered while at least one player is present
                    if (gameMap.getAllPlayers().size() >= gameMap.getMinTeams() || (gameMap.getForceStart() && gameMap.getAllPlayers().size() > 0)) {
                        if (gameMap.getTimer() <= 0) {
                            this.cancel();
                            for (final Player player : gameMap.getAlivePlayers()) {
                                Util.get().playSound(player, player.getLocation(), SkyWarsReloaded.getCfg().getCountdownSound(), 1, 1F);
                            }
                            MatchManager.this.startMatch(gameMap);
                        } else {
                            if (gameMap.getTimer() <= 5 && gameMap.getMatchState() != MatchState.ENDING) {
                                for (final Player player : gameMap.getAlivePlayers()) {
                                    if (SkyWarsReloaded.getCfg().titlesEnabled()) {
                                        Util.get().sendTitle(player, 2, 20, 2, new Messaging.MessageFormatter().
                                                        setVariable("time", "" + gameMap.getTimer()).format("titles.warmup-title"),
                                                new Messaging.MessageFormatter().format("titles.warmup-subtitle"));
                                    }
                                    if (gameMap.getTimer() == 5) {
                                        Util.get().playSound(player, player.getLocation(), SkyWarsReloaded.getCfg().getCountdownSound(), 1, 0.5F);
                                    } else if (gameMap.getTimer() == 4) {
                                        Util.get().playSound(player, player.getLocation(), SkyWarsReloaded.getCfg().getCountdownSound(), 1, 0.6F);
                                    } else if (gameMap.getTimer() == 3) {
                                        Util.get().playSound(player, player.getLocation(), SkyWarsReloaded.getCfg().getCountdownSound(), 1, 0.7F);
                                    } else if (gameMap.getTimer() == 2) {
                                        Util.get().playSound(player, player.getLocation(), SkyWarsReloaded.getCfg().getCountdownSound(), 1, 0.8F);
                                    } else if (gameMap.getTimer() == 1) {
                                        Util.get().playSound(player, player.getLocation(), SkyWarsReloaded.getCfg().getCountdownSound(), 1, 0.9F);
                                    }
                                }
                            }
                            // Announce every 5 seconds OR every second if under or equal to 5
                            if (gameMap.getTimer() % 5 == 0 || gameMap.getTimer() <= 5) {
                                MatchManager.this.announceTimer(gameMap);
                            }
                        }
                        // Decrease the timer unless we are already at 0
                        gameMap.setTimer(gameMap.getTimer() > 0 ? gameMap.getTimer() - 1 : 0);
                    } else { // if not at least 1 player per team AND force start is not triggered
                        // Reset the timer
                        gameMap.setTimer(waitTime);
                    }
                } else { // If not in waitingstart state (aka are we in a lobby mode?)

                    // if there is at least one player per team OR forcestart is triggered while at least one player is present
                    if (gameMap.getAllPlayers().size() >= gameMap.getMinTeams() || (gameMap.getForceStart() && gameMap.getAllPlayers().size() > 0)) {
                        if (gameMap.getTimer() <= 0) {

                            // Team assigning
                            /*
                                TODO: this is broken - lowest should start at max per team and reduce
                                        only if it's actually lower.. there is no check for that rn. Also,
                                        given this setup it requires that there is already a lone player in
                                        the team before being able to add a nwe one.
                                    - - -
                                    Suggested setup:
                                        - build a list of unassigned players (aka your new minions xD),
                                        - build a ordered list of teams ranked from smallest to biggest
                                        - iterate once over to find the lowest number of players in a team,
                                        - then iterate over the teams again and take the last player in the list..
                                            (to avoid shifting many players in memory)
                                        - place each player into a team, then start back at the beginning when
                                            the existing players in a team is higher than previously recorded lowest
                                            and increase previous lowest to += 1
                            */
                            for (Player player : gameMap.getAllPlayers()) {
                                if (gameMap.getTeamCard(player) == null) {
                                    List<TeamCard> cards = gameMap.getTeamCards();
                                    Collections.shuffle(cards);
                                    int lowest = 0;
                                    for (TeamCard card : cards) {
                                        // If team has a "lone" player AND
                                        if (card.getFullCount() > 0 && card.getPlayersSize() <= lowest) {
                                            // Add player to team
                                            card.sendReservation(player, PlayerStat.getPlayerStats(player));
                                            break;
                                        }
                                        // TODO: broken AF, no check before setting lowest
                                        lowest = card.getPlayersSize();
                                    }
                                }
                            }

                            // Remove all players from waiting lobby state and set the game to waiting start (in cages mode)
                            gameMap.clearWaitingPlayers();
                            gameMap.setMatchState(MatchState.WAITINGSTART);
                            // Send all player tp their respective start cages
                            for (final Player player : gameMap.getAlivePlayers()) {
                                Util.get().playSound(player, player.getLocation(), SkyWarsReloaded.getCfg().getCountdownSound(), 1, 1F);

                                gameMap.getCage().removeSpawnHousing(gameMap, gameMap.getPlayerCard(player), false);
                                boolean b = new SchematicCage().createSpawnPlatform(gameMap, player);
                                if (!b) {
                                    gameMap.getCage().setGlassColor(gameMap, gameMap.getTeamCard(player));
                                }
                                // This just runs the teleport-to-cage operation
                                teleportToArena(gameMap, gameMap.getPlayerCard(player));
                            }
                            // Wait 15 seconds before starting the game
                            gameMap.setTimer(15); // todo make this editable
                        } else { // if (gameMap.getTimer() <= 0)
                            gameMap.setTimer(gameMap.getTimer() - 1);
                        }
                    } else { // if not at least 1 player per team AND force start is not triggered
                        gameMap.setTimer(waitTime);
                    }
                }
            }
        }.runTaskTimer(SkyWarsReloaded.get(), 0L, 20L);
    }

    public void forceStart(Player player) {
        GameMap gameMap = this.getPlayerMap(player);
        gameMap.setForceStart(true);
    }

    private void startMatch(final GameMap gameMap) {
        if (debug) {
            Util.get().logToFile(getDebugName(gameMap) + ChatColor.YELLOW + "Starting SkyWars Match");
        }
        for (Player player : gameMap.getAlivePlayers()) {
            player.closeInventory();
            player.getInventory().clear();
            player.setGameMode(GameMode.SURVIVAL);
            if (SkyWarsReloaded.getCfg().titlesEnabled()) {
                Util.get().sendTitle(player, 5, 60, 5, new Messaging.MessageFormatter().setVariable("map", gameMap.getDisplayName()).format("titles.start-title"),
                        new Messaging.MessageFormatter().setVariable("map", gameMap.getDisplayName()).format("titles.start-subtitle"));
            }
        }
        if (gameMap.getMatchState() != MatchState.ENDING) {
            this.matchCountdown(gameMap);
        }
        gameMap.getChestOption().completeOption();
        if (SkyWarsReloaded.getCfg().isTimeVoteEnabled()) {
            gameMap.getTimeOption().completeOption();
        }
        if (SkyWarsReloaded.getCfg().isWeatherVoteEnabled()) {
            gameMap.getWeatherOption().completeOption();
        }
        if (SkyWarsReloaded.getCfg().isModifierVoteEnabled()) {
            gameMap.getModifierOption().completeOption();
        }
        if (SkyWarsReloaded.getCfg().isHealthVoteEnabled()) {
            gameMap.getHealthOption().completeOption();
        }
        selectKit(gameMap);
        gameMap.getCage().removeSpawnHousing(gameMap);


        if (SkyWarsReloaded.getCfg().getEnablePVPTimer() && SkyWarsReloaded.getCfg().getPVPTimerTime() >= 1) {
            gameMap.setDisableDamage(true);
            Bukkit.getScheduler().scheduleSyncDelayedTask(SkyWarsReloaded.get(), () -> {
                gameMap.setDisableDamage(false);
                for (Player player : gameMap.getAlivePlayers()) {
                    if (!SkyWarsReloaded.getMessaging().getFile().getString("game.pvp-timer-disabled-message").isEmpty()) {
                        player.sendMessage(new Messaging.MessageFormatter().setVariable("player", player.getName()).setVariable("arena", gameMap.getName()).format("game.pvp-timer-disabled-message"));
                    }
                    if (!SkyWarsReloaded.getMessaging().getFile().getString("game.pvp-timer-disabled-title").isEmpty()) {
                        String[] lines = new Messaging.MessageFormatter().setVariable("player", player.getName()).setVariable("arena", gameMap.getName()).format("game.pvp-timer-disabled-title").split("\\\\n");
                        if (lines.length == 1) {
                            SkyWarsReloaded.getNMS().sendTitle(player, 20, 50, 20, lines[0], "");
                        } else {
                            SkyWarsReloaded.getNMS().sendTitle(player, 20, 50, 20, lines[0], lines[1]);
                        }
                    }
                }
            }, 20L * SkyWarsReloaded.getCfg().getPVPTimerTime());
        }
    }

    private void selectKit(GameMap gameMap) {
        if (SkyWarsReloaded.getCfg().kitVotingEnabled()) {
            gameMap.getKitVoteOption().getVotedKit();
            for (final Player player : gameMap.getAlivePlayers()) {
                GameKit.giveKit(player, gameMap.getKit());
            }
        } else {
            for (final Player player : gameMap.getAlivePlayers()) {
                GameKit.giveKit(player, gameMap.getSelectedKit(player));
            }
        }
    }

    private void matchCountdown(final GameMap gameMap) {
        if (gameMap.getMatchState() == MatchState.ENDING) {
            return;
        }
        gameMap.setMatchState(MatchState.PLAYING);
        gameMap.getGameBoard().updateScoreboard();
        gameMap.update();
        gameMap.setTimer(this.getGameTime());
        new BukkitRunnable() {
            public void run() {
                if (gameMap.getMatchState() == MatchState.ENDING) {
                    this.cancel();
                } else {
                    for (MatchEvent event : gameMap.getEvents()) {
                        if (event.willFire() && !event.fired()) {
                            if (event.getStartTime() <= gameMap.getTimer()) {
                                event.doEvent();
                            } else {
                                if (event.announceEnabled()) {
                                    event.announceTimer();
                                }
                            }
                        }
                    }
                }
                if (gameMap.isThunder()) {
                    if (gameMap.getStrikeCounter() == gameMap.getNextStrike()) {
                        World mapWorld = gameMap.getCurrentWorld();
                        int hitPlayer = new Random().nextInt(100);
                        if (hitPlayer <= 10) {
                            int size = gameMap.getAlivePlayers().size();
                            Player player = gameMap.getAlivePlayers().get(new Random().nextInt(size));
                            mapWorld.strikeLightning(player.getLocation());
                        } else {
                            int x = Util.get().getRandomNum(-150, 150);
                            int z = Util.get().getRandomNum(-150, 150);
                            int y = Util.get().getRandomNum(20, 50);
                            mapWorld.strikeLightningEffect(new Location(mapWorld, x, y, z));
                        }
                        gameMap.setNextStrike(Util.get().getRandomNum(3, 20));
                        gameMap.setStrikeCounter(0);
                    } else {
                        gameMap.setStrikeCounter(gameMap.getStrikeCounter() + 1);
                    }
                }
                gameMap.setTimer(gameMap.getTimer() + 1);
                gameMap.getGameBoard().updateScoreboardVar(ScoreVar.TIME);
            }
        }.runTaskTimer(SkyWarsReloaded.get(), 0L, 20L);
    }

    private void won(final GameMap gameMap, final TeamCard winners) {
        if (winners != null) {
            if (debug) {
                Util.get().logToFile(getDebugName(gameMap) + ChatColor.YELLOW + winners.getTeamName() + "Won the Match");
            }

            for (PlayerCard winner : winners.getPlayerCards()) {
                if (winner != null && winner.getPlayer() != null) {
                    gameMap.addWinner(winner.getPlayer().getName());
                }
            }
            final String winner = SkyWarsReloaded.getCfg().usePlayerNames() ? winners.getPlayerNames() : winners.getTeamName();
            final String map = gameMap.getDisplayName();

            // Make sure winners are placed #1
            winners.setPlace(1);
            for (PlayerCard pCard : winners.getPlayerCards()) {
                Player pWinner = pCard.getPlayer();

                if (pWinner != null) {
                    final PlayerStat winnerData = PlayerStat.getPlayerStats(pWinner.getUniqueId().toString());
                    if (winnerData != null) {
                        winnerData.setWins(winnerData.getWins() + 1);
                        final int multiplier = Util.get().getMultiplier(pWinner);
                        winnerData.setXp(winnerData.getXp() + (multiplier * SkyWarsReloaded.getCfg().getWinnerXP()));
                        if (SkyWarsReloaded.getCfg().economyEnabled()) {
                            VaultUtils.get().give(pWinner, multiplier * SkyWarsReloaded.getCfg().getWinnerEco());
                        }
                        WinSoundOption sound = (WinSoundOption) WinSoundOption.getPlayerOptionByKey(winnerData.getWinSound());
                        if (sound != null) {
                            sound.playSound(pWinner.getLocation());
                        }

                        Util.get().sendActionBar(pWinner, new Messaging.MessageFormatter().setVariable("xp", "" + multiplier * SkyWarsReloaded.getCfg().getWinnerXP()).format("game.win-actionbar"));
                        Util.get().doCommands(SkyWarsReloaded.getCfg().getWinCommands(), pWinner);
                        if (SkyWarsReloaded.getCfg().getEnableFlightOnWin()) {
                            pWinner.setAllowFlight(true);
                            pWinner.setFlying(true);
                        }
                        if (SkyWarsReloaded.getCfg().getClearInventoryOnWin()) {
                            pWinner.getInventory().clear();
                        }
                        Bukkit.getPluginManager().callEvent(new SkyWarsWinEvent(winnerData, gameMap));
                    }

                    if (SkyWarsReloaded.getCfg().enableWinMessage()) {
                        SkyWarsReloaded.get().getServer().broadcastMessage(new Messaging.MessageFormatter()
                                .setVariable("player1", winner).setVariable("map", map).format("game.broadcast-win"));
                    }
                    if (SkyWarsReloaded.getCfg().titlesEnabled()) {
                        Util.get().sendTitle(pWinner, 5, 80, 5, new Messaging.MessageFormatter().format("titles.endgame-title-won"), new Messaging.MessageFormatter().format("titles.endgame-subtitle-won"));
                    }
                    if (SkyWarsReloaded.getCfg().fireworksEnabled()) {
                        Util.get().fireworks(pWinner, 5, SkyWarsReloaded.getCfg().getFireWorksPer5Tick());
                    }
                    if (SkyWarsReloaded.getCfg().particlesEnabled()) {
                        List<String> particles = new ArrayList<>();
                        particles.add("FIREWORKS_SPARK");
                        Util.get().surroundParticles(pWinner, 1, particles, 8, 0);
                    }
                    pWinner.sendMessage(new Messaging.MessageFormatter()
                            .setVariable("map", gameMap.getName()).format("game.won"));
                }
            }

        }
        if (gameMap.getMatchState() != MatchState.OFFLINE) {
            gameMap.setMatchState(MatchState.ENDING);
            gameMap.getGameBoard().updateScoreboard();
            for (MatchEvent mEvent : gameMap.getEvents()) {
                if (mEvent.hasFired()) {
                    mEvent.endEvent(true);
                }
            }
        }
        this.endGame(gameMap);
    }

    private void endGame(final GameMap gameMap) {
        if (debug) {
            Util.get().logToFile(getDebugName(gameMap) + ChatColor.YELLOW + "SkyWars Match Has Ended - Waiting for teleport");
        }
        gameMap.update();
        gameMap.setTimer(0);
        if (SkyWarsReloaded.get().isEnabled() && !gameMap.getMatchState().equals(MatchState.OFFLINE)) {
            // Save all player stats
            for (final Player player : gameMap.getAllPlayers()) {
                new BukkitRunnable() {
                    public void run() {
                        PlayerStat toSave = PlayerStat.getPlayerStats(player.getUniqueId().toString());
                        if (toSave != null) {
                            DataStorage.get().saveStats(toSave);
                        }
                    }
                }.runTaskAsynchronously(SkyWarsReloaded.get());
            }
            // Clear all players
            new BukkitRunnable() {
                public void run() {
                    // Clear Spectators
                    for (final UUID uuid : gameMap.getSpectators()) {
                        MatchManager.this.removeSpectatorByUuid(uuid);
                    }
                    gameMap.getSpectators().clear();
                    // Clear Alive players
                    for (final Player player : gameMap.getAlivePlayers()) {
                        if (player != null) {
                            if (PlayerData.getPlayerData(player.getUniqueId()) != null) {
                                PlayerData pd = PlayerData.getPlayerData(player.getUniqueId());
                                if (pd != null) {
                                    pd.setTaggedBy(null);
                                }
                            }
                            SkyWarsReloaded.get().getPlayerManager().removePlayer(player, PlayerRemoveReason.PLAYER_QUIT_GAME, null, false);
                            // MatchManager.this.removeAlivePlayer(player, DamageCause.CUSTOM, true, true);
                        }
                    }
                }
            }.runTaskLater(SkyWarsReloaded.get(), SkyWarsReloaded.getCfg().getTimeAfterMatch() * 20L);
            // Refresh map 5s after clearing all players in case some plugins do bad things
            new BukkitRunnable() {
                public void run() {
                    if (SkyWarsReloaded.getCfg().bungeeMode()) {
                        Util.get().doCommands(SkyWarsReloaded.getCfg().getGameEndCommands(), null);
                    }
                    gameMap.refreshMap();
                    if (debug) {
                        Util.get().logToFile(getDebugName(gameMap) + ChatColor.YELLOW + "SkyWars Match Has Ended - Anena has been refreshed");
                    }
                }
            }.runTaskLater(SkyWarsReloaded.get(), (SkyWarsReloaded.getCfg().getTimeAfterMatch() + 5) * 20L);
        }
    }

    public void removeSpectatorByUuid(UUID uuid) {
        final Player player = SkyWarsReloaded.get().getServer().getPlayer(uuid);
        if (player != null) {
            removeSpectator(player);
        }
    }

    public void removeSpectator(Player player) {
        PlayerData pData = PlayerData.getPlayerData(player.getUniqueId());
        GameMap gameMap = getPlayerMap(player);
        if (debug) {
            Util.get().logToFile(getDebugName(gameMap) + ChatColor.YELLOW + player.getName() + " has been removed from spectators");
        }
        if (pData != null) {
            pData.restoreToBeforeGameState(false);
            PlayerData.getAllPlayerData().remove(pData);
        }
    }

    /*@Deprecated
    public void removeAlivePlayer(final Player player, DamageCause dCause, final boolean isPlayerLeft, boolean sendMessages) {
        // TODO: DEBUG ====== THIS MUST BE REMOVED ONCE Triple Deaths cause is found
        if (SkyWarsReloaded.getCfg().debugEnabled()) {
            try {
                throw new Exception("This is NORMAL!! This is useful data to know from where the death is being called if you are having triple death issues.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        SkyWarsReloaded.getOM().removePlayer(player.getUniqueId());
        UUID playerUuid = player.getUniqueId();

        final GameMap gameMap = this.getPlayerMap(player);
        if (gameMap == null) {
            return;
        }
        if (gameMap.getMatchState() == MatchState.PLAYING) {
            gameMap.getTeamCard(player).getDead().add(player.getUniqueId());

            PlayerCard pCard = gameMap.getPlayerCard(player);
            pCard.getTeamCard().setPlace(gameMap.getTeamCards().size() + 1 - gameMap.getNumTeamsOut());
            player.setNoDamageTicks(1);
            final PlayerData playerData = PlayerData.getPlayerData(player.getUniqueId());
            if (playerData != null) {
                if (isPlayerLeft) {
                    if (playerData.getTaggedBy() != null && playerData.getTaggedBy().getPlayer() != null && playerData.getTaggedBy().getPlayer() != player && System.currentTimeMillis() - playerData.getTaggedBy().getTime() < 10000) {
                        if (sendMessages) {
                            this.message(gameMap, new Messaging.MessageFormatter()
                                    .withPrefix()
                                    .setVariable("player", player.getName())
                                    .setVariable("killer", playerData.getTaggedBy().getPlayer().getName())
                                    .format("game.death.quit-while-tagged"), null);
                            SkyWarsReloaded.get().getPlayerManager().updateStatsForLoser(player);
                            SkyWarsReloaded.get().getPlayerManager().updateStatsForKiller(playerData.getTaggedBy().getPlayer());
                        }
                        Bukkit.getPluginManager().callEvent(new SkyWarsKillEvent(playerData.getTaggedBy().getPlayer(), player, gameMap));
                        gameMap.increaseDisplayedKillsVar(playerData.getTaggedBy().getPlayer());
                    } else {
                        if (sendMessages) {
                            this.message(gameMap, new Messaging.MessageFormatter().setVariable("player", player.getName()).format("game.left-the-game"), player);
                        }
                        Bukkit.getPluginManager().callEvent(new SkyWarsLeaveEvent(player, gameMap));
                    }

                    playerData.restoreToBeforeGameState(true);
                    gameMap.removePlayer(player.getUniqueId());
                } else {
                    if (debug) {
                        Util.get().logToFile(getDebugName(gameMap) + ChatColor.YELLOW + player.getName() + " died. Respawning.");
                    }
                    if (sendMessages) {
                        if (playerData.getTaggedBy() != null && System.currentTimeMillis() - playerData.getTaggedBy().getTime() < 10000) {
                            this.message(gameMap, Util.get().getDeathMessage(dCause, true, player, playerData.getTaggedBy().getPlayer()), null);
                            Bukkit.getPluginManager().callEvent(new SkyWarsKillEvent(playerData.getTaggedBy().getPlayer(), player, gameMap));
                            gameMap.increaseDisplayedKillsVar(playerData.getTaggedBy().getPlayer());
                            SkyWarsReloaded.get().getPlayerManager().updateStatsForLoser(player);
                            SkyWarsReloaded.get().getPlayerManager().updateStatsForKiller(playerData.getTaggedBy().getPlayer());
                        } else {
                            this.message(gameMap, Util.get().getDeathMessage(dCause, false, player, player), null);
                            PlayerStat loserData = PlayerStat.getPlayerStats(player.getUniqueId().toString());
                            if (loserData != null) {
                                loserData.setDeaths(loserData.getDeaths() + 1);
                            }
                        }
                    }
                    Bukkit.getPluginManager().callEvent(new SkyWarsDeathEvent(player, dCause, gameMap));

                    new BukkitRunnable() {
                        public void run() {
                            player.sendMessage(new Messaging.MessageFormatter()
                                    .setVariable("arena", gameMap.getDisplayName())
                                    .setVariable("map", gameMap.getName()).format("game.lost"));
                        }
                    }.runTaskLater(SkyWarsReloaded.get(), 10L);
                }

                if (sendMessages) {
                    if (pCard.getTeamCard().isElmininated()) {
                        for (PlayerCard card : pCard.getTeamCard().getPlayerCards()) {
                            if (card.getPlayer() != null) {
                                PlayerStat loserData = PlayerStat.getPlayerStats(card.getPlayer().getUniqueId().toString());
                                if (loserData != null) {
                                    loserData.setLosts(loserData.getLosses() + 1);
                                }
                            }
                        }
                    }

                    if (gameMap.getTeamsLeft() <= 1) {
                        if (gameMap.getTeamsLeft() == 1) {
                            this.won(gameMap, gameMap.getWinningTeam());
                        } else {
                            this.won(gameMap, null);
                        }
                    }
                }
            }
            for (UUID uuid : gameMap.getSpectators()) {
                if (!uuid.equals(player.getUniqueId())) {
                    Player spec = SkyWarsReloaded.get().getServer().getPlayer(uuid);
                    SkyWarsReloaded.get().getPlayerManager().prepareSpectateInv(spec, gameMap);
                }
            }
        } else {
            if (gameMap.getMatchState() == MatchState.WAITINGSTART || gameMap.getMatchState() == MatchState.WAITINGLOBBY) {
                PlayerStat ps = PlayerStat.getPlayerStats(player);
                if (ps != null && isPlayerLeft) {
                    String cageName = ps.getGlassColor();
                    if (gameMap.getTeamSize() == 1 || SkyWarsReloaded.getCfg().isUseSeparateCages()) {
                        if (cageName.startsWith("custom-")) {
                            new SchematicCage().removeSpawnPlatform(gameMap, player);
                        } else {
                            gameMap.getCage().removeSpawnHousing(gameMap, gameMap.getTeamCard(player), false);
                        }
                    }
                }
            }

            PlayerStat.resetScoreboard(player);
            gameMap.removePlayer(playerUuid);
            gameMap.removeWaitingPlayer(playerUuid);
            Bukkit.getPluginManager().callEvent(new SkyWarsLeaveEvent(player, gameMap));
            for (SWRSign sign : gameMap.getSigns()) {
                sign.update();
            }

            if (gameMap.getMatchState() != MatchState.ENDING) {
                if (SkyWarsReloaded.getCfg().titlesEnabled()) {
                    for (final Player p : gameMap.getAlivePlayers()) {
                        if (!p.equals(player)) {
                            Util.get().sendTitle(p, 2, 20, 2, "",
                                    new Messaging.MessageFormatter().setVariable("player", player.getDisplayName())
                                            .setVariable("players", "" + gameMap.getPlayerCount())
                                            .setVariable("playercount", "" + gameMap.getPlayerCount())
                                            .setVariable("maxplayers", "" + gameMap.getMaxPlayers()).format("game.left-the-game"));
                        }
                    }
                }
                message(gameMap, new Messaging.MessageFormatter().setVariable("player", player.getDisplayName())
                        .setVariable("players", "" + gameMap.getPlayerCount())
                        .setVariable("playercount", "" + gameMap.getPlayerCount())
                        .setVariable("maxplayers", "" + gameMap.getMaxPlayers()).format("game.waitstart-left-the-game"), player);
            }

            for (final Player p : gameMap.getAlivePlayers()) {
                Util.get().playSound(p, p.getLocation(), SkyWarsReloaded.getCfg().getLeaveSound(), 1, 1);
            }

            final PlayerData playerData = PlayerData.getPlayerData(player.getUniqueId());
            if (playerData != null) {
                playerData.restoreToBeforeGameState(true);
                PlayerData.getAllPlayerData().remove(playerData);
            }

        }
        if (debug) {
            Util.get().logToFile(getDebugName(gameMap) + ChatColor.YELLOW + player.getName() + " Has Left The SkyWars Match on map" + gameMap.getName());
        }
    }*/

    public void checkForWin(GameMap gameMap) {
        if (gameMap.getTeamsLeft() <= 1) {
            if (gameMap.getTeamsLeft() == 1) {
                this.won(gameMap, gameMap.getWinningTeam());
            } else {
                this.won(gameMap, null);
            }
        }
    }

    public GameMap getPlayerMap(final Player v0) {
        if (v0 != null) {
            for (final GameMap gameMap : GameMap.getMaps()) {
                if (gameMap.getAllPlayers().contains(v0)) return gameMap;
                else if (gameMap.getWaitingPlayers().contains(v0.getUniqueId())) return gameMap;
                else if (gameMap.getSpectators().contains(v0.getUniqueId())) return gameMap;
            }
        }
        return null;
    }

    public GameMap getDeadPlayerMap(final Player v0) {
        if (v0 != null) {
            for (final GameMap gameMap : GameMap.getMaps()) {
                if (gameMap.mapContainsDead(v0.getUniqueId())) {
                    return gameMap;
                }
            }
        }
        return null;
    }

    public GameMap getSpectatorMap(final Player player) {
        UUID uuid = null;
        if (player != null) {
            uuid = player.getUniqueId();
        }

        if (uuid != null) {
            for (final GameMap gameMap : GameMap.getMaps()) {
                for (final UUID id : gameMap.getSpectators()) {
                    if (uuid.equals(id)) {
                        return gameMap;
                    }
                }
            }
        }
        return null;
    }

    public boolean isSpectating(final Player player) {
        return this.getSpectatorMap(player) != null;
    }

    private int getGameTime() {
        return gameTime;
    }

    private int getWaitTime() {
        return waitTime;
    }

    private void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    private void setGameTime() {
        this.gameTime = 0;
    }

    // UTILS

    public String getDebugName(GameMap gameMap) {
        return ChatColor.RED + "SWR[" + (gameMap != null ? gameMap.getName() : "null") + "] ";
    }

    private void announceTimer(final GameMap gameMap) {
        final int v1 = gameMap.getTimer();
        String time;
        if (v1 % 60 == 0) {
            time = v1 / 60 + " " + ((v1 > 60) ? new Messaging.MessageFormatter().format("timer.minutes") : new Messaging.MessageFormatter().format("timer.minute"));
        } else {
            if (v1 >= 60 || (v1 % 10 != 0 && v1 >= 10) || v1 <= 0) {
                return;
            }
            time = v1 + " " + ((v1 > 1) ? new Messaging.MessageFormatter().format("timer.seconds") : new Messaging.MessageFormatter().format("timer.second"));
        }
        this.message(gameMap, new Messaging.MessageFormatter().setVariable("time", time).format("timer.wait-timer"), null);
    }
}