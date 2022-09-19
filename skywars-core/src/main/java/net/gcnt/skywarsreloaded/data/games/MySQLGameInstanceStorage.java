package net.gcnt.skywarsreloaded.data.games;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.data.CoreMySQLStorage;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.gameinstance.CoreRemoteGameInstance;
import net.gcnt.skywarsreloaded.game.gameinstance.GameInstance;
import net.gcnt.skywarsreloaded.game.gameinstance.RemoteGameInstance;
import net.gcnt.skywarsreloaded.game.types.GameState;
import net.gcnt.skywarsreloaded.manager.gameinstance.RemoteGameInstanceManager;
import net.gcnt.skywarsreloaded.wrapper.scheduler.CoreSWRunnable;
import net.gcnt.skywarsreloaded.wrapper.scheduler.SWRunnable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MySQLGameInstanceStorage extends CoreMySQLStorage<GameInstance> implements GameInstanceStorage {

    private SWRunnable updateTask;

    public MySQLGameInstanceStorage(SkyWarsReloaded plugin) {
        super(plugin, "sw_game_instances");

        updateTask = null;
    }

    @Override
    public void createTable(Connection connection) throws SQLException {
        connection.createStatement().executeUpdate("CREATE TABLE `" + table + "` ( \n" +
                "  `id` VARCHAR NOT NULL , \n" +
                "  `template` INT NULL DEFAULT NULL , \n" +
                "  `server` VARCHAR NULL DEFAULT NULL ,\n" +
                "  `playercount` INT NOT NULL DEFAULT '0' , \n" +
                "  `state` VARCHAR NOT NULL , \n" +
                "  `created_at` INT NOT NULL DEFAULT CURRENT_TIMESTAMP , \n" +
                "  `updated_at` INT NOT NULL DEFAULT CURRENT_TIMESTAMP , \n" +
                "  PRIMARY KEY (`id`)\n" +
                ") ENGINE = InnoDB;");
    }

    @Override
    public void removeGameInstance(String uuid) {
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement("DELETE FROM `" + table + "` WHERE `id`=?")) {
            ps.setString(1, uuid);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeOldInstances() {
        try (Connection conn = getConnection();
             final Statement statement = conn.createStatement()) {
            statement.executeUpdate("DELETE FROM `" + table + "` WHERE `updated_at` <= NOW() - INTERVAL 5 SECOND");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public GameInstance getGameInstanceById(String uuid) {
        try (Connection conn = getConnection();
             final PreparedStatement statement = conn.prepareStatement("SELECT * FROM `" + table + "` WHERE `id`=?")) {
            bindPropertyValue(statement, 1, uuid);

            try (final ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return getRemoteInstanceFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<RemoteGameInstance> fetchGameInstances() {
        List<RemoteGameInstance> instances = new ArrayList<>();
        try (Connection conn = getConnection();
             final Statement statement = conn.createStatement();
             final ResultSet resultSet = statement.executeQuery("SELECT * FROM `" + table + "`)")) {
            while (resultSet.next()) {
                RemoteGameInstance instance = getRemoteInstanceFromResultSet(resultSet);
                if (instance == null) continue;

                instances.add(instance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return instances;
    }

    protected RemoteGameInstance getRemoteInstanceFromResultSet(ResultSet resultSet) throws SQLException {
        final GameTemplate template = plugin.getGameInstanceManager().getGameTemplateByName(resultSet.getString("template"));
        // Trying to load a game instance with a template that doesn't exist on this server.
        if (resultSet.getString("template") != null && template == null) {
            plugin.getLogger().error("Failed to load game instance " + resultSet.getString("id") + " because the template " + resultSet.getString("template") + " doesn't exist on this server.");
            return null;
        }

        return new CoreRemoteGameInstance(
                plugin,
                resultSet.getString("server"),
                UUID.fromString(resultSet.getString("id")),
                template,
                GameState.valueOf(resultSet.getString("state"))
        );
    }

    @Override
    public List<GameInstance> getGameInstancesByTemplate(GameTemplate template) {
        List<GameInstance> instances = new ArrayList<>();
        try (Connection conn = getConnection(); final PreparedStatement statement = conn.prepareStatement("SELECT * FROM `" + table + "` WHERE `template` = ?")) {
            bindPropertyValue(statement, 1, template.getName());
            try (final ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    RemoteGameInstance instance = new CoreRemoteGameInstance(
                            plugin,
                            resultSet.getString("server"),
                            UUID.fromString(resultSet.getString("id")),
                            template,
                            GameState.valueOf(resultSet.getString("state"))
                    );

                    instances.add(instance);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return instances;
    }

    @Override
    public void addGameInstance(GameInstance gameInstance) {
        try (Connection conn = getConnection();
             final PreparedStatement statement = conn.prepareStatement("INSERT INTO `" + table + "` (`id`, `template`, `server`, `playercount`, `state`) VALUES (?, ?, ?, ?, ?)")) {
            bindPropertyValue(statement, 1, gameInstance.getId());
            bindPropertyValue(statement, 2, gameInstance.getTemplate().getName());
            bindPropertyValue(statement, 3, gameInstance instanceof RemoteGameInstance ? ((RemoteGameInstance) gameInstance).getServerProxyName() : null);
            bindPropertyValue(statement, 4, gameInstance.getPlayerCount());
            bindPropertyValue(statement, 5, gameInstance.getState().name());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateGameInstance(GameInstance gameInstance) {
        try (Connection conn = getConnection();
             final PreparedStatement ps = conn.prepareStatement("UPDATE `" + table + "` SET `playercount`=?, `state`=?, `updated_at`=NOW() WHERE `id`=?")) {
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
        this.updateTask = plugin.getScheduler().runAsyncTimer(new CoreSWRunnable() {
            @Override
            public void run() {
                List<RemoteGameInstance> fetchedInstances = fetchGameInstances();
                RemoteGameInstanceManager instanceManager = (RemoteGameInstanceManager) plugin.getGameInstanceManager();

                // decaching instances that are no longer in the database
                for (RemoteGameInstance instance : instanceManager.getGameInstancesList()) {
                    if (!fetchedInstances.contains(instance)) {
                        instanceManager.removeCachedGameInstance(instance.getId());
                    }
                }

                // updating or caching the fetched instances
                for (RemoteGameInstance fetched : fetchedInstances) {
                    if (instanceManager.isGameInstanceCached(fetched.getId())) {
                        instanceManager.updateCachedGameInstance(fetched);
                    } else {
                        instanceManager.addCachedGameInstance(fetched);
                    }
                }
            }
        }, 0, 40);
    }

    @Override
    public void stopAutoUpdating() {
        if (this.updateTask != null) {
            this.updateTask.cancel();
        }
    }

    @Override
    public void removeGameInstance(GameInstance gameInstance) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM `" + table + "` WHERE `id`=?")) {
            bindPropertyValue(ps, 1, gameInstance.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
