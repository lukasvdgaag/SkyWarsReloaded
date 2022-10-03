package net.gcnt.skywarsreloaded.data.mysql;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.data.Storage;

import java.sql.Connection;
import java.sql.SQLException;

public interface SQLStorage extends Storage {

    Connection getConnection() throws SQLException;

    void setupDatabase(Connection connection, String database) throws SQLException;

    SkyWarsReloaded getPlugin();

    void addTable(SQLTable<?> table);

}
