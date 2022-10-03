package net.gcnt.skywarsreloaded.data.sql;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.data.config.YAMLConfig;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CoreSQLiteStorage implements SQLStorage {

    protected final SkyWarsReloaded plugin;
    private final List<SQLTable<?>> tables;
    private final String databaseUrl;

    public CoreSQLiteStorage(SkyWarsReloaded plugin) {
        this.plugin = plugin;
        this.tables = new ArrayList<>();
        this.databaseUrl = "jdbc:sqlite:" + plugin.getDataFolder() + File.separator + "sw_data.db";
    }

    @Override
    public Connection getConnection() throws SQLException {
        Connection con = DriverManager.getConnection(databaseUrl);
        con.setAutoCommit(true);
        return con;
    }

    @Override
    public void setup(YAMLConfig yamlConfig) {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            ClassNotFoundException ex = new ClassNotFoundException("Failed to connect to MySQL databases because the Driver could not be found.");
            ex.setStackTrace(e.getStackTrace());
            return;
        }

        try (Connection connection = getConnection()) {
            for (SQLTable<?> table : this.tables) {
                table.createTable(connection);
            }
        } catch (SQLException e) {
            plugin.getLogger().error("SkyWarsReloaded failed to connect to the SQLite database located at: '" + plugin.getDataFolder() + File.separator + "sw_data.db'. Do you have access?");
            plugin.getLogger().error("Here's the mysql URI we used: " + databaseUrl);
            plugin.getLogger().error("Disabling the plugin to prevent further complications...");
            e.printStackTrace();
            plugin.disableSkyWars();
        }
    }

    @Override
    public void setupDatabase(Connection connection, String database) {
    }

    @Override
    public SkyWarsReloaded getPlugin() {
        return plugin;
    }

    @Override
    public void addTable(SQLTable<?> table) {
        this.tables.add(table);
    }
}
