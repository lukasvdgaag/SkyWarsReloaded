package net.gcnt.skywarsreloaded.data.sql.tables.sqlite;

import net.gcnt.skywarsreloaded.data.sql.SQLStorage;
import net.gcnt.skywarsreloaded.data.sql.tables.SQLPlayerTable;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLitePlayerStatsTable extends SQLPlayerTable {

    public SQLitePlayerStatsTable(SQLStorage storage) {
        super(storage);
    }

    @Override
    public void createTable(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
                    "uuid TEXT NOT NULL UNIQUE PRIMARY KEY," +
                    "solo_wins  INT DEFAULT 0," +
                    "solo_kills INT DEFAULT 0," +
                    "solo_games INT DEFAULT 0," +
                    "team_wins  INT DEFAULT 0," +
                    "team_kills INT DEFAULT 0," +
                    "team_games INT DEFAULT 0," + ")");
        }
    }
}
