package net.gcnt.skywarsreloaded.data.sql.tables;

import net.gcnt.skywarsreloaded.data.player.PlayerStorage;
import net.gcnt.skywarsreloaded.data.player.SWPlayerData;
import net.gcnt.skywarsreloaded.data.player.SWPlayerStats;
import net.gcnt.skywarsreloaded.data.sql.CoreSQLTable;
import net.gcnt.skywarsreloaded.data.sql.SQLStorage;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLPlayerTable extends CoreSQLTable<SWPlayer> implements PlayerStorage {

    private final SQLPlayerStatsTable statsTable;

    public SQLPlayerTable(SQLStorage storage) {
        super(storage, "sw_player_data");

        this.statsTable = new SQLPlayerStatsTable(storage);
    }

    @Override
    public void createTable(Connection connection) throws SQLException {
        connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
                "`uuid`   VARCHAR(255)  NOT NULL UNIQUE," +
                "`selected_solo_cage` VARCHAR(100) DEFAULT NULL," +
                "`selected_team_cage` VARCHAR(100) DEFAULT NULL," +
                "`selected_particle` VARCHAR(100) DEFAULT NULL," +
                "`selected_kill_effect` VARCHAR(100) DEFAULT NULL," +
                "`selected_win_effect` VARCHAR(100) DEFAULT NULL," +
                "`selected_projectile_effect` VARCHAR(100) DEFAULT NULL," +
                "`selected_kill_messages_theme` VARCHAR(100) DEFAULT NULL," +
                "KEY  (`uuid`))");
    }

    protected void createDefault(SWPlayer player, Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("INSERT INTO `" + table + "`(`uuid`) VALUES (?)");
        ps.setString(1, player.getUuid().toString());
        ps.executeUpdate();

        SWPlayerData swpd = storage.getPlugin().getPlayerDataManager().createSWPlayerDataInstance();
        SWPlayerStats swps = storage.getPlugin().getPlayerDataManager().createSWPlayerStatsInstance();
        swps.initData(0, 0, 0, 0, 0, 0);
        swpd.initData(swps, null, null, null, null, null, null, null, null);
        player.setPlayerData(swpd);
    }

    @Override
    public void loadData(SWPlayer player) {
        try (Connection conn = storage.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM `" + table + "` WHERE `uuid`=?");
            ps.setString(1, player.getUuid().toString());
            ResultSet res = ps.executeQuery();

            if (!res.next()) {
                createDefault(player, conn);
                return;
            }

            SWPlayerData swpd = storage.getPlugin().getPlayerDataManager().createSWPlayerDataInstance();
            SWPlayerStats swps = storage.getPlugin().getPlayerDataManager().createSWPlayerStatsInstance();
            swps.initData(
                    res.getInt("solo_wins"),
                    res.getInt("solo_kills"),
                    res.getInt("solo_games"),
                    res.getInt("team_wins"),
                    res.getInt("team_kills"),
                    res.getInt("team_games")
            );
            swpd.initData(swps,
                    res.getString("selected_solo_cage"),
                    res.getString("selected_team_cage"),
                    res.getString("selected_particle"),
                    res.getString("selected_kill_effect"),
                    res.getString("selected_win_effect"),
                    res.getString("selected_projectile_effect"),
                    res.getString("selected_kill_messages_theme"),
                    res.getString("selected_kit")
            );
            // unlockables
            player.setPlayerData(swpd);
            res.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveData(SWPlayer player) {
        SWPlayerData swpd = player.getPlayerData();
        SWPlayerStats swps = swpd.getStats();

        try (Connection conn = storage.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("UPDATE `" + table + "` SET solo_wins=?, solo_kills=?, solo_games=?, team_wins=?, team_kills=?, team_games=? WHERE `uuid`=?");
            ps.setInt(1, swps.getSoloWins());
            ps.setInt(2, swps.getSoloKills());
            ps.setInt(3, swps.getSoloGamesPlayed());
            ps.setInt(4, swps.getTeamKills());
            ps.setInt(5, swps.getTeamWins());
            ps.setInt(6, swps.getTeamGamesPlayed());

            ps.setString(7, player.getUuid().toString());

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setProperty(String property, Object value, SWPlayer player) {
        try (Connection conn = storage.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("UPDATE `" + table + "` SET ?=? WHERE `uuid`=?");

            ps.setString(1, property);
            bindPropertyValue(ps, 2, value);
            ps.setString(3, player.getUuid().toString());

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
