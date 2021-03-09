package com.walrusone.skywarsreloaded.database;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.enums.LeaderType;
import com.walrusone.skywarsreloaded.managers.PlayerStat;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DataStorage {

    private static DataStorage instance;

    public static DataStorage get() {
        if (DataStorage.instance == null) {
            DataStorage.instance = new DataStorage();
        }
        return DataStorage.instance;
    }

    public void saveStats(final PlayerStat pData) {
        boolean sqlEnabled = SkyWarsReloaded.get().getConfig().getBoolean("sqldatabase.enabled");
        if (!sqlEnabled) {
            try {
                File dataDirectory = SkyWarsReloaded.get().getDataFolder();
                File playerDataDirectory = new File(dataDirectory, "player_data");

                if (!playerDataDirectory.exists() && !playerDataDirectory.mkdirs()) {
                    return;
                }

                File playerFile = new File(playerDataDirectory, pData.getId() + ".yml");
                if (!playerFile.exists()) {
                    SkyWarsReloaded.get().getLogger().info("File doesn't exist!");
                    return;
                }

                copyDefaults(playerFile);
                FileConfiguration fc = YamlConfiguration.loadConfiguration(playerFile);
                fc.set("uuid", pData.getId());
                fc.set("player_name", pData.getPlayerName());
                fc.set("wins", pData.getWins());
                fc.set("losses", pData.getLosses());
                fc.set("kills", pData.getKills());
                fc.set("deaths", pData.getDeaths());
                fc.set("xp", pData.getXp());
                fc.set("pareffect", pData.getParticleEffect());
                fc.set("proeffect", pData.getProjectileEffect());
                fc.set("glasscolor", pData.getGlassColor());
                fc.set("killsound", pData.getKillSound());
                fc.set("winsound", pData.getWinSound());
                fc.set("taunt", pData.getTaunt());
                fc.save(playerFile);

            } catch (IOException ioException) {
                System.out.println("Failed to load faction " + pData.getId() + ": " + ioException.getMessage());
            }
        } else {
            Database database = SkyWarsReloaded.getDb();

            if (database.checkConnection()) {
                return;
            }

            Connection connection = database.getConnection();
            PreparedStatement preparedStatement = null;

            try {
                String query = "UPDATE `sw_player` SET `player_name` = ?, `wins` = ?, `losses` = ?, `kills` = ?, `deaths` = ?, `xp` = ?, `pareffect` = ?, " +
                        "`proeffect` = ?, `glasscolor` = ?,`killsound` = ?, `winsound` = ?, `taunt` = ? WHERE `uuid` = ?;";

                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, pData.getPlayerName());
                preparedStatement.setInt(2, pData.getWins());
                preparedStatement.setInt(3, pData.getLosses());
                preparedStatement.setInt(4, pData.getKills());
                preparedStatement.setInt(5, pData.getDeaths());
                preparedStatement.setInt(6, pData.getXp());
                preparedStatement.setString(7, pData.getParticleEffect());
                preparedStatement.setString(8, pData.getProjectileEffect());
                preparedStatement.setString(9, pData.getGlassColor());
                preparedStatement.setString(10, pData.getKillSound());
                preparedStatement.setString(11, pData.getWinSound());
                preparedStatement.setString(12, pData.getTaunt());
                preparedStatement.setString(13, pData.getId());
                preparedStatement.executeUpdate();

            } catch (final SQLException sqlException) {
                sqlException.printStackTrace();

            } finally {
                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (final SQLException ignored) {
                    }
                }
            }
        }
    }


    public void loadStats(final PlayerStat pData, Runnable postLoadStatsTask) {
        new BukkitRunnable() {
            @Override
            public void run() {
                boolean sqlEnabled = SkyWarsReloaded.get().getConfig().getBoolean("sqldatabase.enabled");
                if (sqlEnabled) {
                    Database database = SkyWarsReloaded.getDb();

                    if (database.checkConnection()) {
                        SkyWarsReloaded.get().getLogger().severe("Failed to connect to the database, player data will not be loaded and will be unable to play!");
                        return;
                    }

                    if (!database.doesPlayerExist(pData.getId())) {
                        database.createNewPlayer(pData.getId(), pData.getPlayerName());
                        pData.setWins(0);
                        pData.setLosts(0);
                        pData.setKills(0);
                        pData.setDeaths(0);
                        pData.setXp(0);
                        pData.setParticleEffect("none");
                        pData.setProjectileEffect("none");
                        pData.setGlassColor("none");
                        pData.setKillSound("none");
                        pData.setWinSound("none");
                        pData.setTaunt("none");
                    } else {
                        Connection connection = database.getConnection();
                        PreparedStatement preparedStatement = null;
                        ResultSet resultSet = null;

                        try {
                            String query = "SELECT `player_name`, `wins`, `losses`, `kills`, `deaths`, `xp`, `pareffect`, `proeffect`, `glasscolor`, `killsound`, `winsound`, `taunt` " +
                                    "FROM `sw_player` WHERE `uuid` = ? LIMIT 1;";

                            preparedStatement = connection.prepareStatement(query);
                            preparedStatement.setString(1, pData.getId());
                            resultSet = preparedStatement.executeQuery();

                            if (resultSet != null && resultSet.next()) {
                                String name = Bukkit.getPlayer(UUID.fromString(pData.getId())).getName();
                                if (name == null) name = pData.getPlayerName();
                                if (name == null) name = resultSet.getString("player_name");
                                pData.setPlayerName(name);
                                pData.setWins(resultSet.getInt("wins"));
                                pData.setLosts(resultSet.getInt("losses"));
                                pData.setKills(resultSet.getInt("kills"));
                                pData.setDeaths(resultSet.getInt("deaths"));
                                pData.setXp(resultSet.getInt("xp"));
                                pData.setParticleEffect(resultSet.getString("pareffect"));
                                pData.setProjectileEffect(resultSet.getString("proeffect"));
                                pData.setGlassColor(resultSet.getString("glasscolor"));
                                pData.setKillSound(resultSet.getString("killsound"));
                                pData.setWinSound(resultSet.getString("winsound"));
                                pData.setTaunt(resultSet.getString("taunt"));
                            }

                        } catch (final SQLException sqlException) {
                            sqlException.printStackTrace();

                        } finally {
                            if (resultSet != null) {
                                try {
                                    resultSet.close();
                                } catch (final SQLException ignored) {
                                }
                            }

                            if (preparedStatement != null) {
                                try {
                                    preparedStatement.close();
                                } catch (final SQLException ignored) {
                                }
                            }
                        }
                    }
                } else {
                    try {
                        File dataDirectory = SkyWarsReloaded.get().getDataFolder();
                        File playerDataDirectory = new File(dataDirectory, "player_data");

                        if (!playerDataDirectory.exists() && !playerDataDirectory.mkdirs()) {
                            SkyWarsReloaded.get().getLogger().info("Encountered an error while creating data directory!");
                        }

                        File playerFile = new File(playerDataDirectory, pData.getId() + ".yml");

                        if (!playerFile.exists() && !playerFile.createNewFile()) {
                            SkyWarsReloaded.get().getLogger().info("Something strange is happening while saving!");
                        }

                        copyDefaults(playerFile);
                        FileConfiguration fc = YamlConfiguration.loadConfiguration(playerFile);
                        String name = Bukkit.getPlayer(UUID.fromString(pData.getId())).getName();
                        if (name == null) name = pData.getPlayerName();
                        if (name == null) name = fc.getString("player_name", "null");
                        pData.setPlayerName(name);
                        pData.setWins(fc.getInt("wins", 0));
                        pData.setLosts(fc.getInt("losses", 0));
                        pData.setKills(fc.getInt("kills", 0));
                        pData.setDeaths(fc.getInt("deaths", 0));
                        pData.setXp(fc.getInt("xp", 0));
                        pData.setParticleEffect(fc.getString("pareffect", "none"));
                        pData.setProjectileEffect(fc.getString("proeffect", "none"));
                        pData.setGlassColor(fc.getString("glasscolor", "none"));
                        pData.setKillSound(fc.getString("killsound", "none"));
                        pData.setWinSound(fc.getString("winsound", "none"));
                        pData.setTaunt(fc.getString("taunt", "none"));
                    } catch (IOException ioException) {
                        System.out.println("Failed to load player " + pData.getId() + ": " + ioException.getMessage());
                    }
                }
                postLoadStatsTask.run();
            }
        }.runTaskAsynchronously(SkyWarsReloaded.get());
    }

    private void copyDefaults(File playerFile) {
        FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerFile);
        Reader defConfigStream = new InputStreamReader(SkyWarsReloaded.get().getResource("playerFile.yml"));
        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
        playerConfig.setDefaults(defConfig);
        playerConfig.options().copyDefaults();
        try {
            playerConfig.save(playerFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void removePlayerData(String uuid) {
        boolean sqlEnabled = SkyWarsReloaded.get().getConfig().getBoolean("sqldatabase.enabled");
        if (!sqlEnabled) {
            File dataDirectory = SkyWarsReloaded.get().getDataFolder();
            File playerDataDirectory = new File(dataDirectory, "player_data");

            if (!playerDataDirectory.exists() && !playerDataDirectory.mkdirs()) {
                SkyWarsReloaded.get().getLogger().info("Failed to create data directory");
            }

            File playerFile = new File(playerDataDirectory, uuid + ".yml");
            if (playerFile.exists()) {
                if (!playerFile.delete())
                    SkyWarsReloaded.get().getLogger().info("Failed to delete playerfile for: " + uuid);
            }
        } else {
            Database database = SkyWarsReloaded.getDb();

            if (database.checkConnection()) {
                return;
            }

            Connection connection = database.getConnection();
            PreparedStatement preparedStatement = null;

            try {
                String query = "DELETE FROM `sw_player` WHERE `uuid` = ?;";

                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, uuid);
                preparedStatement.executeUpdate();

            } catch (final SQLException sqlException) {
                sqlException.printStackTrace();

            } finally {
                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (final SQLException ignored) {
                    }
                }
            }
        }
    }

    public void updateTop(LeaderType type, int size) {
        new BukkitRunnable() {

            @Override
            public void run() {
                boolean sqlEnabled = SkyWarsReloaded.get().getConfig().getBoolean("sqldatabase.enabled");
                if (sqlEnabled) {
                    Database database = SkyWarsReloaded.getDb();

                    if (database.checkConnection()) {
                        return;
                    }

                    Connection connection = database.getConnection();
                    PreparedStatement preparedStatement;
                    ResultSet resultSet;

                    try {
                        String query = "SELECT `uuid`, `player_name`, `wins`, `losses`, `kills`, `deaths`, `xp` FROM `sw_player` GROUP BY `uuid` " +
                                "ORDER BY `" + type.toString().toLowerCase() + "` DESC LIMIT " + size + ";";

                        preparedStatement = connection.prepareStatement(query);
                        resultSet = preparedStatement.executeQuery();
                        SkyWarsReloaded.getLB().resetLeader(type);
                        while (resultSet.next()) {
                            String uuid = resultSet.getString("uuid");
                            String name = Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName();
                            if (name == null) name = resultSet.getString("player_name");
                            int wins = resultSet.getInt("wins");
                            int losses = resultSet.getInt("losses");
                            int kills = resultSet.getInt("kills");
                            int deaths = resultSet.getInt( "deaths");
                            int xp = resultSet.getInt("xp");
                            SkyWarsReloaded.getLB().addLeader(type, uuid, name, wins, losses, kills, deaths, xp);
                        }

                    } catch (final SQLException sqlException) {
                        sqlException.printStackTrace();

                    }
                } else {
                    File dataDirectory = SkyWarsReloaded.get().getDataFolder();
                    File playerDirectory = new File(dataDirectory, "player_data");

                    if (!playerDirectory.exists()) {
                        if (!playerDirectory.mkdirs()) {
                            return;
                        }
                    }

                    File[] playerFiles = playerDirectory.listFiles();
                    if (playerFiles == null) {
                        return;
                    }

                    SkyWarsReloaded.getLB().resetLeader(type);
                    for (File playerFile : playerFiles) {
                        if (!playerFile.getName().endsWith(".yml")) {
                            continue;
                        }

                        FileConfiguration fc = YamlConfiguration.loadConfiguration(playerFile);

                        String uuid = playerFile.getName().replace(".yml", "");
                        String name = Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName();
                        if (name == null) name = fc.getString("player_name", "null");
                        int wins = fc.getInt("wins", 0);
                        int losses = fc.getInt("losses", 0);
                        int kills = fc.getInt("kills", 0);
                        int deaths = fc.getInt("deaths", 0);
                        int xp = fc.getInt("xp", 0);
                        SkyWarsReloaded.getLB().addLeader(type, uuid, name, wins, losses, kills, deaths, xp);
                    }
                }
                SkyWarsReloaded.getLB().finishedLoading(type);
            }
        }.runTaskLaterAsynchronously(SkyWarsReloaded.get(), 10L);
    }

    public void loadperms(PlayerStat playerStat) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!SkyWarsReloaded.get().getConfig().getBoolean("sqldatabase.enabled")) {
                    try {
                        File dataDirectory = SkyWarsReloaded.get().getDataFolder();
                        File playerDataDirectory = new File(dataDirectory, "player_data");

                        if (!playerDataDirectory.exists() && !playerDataDirectory.mkdirs()) {
                            System.out.println("Failed to load player " + playerStat.getPlayerName() + ": Could not create player_data directory.");
                            return;
                        }

                        File playerFile = new File(playerDataDirectory, playerStat.getId() + ".yml");
                        if (!playerFile.exists() && !playerFile.createNewFile()) {
                            System.out.println("Failed to load player " + playerStat.getPlayerName() + ": Could not create player file.");
                            return;
                        }
                        copyDefaults(playerFile);
                        FileConfiguration fc = YamlConfiguration.loadConfiguration(playerFile);

                        List<String> perms = fc.getStringList("permissions");
                        for (String perm : perms) {
                            playerStat.addPerm(perm, false);
                        }
                    } catch (IOException ioException) {
                        System.out.println("Failed to load player " + playerStat.getPlayerName() + ": " + ioException.getMessage());
                    }
                } else {
                    Database database = SkyWarsReloaded.getDb();
                    if (database.checkConnection()) {
                        return;
                    }
                    Connection connection = database.getConnection();
                    PreparedStatement preparedStatement = null;
                    ResultSet resultSet = null;

                    try {
                        String query = "SELECT `permissions` FROM `sw_permissions` WHERE `uuid` = ?;";

                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, playerStat.getId());
                        resultSet = preparedStatement.executeQuery();

                        while (resultSet != null && resultSet.next()) {
                            playerStat.addPerm(resultSet.getString("permissions"), false);
                        }
                    } catch (final SQLException sqlException) {
                        sqlException.printStackTrace();

                    } finally {
                        if (resultSet != null) {
                            try {
                                resultSet.close();
                            } catch (final SQLException ignored) {
                            }
                        }
                        if (preparedStatement != null) {
                            try {
                                preparedStatement.close();
                            } catch (final SQLException ignored) {
                            }
                        }
                    }
                }
            }

        }.runTaskAsynchronously(SkyWarsReloaded.get());
    }

    public void savePerms(PlayerStat playerStat) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!SkyWarsReloaded.get().getConfig().getBoolean("sqldatabase.enabled")) {
                    try {
                        File dataDirectory = SkyWarsReloaded.get().getDataFolder();
                        File playerDataDirectory = new File(dataDirectory, "player_data");

                        if (!playerDataDirectory.exists() && !playerDataDirectory.mkdirs()) {
                            System.out.println("Failed to load player " + playerStat.getPlayerName() + ": Could not create player_data directory.");
                            return;
                        }

                        File playerFile = new File(playerDataDirectory, playerStat.getId() + ".yml");
                        if (!playerFile.exists() && !playerFile.createNewFile()) {
                            System.out.println("Failed to load player " + playerStat.getPlayerName() + ": Could not create player file.");
                            return;
                        }
                        copyDefaults(playerFile);
                        FileConfiguration fc = YamlConfiguration.loadConfiguration(playerFile);

                        List<String> perms = new ArrayList<>(playerStat.getPerms().getPermissions().keySet());
                        fc.set("permissions", perms);
                        fc.save(playerFile);

                    } catch (IOException ioException) {
                        System.out.println("Failed to load player " + playerStat.getPlayerName() + ": " + ioException.getMessage());
                    }
                } else {
                    if (playerStat.getPerms().getPermissions().size() > 0) {
                        Database database = SkyWarsReloaded.getDb();
                        if (database.checkConnection()) {
                            return;
                        }
                        Connection connection = database.getConnection();
                        PreparedStatement preparedStatement = null;
                        try {
                            if (playerStat.getPerms().getPermissions().size() >= 1) {
                                for (String perm : playerStat.getPerms().getPermissions().keySet()) {
                                    String query = "INSERT INTO `sw_permissions` (`uuid`, `playername`, `permissions`) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE " +
                                            "`uuid`=`uuid`, `playername`=`playername`, `permissions`=`permissions` ";

                                    preparedStatement = connection.prepareStatement(query);
                                    preparedStatement.setString(1, playerStat.getId());
                                    preparedStatement.setString(2, playerStat.getPlayerName());
                                    preparedStatement.setString(3, perm);
                                    preparedStatement.executeUpdate();
                                }
                            }
                        } catch (final SQLException sqlException) {
                            sqlException.printStackTrace();
                        } finally {
                            if (preparedStatement != null) {
                                try {
                                    preparedStatement.close();
                                } catch (final SQLException ignored) {
                                }
                            }
                        }
                    }
                }
            }
        }.runTaskAsynchronously(SkyWarsReloaded.get());
    }


}