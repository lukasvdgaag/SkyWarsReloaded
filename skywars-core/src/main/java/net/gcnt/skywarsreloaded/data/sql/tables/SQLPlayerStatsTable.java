package net.gcnt.skywarsreloaded.data.sql.tables;

import net.gcnt.skywarsreloaded.data.player.SWPlayerStorageUnit;
import net.gcnt.skywarsreloaded.data.player.stats.SWPlayerData;
import net.gcnt.skywarsreloaded.data.player.stats.SWPlayerStats;
import net.gcnt.skywarsreloaded.data.sql.CoreSQLTable;
import net.gcnt.skywarsreloaded.data.sql.SQLStorage;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLPlayerStatsTable extends CoreSQLTable<SWPlayer> implements SWPlayerStorageUnit {

    public SQLPlayerStatsTable(SQLStorage storage) {
        super(storage, "sw_player_stats");
    }

    @Override
    public void createTable(Connection connection) throws SQLException {
        connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
                "`uuid`   VARCHAR(255)  NOT NULL UNIQUE," +
                "`solo_wins`  INT(6) DEFAULT 0," +
                "`solo_kills` INT(10) DEFAULT 0," +
                "`solo_games` INT(10) DEFAULT 0," +
                "`team_wins` INT(10) DEFAULT 0," +
                "`team_kills` INT(10) DEFAULT 0," +
                "`team_games` INT(10) DEFAULT 0," +
                "KEY  (`uuid`))");
    }

    protected void createDefault(SWPlayer player, Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("INSERT INTO `" + table + "`(`uuid`) VALUES (?)");
        ps.setString(1, player.getUuid().toString());
        ps.executeUpdate();

        SWPlayerData swpd = player.getPlayerData();
        SWPlayerStats swps = storage.getPlugin().getPlayerDataManager().createSWPlayerStatsInstance();
        swps.initData(0, 0, 0, 0, 0, 0);
        player.setPlayerData(swpd);
    }

    @Override
    public void loadData(SWPlayer player) {
        try (Connection conn = storage.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM `" + table + "` WHERE `uuid`=?")) {
            ps.setString(1, player.getUuid().toString());

            try (ResultSet res = ps.executeQuery()) {
                if (!res.next()) {
                    createDefault(player, conn);
                    return;
                }

                SWPlayerData playerData = player.getPlayerData();
                SWPlayerStats stats = storage.getPlugin().getPlayerDataManager().createSWPlayerStatsInstance();
                stats.initData(
                        res.getInt("solo_wins"),
                        res.getInt("solo_kills"),
                        res.getInt("solo_games"),
                        res.getInt("team_wins"),
                        res.getInt("team_kills"),
                        res.getInt("team_games")
                );
                player.setPlayerData(playerData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveData(SWPlayer player) {
        SWPlayerData swpd = player.getPlayerData();
        SWPlayerStats swps = swpd.getStats();

        try (Connection conn = storage.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE `" + table + "` SET solo_wins=?, solo_kills=?, solo_games=?, team_wins=?, team_kills=?, team_games=? WHERE `uuid`=?")) {
            ps.setInt(1, swps.getSoloWins());
            ps.setInt(2, swps.getSoloKills());
            ps.setInt(3, swps.getSoloGamesPlayed());
            ps.setInt(4, swps.getTeamKills());
            ps.setInt(5, swps.getTeamWins());
            ps.setInt(6, swps.getTeamGamesPlayed());
            ps.setString(7, player.getUuid().toString());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
