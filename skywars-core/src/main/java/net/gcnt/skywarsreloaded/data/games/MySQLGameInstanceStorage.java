package net.gcnt.skywarsreloaded.data.games;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.data.CoreMySQLStorage;
import net.gcnt.skywarsreloaded.game.GameInstance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQLGameInstanceStorage extends CoreMySQLStorage<GameInstance> implements GameInstanceStorage {

    public MySQLGameInstanceStorage(SkyWarsReloaded plugin) {
        super(plugin, "sw_game_instances");
    }

    @Override
    public void createTable(Connection connection) throws SQLException {
        connection.createStatement().executeUpdate("CREATE TABLE `" + table + "` ( \n" +
                "  `id` VARCHAR NOT NULL , \n" +
                "  `template_id` INT NULL DEFAULT NULL , \n" +
                "  `server` VARCHAR NULL DEFAULT NULL ,\n" +
                "  `playercount` INT NOT NULL DEFAULT '0' , \n" +
                "  `status` VARCHAR NOT NULL , \n" +
                "  `created_at` INT NOT NULL DEFAULT CURRENT_TIMESTAMP , \n" +
                "  `updated_at` INT NOT NULL DEFAULT CURRENT_TIMESTAMP , \n" +
                "  PRIMARY KEY (`id`)\n" +
                ") ENGINE = InnoDB;");
    }

    @Override
    public void removeGameInstance(String uuid) {
        try (Connection conn = getConnection()) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM `" + table + "` WHERE `id`=?");
            ps.setString(1, uuid);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public GameInstance getGameInstanceById(String uuid) {
        // todo
    }
}
