package com.walrusone.skywarsreloaded.managers;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.database.DataStorage;
import com.walrusone.skywarsreloaded.enums.GameType;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.Util;
import com.walrusone.skywarsreloaded.utilities.VaultUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class PlayerStat {

    private static ArrayList<PlayerStat> players;
    private static HashMap<Player, Scoreboard> scoreboards = new HashMap<>();

    static {
        PlayerStat.players = new ArrayList<>();
    }

    private final String uuid;
    private String playername;
    private int wins;
    private int losts;
    private int kills;
    private int deaths;
    private int elo;
    private int xp;
    private String particleEffect;
    private String projectileEffect;
    private String glassColor;
    private String killSound;
    private String winSound;
    private String taunt;
    private boolean initialized;
    private PermissionAttachment perms;

    public PlayerStat(final Player player) {
        this.initialized = false;
        this.uuid = player.getUniqueId().toString();
        this.playername = player.getName();
        this.perms = player.addAttachment(SkyWarsReloaded.get());
        DataStorage.get().loadStats(this);
        if (SkyWarsReloaded.getCfg().economyEnabled()) {
            DataStorage.get().loadperms(this);
        }
        if (SkyWarsReloaded.getCfg().getSpawn() != null) {
            if (player.getWorld().equals(SkyWarsReloaded.getCfg().getSpawn().getWorld())) {
                updatePlayer(uuid);
            }
        }
        saveStats(uuid);
    }

    public static void updatePlayer(final String uuid) {
        new BukkitRunnable() {
            public void run() {
                PlayerStat ps = PlayerStat.getPlayerStats(uuid);
                if (ps == null) {
                    this.cancel();
                } else if (ps.isInitialized()) {
                    final Player player = SkyWarsReloaded.get().getServer().getPlayer(UUID.fromString(uuid));
                    if (player != null) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (Util.get().isSpawnWorld(player.getWorld())) {
                                    if (SkyWarsReloaded.getCfg().protectLobby()) {
                                        player.setGameMode(GameMode.ADVENTURE);
                                        player.setHealth(20);
                                        player.setFoodLevel(20);
                                        player.setSaturation(20);
                                        player.setFireTicks(0);
                                        player.resetPlayerTime();
                                        player.resetPlayerWeather();
                                    }
                                    PlayerStat pStats = PlayerStat.getPlayerStats(player);
                                    if (pStats != null && SkyWarsReloaded.getCfg().displayPlayerExeperience()) {
                                        Util.get().setPlayerExperience(player, pStats.getXp());
                                    }
                                    if (SkyWarsReloaded.get().isEnabled() && SkyWarsReloaded.getCfg().lobbyBoardEnabled()) {
                                        getScoreboard(player);
                                        player.setScoreboard(getPlayerScoreboard(player));
                                    }
                                    if (SkyWarsReloaded.getCfg().optionsMenuEnabled() && SkyWarsReloaded.getCfg().isOptionsItemEnabled()) {
                                        player.getInventory().setItem(SkyWarsReloaded.getCfg().getOptionsSlot(), SkyWarsReloaded.getIM().getItem("optionselect"));
                                    }
                                    if (SkyWarsReloaded.getCfg().joinMenuEnabled() && SkyWarsReloaded.getCfg().isJoinGameItemEnabled() && player.hasPermission("sw.join")) {
                                        player.getInventory().setItem(SkyWarsReloaded.getCfg().getJoinSlot(), SkyWarsReloaded.getIM().getItem("joinselect"));
                                    }
                                    if (!SkyWarsReloaded.getCfg().bungeeMode() && SkyWarsReloaded.getCfg().isSpectateGameItemEnabled() && SkyWarsReloaded.getCfg().spectateMenuEnabled() && player.hasPermission("sw.spectate")) {
                                        player.getInventory().setItem(SkyWarsReloaded.getCfg().getSpectateSlot(), SkyWarsReloaded.getIM().getItem("spectateselect"));
                                    }
                                    player.updateInventory();
                                }
                            }
                        }.runTask(SkyWarsReloaded.get());
                    } else {
                        this.cancel();
                    }
                } else {
                    updatePlayer(uuid);
                }
            }
        }.runTaskLaterAsynchronously(SkyWarsReloaded.get(), 10L);
    }

    public static ArrayList<PlayerStat> getPlayers() {
        return PlayerStat.players;
    }

    public static void setPlayers(final ArrayList<PlayerStat> playerData) {
        PlayerStat.players = playerData;
    }

    public static PlayerStat getPlayerStats(final String playerData) {
        for (final PlayerStat pData : getPlayers()) {
            if (pData.getId().equals(playerData)) {
                return pData;
            }
        }
        return null;
    }

    public static PlayerStat getPlayerStats(final Player player) {
        String uuid = player.getUniqueId().toString();
        for (final PlayerStat pData : getPlayers()) {
            if (pData.getId().equals(uuid)) {
                return pData;
            }
        }
        return null;
    }

    public static PlayerStat getPlayerStats(final UUID uuid) {
        for (final PlayerStat pData : getPlayers()) {
            if (pData.getId().equals(uuid.toString())) {
                return pData;
            }
        }
        return null;
    }

    private static void getScoreboard(Player player) {
        Scoreboard scoreboard = scoreboards.get(player);
        if (scoreboard != null) {
            resetScoreboard(player);
        }
        ScoreboardManager manager = SkyWarsReloaded.get().getServer().getScoreboardManager();
        scoreboard = manager.getNewScoreboard();
        Objective objective = SkyWarsReloaded.getNMS().getNewObjective(scoreboard, "dummy", "info");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        scoreboards.put(player, scoreboard);
        updateScoreboard(player);
    }

    public static void updateScoreboard(Player player) {
        Scoreboard scoreboard = scoreboards.get(player);
        if (scoreboard == null) {
            getScoreboard(player);
            scoreboard = scoreboards.get(player);
        }
        for (Objective objective : scoreboard.getObjectives()) {
            if (objective != null) {
                objective.unregister();
            }
        }

        Objective objective = SkyWarsReloaded.getNMS().getNewObjective(scoreboard, "dummy", "info");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        String sb = "scoreboards.lobbyboard.line";
        ArrayList<String> scores = new ArrayList<>();
        for (int i = 1; i < 17; i++) {
            if (i == 1) {
                String leaderboard = getScoreboardLine(sb + i, player);
                objective.setDisplayName(leaderboard);
            } else {
                String s = getScoreboardLine(sb + i, player);
                while (scores.contains(s) && !s.equalsIgnoreCase("remove")) {
                    s = s + " ";
                }
                scores.add(s);
                if (!s.equalsIgnoreCase("remove")) {
                    Score score = objective.getScore(s);
                    score.setScore(17 - i);
                }
            }
        }
    }

    private static String getScoreboardLine(String lineNum, Player player) {
        PlayerStat ps = PlayerStat.getPlayerStats(player);
        String killdeath;
        String winloss;
        if (ps != null) {
            if (ps.getWins() == 0) {
                winloss = "0.00";
            } else {
                winloss = String.format("%1$,.2f", ((double) ((double) ps.getWins() / (double) ps.getLosses())));
            }
            if (ps.getKills() == 0) {
                killdeath = "0.00";
            } else {
                killdeath = String.format("%1$,.2f", ((double) ((double) ps.getKills() / (double) ps.getDeaths())));
            }

            return new Messaging.MessageFormatter()
                    .setVariable("elo", Integer.toString(ps.getElo()))
                    .setVariable("wins", Integer.toString(ps.getWins()))
                    .setVariable("losses", Integer.toString(ps.getLosses()))
                    .setVariable("kills", Integer.toString(ps.getKills()))
                    .setVariable("deaths", Integer.toString(ps.getDeaths()))
                    .setVariable("xp", Integer.toString(ps.getXp()))
                    .setVariable("killdeath", killdeath)
                    .setVariable("winloss", winloss)
                    .setVariable("balance", "" + getBalance(player))
                    .setVariable("level", Integer.toString(Util.get().getPlayerLevel(player)))
                    .format(lineNum);
        }
        return "";
    }

    private static double getBalance(Player player) {
        if (SkyWarsReloaded.getCfg().economyEnabled()) {
            return VaultUtils.get().getBalance(player);
        }
        return 0;
    }

    private static void resetScoreboard(Player player) {
        Scoreboard scoreboard = scoreboards.get(player);
        if (!SkyWarsReloaded.getNMS().removeFromScoreboardCollection(scoreboard)) {
            for (Objective objective : scoreboard.getObjectives()) {
                if (objective != null) {
                    objective.unregister();
                }
            }
        }
    }

    private static Scoreboard getPlayerScoreboard(Player player) {
        return scoreboards.get(player);
    }

    public static void removePlayer(String id) {
        PlayerStat ps = getPlayerStats(id);
        if (ps != null) {
            players.remove(ps);
        }
    }

    private void saveStats(final String uuid) {
        Player player = SkyWarsReloaded.get().getServer().getPlayer(UUID.fromString(uuid));
        new BukkitRunnable() {
            public void run() {
                PlayerStat ps = PlayerStat.getPlayerStats(uuid);
                if (ps == null) {
                    this.cancel();
                } else if (ps.isInitialized()) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (SkyWarsReloaded.getCfg().bungeeMode()) {
                                if (player != null) {
                                    if (!SkyWarsReloaded.getCfg().isLobbyServer()) {
                                        boolean joined = MatchManager.get().joinGame(player, GameType.ALL);
                                        if (!joined) {
                                            SkyWarsReloaded.get().sendBungeeMsg(player, "Connect", SkyWarsReloaded.getCfg().getBungeeLobby());
                                        }
                                    }
                                }
                            }
                        }
                    }.runTask(SkyWarsReloaded.get());

                    DataStorage.get().saveStats(PlayerStat.getPlayerStats(uuid));
                } else {
                    saveStats(uuid);
                }
            }
        }.runTaskLaterAsynchronously(SkyWarsReloaded.get(), 1L);
    }

    public String getId() {
        return this.uuid;
    }

    public int getWins() {
        return this.wins;
    }

    public void setWins(final int a1) {
        this.wins = a1;
    }

    public int getKills() {
        return this.kills;
    }

    public void setKills(final int a1) {
        this.kills = a1;
    }

    public int getXp() {
        return this.xp;
    }

    public void setXp(int x) {
        this.xp = x;
    }

    public int getDeaths() {
        return this.deaths;
    }

    public void setDeaths(final int a1) {
        this.deaths = a1;
    }

    public int getElo() {
        return this.elo;
    }

    public void setElo(final int a1) {
        this.elo = a1;
    }

    public int getLosses() {
        return this.losts;
    }

    public void setLosts(final int a1) {
        this.losts = a1;
    }

    public boolean isInitialized() {
        return this.initialized;
    }

    public void setInitialized(final boolean a1) {
        this.initialized = a1;
    }

    public void clear() {
        this.losts = 0;
        this.wins = 0;
        this.kills = 0;
        this.deaths = 0;
        this.elo = 1500;
    }

    public String getParticleEffect() {
        return particleEffect;
    }

    public void setParticleEffect(String effect) {
        this.particleEffect = effect;
    }

    public String getProjectileEffect() {
        return projectileEffect;
    }

    public void setProjectileEffect(String effect) {
        this.projectileEffect = effect;
    }

    public String getGlassColor() {
        return this.glassColor;
    }

    public void setGlassColor(String glassC) {
        this.glassColor = glassC;
    }

    //Scoreboard Methods

    public String getKillSound() {
        return this.killSound;
    }

    public void setKillSound(String glassC) {
        this.killSound = glassC;
    }

    public String getWinSound() {
        return this.winSound;
    }

    public void setWinSound(String string) {
        this.winSound = string;
    }

    public String getPlayerName() {
        return playername;
    }

    public String getTaunt() {
        return taunt;
    }

    public void setTaunt(String string) {
        taunt = string;
    }

    public PermissionAttachment getPerms() {
        return perms;
    }

    public void addPerm(String perm, boolean save) {
        perms.setPermission(perm, true);
        if (save) {
            DataStorage.get().savePerms(this);
        }
    }
}