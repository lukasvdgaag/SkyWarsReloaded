package net.gcnt.skywarsreloaded.data.sql.tables.sqlite;

import net.gcnt.skywarsreloaded.data.sql.SQLStorage;
import net.gcnt.skywarsreloaded.data.sql.tables.SQLPlayerUnlockablesTable;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLitePlayerUnlockablesTable extends SQLPlayerUnlockablesTable {

    public SQLitePlayerUnlockablesTable(SQLStorage storage) {
        super(storage);
    }

    @Override
    public void createTable(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " + table + " (" +
                    "uuid TEXT NOT NULL UNIQUE PRIMARY KEY," +
                    "type TEXT NOT NULL," +
                    "name TEXT NOT NULL," +
                    "timestamp INT DEFAULT CURRENT_TIMESTAMP" + ")");
        }
    }
}
