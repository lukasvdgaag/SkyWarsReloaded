package net.gcnt.skywarsreloaded.data.player;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.data.CoreMySQLStorage;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;

import java.io.File;
import java.sql.*;

public class SQLitePlayerStorage extends CoreMySQLStorage<SWPlayer> {

    private final String url;
    private final String fileName;

    public SQLitePlayerStorage(SkyWarsReloaded plugin) {
        super(plugin, "sw_player_data");
        this.fileName = "playerdata.db";
        this.url = "jdbc:sqlite:" + plugin.getDataFolder() + File.separator + fileName;
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
            ClassNotFoundException ex = new ClassNotFoundException("Failed to connect to SQLite databases because the Driver could not be found.");
            ex.setStackTrace(e.getStackTrace());
            ex.printStackTrace();
            return;
        }

        try (Connection connection = getConnection()) {
            this.createTable(connection);
        } catch (SQLException e) {
            plugin.getLogger().error("SkyWarsReloaded failed to create the SQLite database called '" + fileName + "'!"); // todo change this name?
            plugin.getLogger().error("Disabling the plugin to prevent further complications!");
            e.printStackTrace();
            plugin.disableSkyWars();
        }
    }

    @Override
    public void createTable(Connection connection) throws SQLException {
        connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
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
    }

    private void createDefault(SWPlayer player, Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("INSERT INTO `" + table + "` (`uuid`) VALUES (?)");
        ps.setString(1, player.getUuid().toString());
        ps.executeUpdate();

        SWPlayerData swpd = this.plugin.getPlayerDataManager().createSWPlayerDataInstance();
        SWPlayerStats swps = this.plugin.getPlayerDataManager().createSWPlayerStatsInstance();
        swps.initData(0, 0, 0, 0, 0, 0);
        swpd.initData(swps, null, null, null, null, null, null, null);
        player.setPlayerData(swpd);
    }

    @Override
    public void loadData(SWPlayer player) {
        try (Connection conn = getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM `" + table + "` WHERE `uuid`=?");
            ps.setString(1, player.getUuid().toString());
            ResultSet res = ps.executeQuery();

            if (!res.next()) {
                createDefault(player, conn);
                return;
            }

            SWPlayerData swpd = this.plugin.getPlayerDataManager().createSWPlayerDataInstance();
            SWPlayerStats swps = this.plugin.getPlayerDataManager().createSWPlayerStatsInstance();
            swps.initData(
                    res.getInt("solo_wins"),
                    res.getInt("solo_kills"),
                    res.getInt("solo_games"),
                    res.getInt("team_wins"),
                    res.getInt("team_kills"),
                    res.getInt("team_games")
            );
            swpd.initData(swps,
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
    public void saveData(SWPlayer player) {
        SWPlayerData swpd = player.getPlayerData();
        SWPlayerStats swps = swpd.getStats();

        try (Connection conn = getConnection()) {
            PreparedStatement ps = conn.prepareStatement("UPDATE `" + table + "` SET solo_wins=?, solo_kills=?, solo_games=?, team_wins=?, team_kills=?, team_games=? WHERE `uuid`=?");
            ps.setInt(1, swps.getSoloWins());
            ps.setInt(2, swps.getSoloKills());
            ps.setInt(3, swps.getSoloGamesPlayed());
            ps.setInt(4, swps.getTeamKills());
            ps.setInt(5, swps.getTeamWins());
            ps.setInt(6, swps.getTeamGamesPlayed());

            ps.setString(7, player.getUuid().toString());

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setProperty(String property, Object value, SWPlayer player) {
        try (Connection conn = getConnection()) {
            PreparedStatement ps = conn.prepareStatement("UPDATE `" + table + "` SET ?=? WHERE `uuid`=?");

            ps.setString(1, property);
            bindPropertyValue(ps, 2, value);
            ps.setString(3, player.getUuid().toString());

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
