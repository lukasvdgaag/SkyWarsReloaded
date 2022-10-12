package net.gcnt.skywarsreloaded.data.sql.tables.sqlite;

import net.gcnt.skywarsreloaded.data.sql.SQLStorage;
import net.gcnt.skywarsreloaded.data.sql.tables.SQLPlayerTable;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLitePlayerTable extends SQLPlayerTable {

    public SQLitePlayerTable(SQLStorage storage) {
        super(storage);
    }

    @Override
    public void createTable(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " + table + " (" +
                    "uuid TEXT NOT NULL UNIQUE PRIMARY KEY," +
                    "selected_solo_cage TEXT DEFAULT NULL," +
                    "selected_team_cage TEXT DEFAULT NULL," +
                    "selected_particle TEXT DEFAULT NULL," +
                    "selected_kill_effect TEXT DEFAULT NULL," +
                    "selected_win_effect TEXT DEFAULT NULL," +
                    "selected_projectile_effect TEXT DEFAULT NULL," +
                    "selected_kill_messages_theme TEXT DEFAULT NULL," +
                    "selected_kit TEXT DEFAULT NULL)");
        }
    }

    @Override
    public void initStatsTable() {
        this.statsTable = new SQLitePlayerStatsTable(storage);
    }

    @Override
    public void initUnlockablesTable() {
        this.unlockablesTable = new SQLitePlayerUnlockablesTable(storage);
    }

}
