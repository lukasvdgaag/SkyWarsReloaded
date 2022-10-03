package net.gcnt.skywarsreloaded.data.sql.tables;

import net.gcnt.skywarsreloaded.data.sql.SQLStorage;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLitePlayerTable extends SQLPlayerTable {

    public SQLitePlayerTable(SQLStorage storage) {
        super(storage);
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
                "selected_kill_messages_theme TEXT DEFAULT NULL," +
                "selected_kit TEXT DEFAULT NULL " + ")");
    }
}
