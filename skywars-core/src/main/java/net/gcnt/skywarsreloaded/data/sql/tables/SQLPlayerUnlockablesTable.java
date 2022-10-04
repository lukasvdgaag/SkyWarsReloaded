package net.gcnt.skywarsreloaded.data.sql.tables;

import net.gcnt.skywarsreloaded.data.player.SWPlayerStorageUnit;
import net.gcnt.skywarsreloaded.data.player.SWUnlockablesStorage;
import net.gcnt.skywarsreloaded.data.player.stats.SWPlayerUnlockables;
import net.gcnt.skywarsreloaded.data.sql.CoreSQLTable;
import net.gcnt.skywarsreloaded.data.sql.SQLStorage;
import net.gcnt.skywarsreloaded.unlockable.Unlockable;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLPlayerUnlockablesTable extends CoreSQLTable<SWPlayer> implements SWPlayerStorageUnit, SWUnlockablesStorage {

    public SQLPlayerUnlockablesTable(SQLStorage storage) {
        super(storage, "sw_player_unlockables");
    }

    @Override
    public void createTable(Connection connection) throws SQLException {
        connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
                "`uuid`   VARCHAR(255)  NOT NULL UNIQUE," +
                "`type`   VARCHAR(30) NOT NULL," +
                "`name`   VARCHAR(255) NOT NULL," +
                "`timestamp` TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "KEY (`uuid`))");
    }

    @Override
    public void loadData(SWPlayer player) {
        try (Connection conn = storage.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM `" + table + "` WHERE `uuid`=?")) {
            ps.setString(1, player.getUuid().toString());

            try (ResultSet res = ps.executeQuery()) {
                final SWPlayerUnlockables unlockables = player.getPlayerData().getUnlockables();
                while (res.next()) {
                    unlockables.initUnlockable(res.getString("type"), res.getString("name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveData(SWPlayer player) {
        // unlockables are saved instantly when unlocked, so this is not needed.
    }

    @Override
    public void addUnlockable(SWPlayer player, Unlockable unlockable) {
        try (Connection conn = storage.getConnection()) {
            if (!hasUnlockable(conn, player, unlockable)) {
                try (PreparedStatement ps = conn.prepareStatement("INSERT INTO `" + table + "` (`uuid`, `type`, `name`) VALUES (?, ?, ?)")) {
                    ps.setString(1, player.getUuid().toString());
                    ps.setString(2, unlockable.getType().name());
                    ps.setString(3, unlockable.getId());

                    ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeUnlockable(SWPlayer player, Unlockable unlockable) {
        try (Connection conn = storage.getConnection()) {
            if (!hasUnlockable(conn, player, unlockable)) {
                try (PreparedStatement ps = conn.prepareStatement("DELETE FROM `" + table + "` WHERE `uuid`=? AND `type`=? AND `name`=?")) {
                    ps.setString(1, player.getUuid().toString());
                    ps.setString(2, unlockable.getType().name());
                    ps.setString(3, unlockable.getId());

                    ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean hasUnlockable(SWPlayer player, Unlockable unlockable) {
        try (Connection connection = storage.getConnection()) {
            return hasUnlockable(connection, player, unlockable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean hasUnlockable(Connection connection, SWPlayer player, Unlockable unlockable) {
        try (PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM `" + table + "` WHERE `uuid` = ? AND `type` = ? AND `name` = ?")) {
            ps.setString(1, player.getUuid().toString());
            ps.setString(2, unlockable.getType().name());
            ps.setString(3, unlockable.getId());

            try (ResultSet res = ps.executeQuery()) {
                return res.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
