package net.gcnt.skywarsreloaded.data;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.data.config.YAMLConfig;
import net.gcnt.skywarsreloaded.utils.properties.ConfigProperties;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class CoreMySQLStorage<DataType> implements MySQLStorage<DataType> {

    protected final SkyWarsReloaded plugin;
    protected final String table;
    protected final int minPoolSize;
    protected final int maxPoolSize;
    protected HikariDataSource ds;

    public CoreMySQLStorage(SkyWarsReloaded plugin, String table) {
        this(plugin, table, 1, 10);
    }

    public CoreMySQLStorage(SkyWarsReloaded plugin, String table, int minPoolSize, int maxPoolSize) {
        this.plugin = plugin;
        this.table = table;
        this.minPoolSize = minPoolSize;
        this.maxPoolSize = maxPoolSize;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    @Override
    public void setup() {
        YAMLConfig yamlConfig = plugin.getConfig();

        String hostname = yamlConfig.getString(ConfigProperties.STORAGE_HOSTNAME.toString());
        String database = yamlConfig.getString(ConfigProperties.STORAGE_DATABASE.toString());
        int port = 3306;
        if (hostname.contains(":")) {
            String[] split = hostname.split(":");
            hostname = split[0];
            port = Integer.parseInt(split[1]);
        }

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
            this.createDatabase(connection, database);
            this.createTable(connection);
        } catch (SQLException e) {
            plugin.getLogger().error("SkyWarsReloaded failed to connect to the MySQL database with the following hostname: '" + hostname + ":" + port + "'. Do you have access?");
            plugin.getLogger().error("Here's the mysql URI we used: " + uri);
            plugin.getLogger().error("Disabling the plugin to prevent further complications...");
            e.printStackTrace();
            plugin.disableSkyWars();
        }
    }

    @Override
    public void createDatabase(Connection connection, String database) throws SQLException {
        connection.createStatement().executeUpdate("CREATE DATABASE IF NOT EXISTS " + database);
    }

    /**
     * @param statement     The {@link PreparedStatement} to supply with parameter data
     * @param paramPosition Number from 1 to n
     * @param value         Paramter data
     * @throws SQLException
     */
    @Override
    public void bindPropertyValue(PreparedStatement statement, int paramPosition, Object value) throws SQLException {
        if (statement == null) return;

        if (value instanceof Integer) statement.setInt(paramPosition, (Integer) value);
        else if (value instanceof Double) statement.setDouble(paramPosition, (Double) value);
        else if (value instanceof Boolean) statement.setBoolean(paramPosition, (Boolean) value);
        else if (value instanceof Float) statement.setFloat(paramPosition, (Float) value);
        else statement.setString(paramPosition, value.toString());
    }

    @Override
    /**
     * Does nothing by default
     */
    public void setProperty(String property, Object value, DataType object) {

    }
}
