package net.gcnt.skywarsreloaded.data.player;

import net.gcnt.skywarsreloaded.data.mysql.SQLStorage;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLitePlayerStorage extends SQLPlayerStorage {

    private final String url;
    private final String fileName;

    public SQLitePlayerStorage(SQLStorage storage) {
        super(storage);
        this.fileName = "playerdata.db";
        this.url = "jdbc:sqlite:" + storage.getPlugin().getDataFolder() + File.separator + fileName;
    }

    @Override
    public Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(url);
        connection.setAutoCommit(true);
        return connection;
    }

    @Override
    public void setup(String username, String password, int port) {
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
}
