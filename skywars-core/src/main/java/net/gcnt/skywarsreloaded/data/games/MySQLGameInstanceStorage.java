package net.gcnt.skywarsreloaded.data.games;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.data.CoreMySQLStorage;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.gameinstance.GameInstance;
import net.gcnt.skywarsreloaded.game.gameinstance.RemoteGameInstance;
import net.gcnt.skywarsreloaded.manager.gameinstance.RemoteGameInstanceManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLGameInstanceStorage extends CoreMySQLStorage<GameInstance> implements GameInstanceStorage {

    public MySQLGameInstanceStorage(SkyWarsReloaded plugin) {
        super(plugin, "sw_game_instances");
    }

    @Override
    public void createTable(Connection connection) throws SQLException {
        connection.createStatement().executeUpdate("CREATE TABLE `" + table + "` ( \n" +
                "  `id` VARCHAR NOT NULL , \n" +
                "  `template` INT NULL DEFAULT NULL , \n" + WAIT // error on purpose here: isn't this supposed to be template_id?
                "  `server` VARCHAR NULL DEFAULT NULL ,\n" + // and this server_id?
                "  `playercount` INT NOT NULL DEFAULT '0' , \n" +
                "  `state` VARCHAR NOT NULL , \n" +
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
    public void removeOldInstances() {
        try (Connection conn = getConnection()) {
            try (final Statement statement = conn.createStatement()) {
                statement.executeUpdate("DELETE FROM `" + table + "` WHERE `updated_at` <= NOW() - INTERVAL 5 SECOND");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public GameInstance getGameInstanceById(String uuid) {
        return null;
    }

    @Override
    public List<RemoteGameInstance> getGameInstances() {
        List<RemoteGameInstance> instances = new ArrayList<>();
        try (Connection conn = getConnection();
             final Statement statement = conn.createStatement();
             final ResultSet resultSet = statement.executeQuery("SELECT * FROM `" + table + "`)")) {
            while (resultSet.next()) {
                final RemoteGameInstanceManager gameManager = (RemoteGameInstanceManager) plugin.getGameInstanceManager();
                // todo create or update a game instance here.
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return instances;
    }

    @Override
    public List<GameInstance> getGameInstancesByTemplate(GameTemplate template) {
        List<GameInstance> instances = new ArrayList<>();
        try (Connection conn = getConnection()) {
            final PreparedStatement statement = conn.prepareStatement("SELECT * FROM `" + table + "` WHERE `template` = ?");
            bindPropertyValue(statement, 1, template.getName());
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                // todo create or update a game instance here.
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return instances;
    }

    @Override
    public void addGameInstance(GameInstance gameInstance) {
        try (Connection conn = getConnection()) {
            final PreparedStatement statement = conn.prepareStatement("INSERT INTO `" + table + "` (`id`, `template`, `server`, `playercount`, `state`) VALUES (?, ?, ?, ?, ?)");
            bindPropertyValue(statement, 1, gameInstance.getId());
            bindPropertyValue(statement, 2, gameInstance.getTemplate().getName());
            bindPropertyValue(statement, 3, gameInstance instanceof RemoteGameInstance ? ((RemoteGameInstance) gameInstance).getServerProxyName() : null);
            bindPropertyValue(statement, 4, gameInstance.getPlayerCount());
            bindPropertyValue(statement, 5, gameInstance.getState().name());

            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateGameInstance(GameInstance gameInstance) {
        try (Connection conn = getConnection()) {
            PreparedStatement ps = conn.prepareStatement("UPDATE `" + table + "` SET `playercount`=?, `state`=?, `updated_at`=NOW() WHERE `id`=?");
            bindPropertyValue(ps, 1, gameInstance.getPlayerCount());
            bindPropertyValue(ps, 2, gameInstance.getState().name());
            bindPropertyValue(ps, 3, gameInstance.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startAutoUpdating() {

    }

    @Override
    public void stopAutoUpdating() {

    }

    @Override
    public void removeGameInstance(GameInstance gameInstance) {
        try (Connection conn = getConnection()) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM `" + table + "` WHERE `id`=?");
            bindPropertyValue(ps, 1, gameInstance.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
