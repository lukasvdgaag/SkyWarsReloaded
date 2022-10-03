package net.gcnt.skywarsreloaded.data.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.data.config.YAMLConfig;
import net.gcnt.skywarsreloaded.utils.properties.ConfigProperties;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CoreMySQLStorage implements SQLStorage {

    protected final SkyWarsReloaded plugin;
    protected final int minPoolSize;
    protected final int maxPoolSize;
    protected HikariDataSource ds;
    private final List<SQLTable<?>> tables;

    public CoreMySQLStorage(SkyWarsReloaded plugin) {
        this(plugin, 3, 20);
    }

    public CoreMySQLStorage(SkyWarsReloaded plugin, int minPoolSize, int maxPoolSize) {
        this.plugin = plugin;
        this.minPoolSize = minPoolSize;
        this.maxPoolSize = maxPoolSize;
        this.tables = new ArrayList<>();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    @Override
    public void setup(YAMLConfig yamlConfig) {
        String hostname = yamlConfig.getString(ConfigProperties.STORAGE_HOSTNAME.toString());
        String database = yamlConfig.getString(ConfigProperties.STORAGE_DATABASE.toString());
        int port = yamlConfig.getInt(ConfigProperties.STORAGE_PORT.toString());

        HikariConfig config = new HikariConfig();
        String uri = "jdbc:mysql://" + hostname + ":" + port + "/" + database + "?useSSL=" + (yamlConfig.getBoolean(ConfigProperties.STORAGE_USE_SSL.toString()));
        config.setJdbcUrl(uri);
        config.setUsername(yamlConfig.getString(ConfigProperties.STORAGE_USERNAME.toString()));
        config.setPassword(yamlConfig.getString(ConfigProperties.STORAGE_DATABASE.toString()));
        config.setMinimumIdle(this.minPoolSize);
        config.setMaximumPoolSize(this.maxPoolSize);
        config.setConnectionTimeout(4000);
        ds = new HikariDataSource(config);

        try (Connection connection = getConnection()) {
            this.setupDatabase(connection, database);

            for (SQLTable<?> table : this.tables) {
                table.createTable(connection);
            }

        } catch (SQLException e) {
            plugin.getLogger().error("SkyWarsReloaded failed to connect to the MySQL database with the following hostname: '" + hostname + ":" + port + "'. Do you have access?");
            plugin.getLogger().error("Here's the mysql URI we used: " + uri);
            plugin.getLogger().error("Disabling the plugin to prevent further complications...");
            e.printStackTrace();
            plugin.disableSkyWars();
        }
    }

    @Override
    public void setupDatabase(Connection connection, String database) throws SQLException {
        connection.createStatement().executeUpdate("CREATE DATABASE IF NOT EXISTS " + database);
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
