package net.gcnt.skywarsreloaded.bukkit.data;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.gcnt.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.gcnt.skywarsreloaded.data.SWPlayer;
import net.gcnt.skywarsreloaded.data.Storage;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLStorage implements Storage {

    private final BukkitSkyWarsReloaded plugin;
    private HikariDataSource ds;

    public MySQLStorage(BukkitSkyWarsReloaded plugin) {
        this.plugin = plugin;
    }

    private Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    @Override
    public void setup() {
        BukkitYAMLConfig yamlConfig = (BukkitYAMLConfig) plugin.getYAMLManager().getFile("config.yml");
        if (yamlConfig == null)
            throw new NullPointerException("Cannot set up database connection because config file is null. Unable to retrieve database info from config.yml.");

        String hostname = yamlConfig.getString("storage.hostname");
        String database = yamlConfig.getString("storage.database");
        int port = 3306;
        if (hostname.contains(":")) {
            String[] split = hostname.split(":");
            hostname = split[0];
            port = Integer.parseInt(split[1]);
        }

        HikariConfig config = new HikariConfig();
        String uri = "jdbc:mysql://" + hostname + ":" + port + "/" + database + "?useSSL=" + (yamlConfig.getBoolean("storage.use-ssl"));
        config.setJdbcUrl(uri);
        config.setUsername(yamlConfig.getString("storage.username"));
        config.setPassword(yamlConfig.getString("storage.password"));
        config.setMinimumIdle(1);
        config.setMaximumPoolSize(50);
        config.setConnectionTimeout(4000);
        ds = new HikariDataSource(config);

        try (Connection connection = getConnection()) {
            connection.createStatement().executeUpdate("CREATE DATABASE IF NOT EXISTS " + database);
            connection.createStatement().executeUpdate("""
                    CREATE TABLE IF NOT EXISTS `sw_player_data` (
                     `uuid`   VARCHAR(255)  NOT NULL UNIQUE,
                     `solo_wins`  INT(6) DEFAULT 0,
                     `solo_kills` INT(10) DEFAULT 0,
                     `solo_games` INT(10) DEFAULT 0,
                     `team_wins` INT(10) DEFAULT 0,
                     `team_kills` INT(10) DEFAULT 0,
                     `team_games` INT(10) DEFAULT 0,
                     `selected_solo_cage` VARCHAR(100) DEFAULT NULL,
                     `selected_team_cage` VARCHAR(100) DEFAULT NULL,
                     `selected_particle` VARCHAR(100) DEFAULT NULL,
                     `selected_kill_effect` VARCHAR(100) DEFAULT NULL,
                     `selected_win_effect` VARCHAR(100) DEFAULT NULL,
                     `selected_projectile_effect` VARCHAR(100) DEFAULT NULL,
                     KEY  (`uuid`)
                    )""");
        } catch (SQLException e) {
            plugin.getLogger().severe("SkyWarsReloaded failed to connect to the MySQL database with the following hostname: '" + hostname + ":" + port + "'. Do you have access?");
            plugin.getLogger().severe("Here's the mysql URI we used: " + uri);
            plugin.getLogger().severe("Disabling the plugin to prevent further complications...");
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
    }

    @Override
    public void loadData(SWPlayer player) {
        try (Connection conn = getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM `sw_player_data` WHERE `uuid`=?");
            ps.setString(1, player.getUuid().toString());
            ResultSet res = ps.executeQuery();

            player.insertData(
                    res.getInt("solo_wins"),
                    res.getInt("solo_kills"),
                    res.getInt("solo_games"),
                    res.getInt("team_wins"),
                    res.getInt("team_kills"),
                    res.getInt("team_games"),
                    res.getString("selected_solo_cage"),
                    res.getString("selected_team_cage"),
                    res.getString("selected_particle"),
                    res.getString("selected_kill_effect"),
                    res.getString("selected_win_effect"),
                    res.getString("selected_projectile_effect")
            );
            res.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void set(String property, Object value, SWPlayer player) {
        try (Connection conn = getConnection()) {
            PreparedStatement ps = conn.prepareStatement("UPDATE `sw_player_data` SET ?=? WHERE `uuid`=?");
            ps.setString(1, property);

            if (value instanceof Integer) ps.setInt(2, (int) value);
            else ps.setString(2, value.toString());

            ps.setString(3, player.getUuid().toString());
            // ?todo add possible other types of data.

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
