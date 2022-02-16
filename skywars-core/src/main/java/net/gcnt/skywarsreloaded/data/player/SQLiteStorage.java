package net.gcnt.skywarsreloaded.data.player;

import net.gcnt.skywarsreloaded.AbstractSkyWarsReloaded;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;

import java.io.File;
import java.sql.*;

public class SQLiteStorage implements Storage {

    private final AbstractSkyWarsReloaded plugin;
    private final String url;

    public SQLiteStorage(AbstractSkyWarsReloaded plugin) {
        this.plugin = plugin;
        this.url = "jdbc:sqlite:" + plugin.getDataFolder() + File.separator + "playerdata.db"; // todo change this name?
    }

    public Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(url);
        connection.setAutoCommit(true);
        return connection;
    }

    @Override
    public void setup() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            ClassNotFoundException ex = new ClassNotFoundException("Failed to connect to MySQL databases because the Driver could not be found.");
            ex.setStackTrace(e.getStackTrace());
            ex.printStackTrace();
            return;
        }

        try (Connection connection = getConnection()) {
            if (connection != null) {
                connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS `sw_player_data` (" +
                        "uuid TEXT NOT NULL UNIQUE PRIMARY KEY," +
                        "solo_wins  INT DEFAULT 0," +
                        "solo_kills INT DEFAULT 0," +
                        "solo_games INT DEFAULT 0," +
                        "team_wins INT DEFAULT 0," +
                        "team_kills INT DEFAULT 0," +
                        "team_games INT DEFAULT 0," +
                        "selected_solo_cage TEXT DEFAULT NULL," +
                        "selected_team_cage TEXT DEFAULT NULL," +
                        "selected_particle TEXT DEFAULT NULL," +
                        "selected_kill_effect TEXT DEFAULT NULL," +
                        "selected_win_effect TEXT DEFAULT NULL," +
                        "selected_projectile_effect TEXT DEFAULT NULL," +
                        "selected_kill_messages_theme TEXT DEFAULT NULL" + ")");
            } else {
                plugin.getLogger().error("SkyWarsReloaded failed to connect to SQLite database file for player data.");
            }
        } catch (SQLException e) {
            plugin.getLogger().error("SkyWarsReloaded failed to create the SQLite database called 'playerdata.db'!"); // todo change this name?
            plugin.getLogger().error("Disabling the plugin to prevent further complications!");
            e.printStackTrace();
            plugin.disableSkyWars();
        }
    }

    private void createDefault(SWPlayer player, Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("INSERT INTO `sw_player_data`(`uuid`) VALUES (?)");
        ps.setString(1, player.getUuid().toString());
        ps.executeUpdate();

        SWPlayerData swpd = this.plugin.getPlayerDataManager().createSWPlayerDataInstance();
        swpd.initData(0, 0, 0, 0, 0, 0, null, null, null, null, null, null, null);
        player.setPlayerData(swpd);
    }

    @Override
    public void loadData(SWPlayer player) {
        try (Connection conn = getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM `sw_player_data` WHERE `uuid`=?");
            ps.setString(1, player.getUuid().toString());
            ResultSet res = ps.executeQuery();

            if (!res.next()) {
                createDefault(player, conn);
                return;
            }

            SWPlayerData swpd = this.plugin.getPlayerDataManager().createSWPlayerDataInstance();
            swpd.initData(
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
                    res.getString("selected_projectile_effect"),
                    res.getString("selected_kill_messages_theme")
            );
            player.setPlayerData(swpd);
            res.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveData() {
        // we do not need this for SQL
    }

    @Override
    public void setProperty(String property, Object value, SWPlayer player) {
        try (Connection conn = getConnection()) {
            PreparedStatement ps = conn.prepareStatement("UPDATE `sw_player_data` SET ?=? WHERE `uuid`=?");
            ps.setString(1, property);

            if (value instanceof Integer) ps.setInt(2, (Integer) value);
            else if (value instanceof Double) ps.setDouble(2, (Double) value);
            else if (value instanceof Boolean) ps.setBoolean(2, (Boolean) value);
            else if (value instanceof Float) ps.setFloat(2, (Float) value);
            else ps.setString(2, value.toString());

            ps.setString(3, player.getUuid().toString());

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
