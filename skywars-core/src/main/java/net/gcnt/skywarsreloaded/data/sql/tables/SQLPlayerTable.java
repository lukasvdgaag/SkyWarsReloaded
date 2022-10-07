package net.gcnt.skywarsreloaded.data.sql.tables;

import net.gcnt.skywarsreloaded.data.player.SWPlayerStorage;
import net.gcnt.skywarsreloaded.data.player.SWUnlockablesStorage;
import net.gcnt.skywarsreloaded.data.player.stats.SWPlayerData;
import net.gcnt.skywarsreloaded.data.player.stats.SWPlayerStats;
import net.gcnt.skywarsreloaded.data.player.stats.SWPlayerUnlockables;
import net.gcnt.skywarsreloaded.data.sql.CoreSQLTable;
import net.gcnt.skywarsreloaded.data.sql.SQLStorage;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLPlayerTable extends CoreSQLTable<SWPlayer> implements SWPlayerStorage {

    protected SQLPlayerStatsTable statsTable;
    protected SQLPlayerUnlockablesTable unlockablesTable;

    public SQLPlayerTable(SQLStorage storage) {
        super(storage, "player_data");

        initStatsTable();
        initUnlockablesTable();
    }

    @Override
    public void initStatsTable() {
        this.statsTable = new SQLPlayerStatsTable(storage);
    }

    @Override
    public void initUnlockablesTable() {
        this.unlockablesTable = new SQLPlayerUnlockablesTable(storage);
    }

    @Override
    public SWUnlockablesStorage getUnlockablesStorage() {
        return this.unlockablesTable;
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

        SWPlayerData playerData = storage.getPlugin().getPlayerDataManager().createSWPlayerDataInstance();
        SWPlayerStats stats = storage.getPlugin().getPlayerDataManager().createSWPlayerStatsInstance();
        SWPlayerUnlockables unlockables = storage.getPlugin().getPlayerDataManager().createSWPlayerUnlockablesInstance(player);
        stats.initData(0, 0, 0, 0, 0, 0);
        playerData.initData(stats, unlockables, null, null, null, null, null, null, null, null);
        player.setPlayerData(playerData);
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

                SWPlayerData playerData = storage.getPlugin().getPlayerDataManager().createSWPlayerDataInstance();
                SWPlayerStats stats = storage.getPlugin().getPlayerDataManager().createSWPlayerStatsInstance();
                SWPlayerUnlockables unlockables = storage.getPlugin().getPlayerDataManager().createSWPlayerUnlockablesInstance(player);
                playerData.initData(stats,
                        unlockables,
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
                player.setPlayerData(playerData);
                statsTable.loadData(player);
                unlockablesTable.loadData(player);
            }
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
