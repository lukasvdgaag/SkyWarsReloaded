package net.gcnt.skywarsreloaded.data.sql.tables;

import net.gcnt.skywarsreloaded.data.games.GameInstanceStorage;
import net.gcnt.skywarsreloaded.data.sql.CoreSQLTable;
import net.gcnt.skywarsreloaded.data.sql.SQLStorage;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.gameinstance.CoreRemoteGameInstance;
import net.gcnt.skywarsreloaded.game.gameinstance.GameInstance;
import net.gcnt.skywarsreloaded.game.gameinstance.LocalGameInstance;
import net.gcnt.skywarsreloaded.game.gameinstance.RemoteGameInstance;
import net.gcnt.skywarsreloaded.game.types.GameState;
import net.gcnt.skywarsreloaded.manager.gameinstance.RemoteGameInstanceManager;
import net.gcnt.skywarsreloaded.wrapper.scheduler.CoreSWRunnable;
import net.gcnt.skywarsreloaded.wrapper.scheduler.SWRunnable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SQLGameInstanceTable extends CoreSQLTable<GameInstance> implements GameInstanceStorage {

    private SWRunnable updateTask;

    public SQLGameInstanceTable(SQLStorage storage) {
        super(storage, "sw_game_instances");

        updateTask = null;
    }

    @Override
    public void createTable(Connection connection) throws SQLException {
        connection.createStatement().executeUpdate("CREATE TABLE `" + table + "` ( \n" +
                "  `id` VARCHAR NOT NULL , \n" +
                "  `template_id` INT NULL DEFAULT NULL , \n" + // unique name of the template
                "  `server_id` VARCHAR NULL DEFAULT NULL ,\n" + // proxy's sub-server identifier
                "  `playercount` INT NOT NULL DEFAULT '0' , \n" +
                "  `state` VARCHAR NOT NULL , \n" +
                "  `created_at` INT NOT NULL DEFAULT CURRENT_TIMESTAMP , \n" +
                "  `updated_at` INT NOT NULL DEFAULT CURRENT_TIMESTAMP , \n" +
                "  PRIMARY KEY (`id`)\n" +
                ") ENGINE = InnoDB;");
    }

    @Override
    public void removeGameInstance(UUID uuid) {
        try (Connection conn = storage.getConnection(); PreparedStatement ps = conn.prepareStatement("DELETE FROM `" + table + "` WHERE `id`=?")) {
            ps.setString(1, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeOldInstances() {
        try (Connection conn = storage.getConnection();
             final Statement statement = conn.createStatement()) {
            statement.executeUpdate("DELETE FROM `" + table + "` WHERE `updated_at` <= NOW() - INTERVAL 5 SECOND");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public RemoteGameInstance getGameInstanceById(UUID uuid) {
        try (Connection conn = storage.getConnection();
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
        try (Connection conn = storage.getConnection();
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
        String template_id = resultSet.getString("template_id");
        final GameTemplate template = storage.getPlugin().getGameInstanceManager().getGameTemplateByName(template_id);
        // Trying to load a game instance with a template that doesn't exist on this server.
        if (resultSet.getString("template") != null && template == null) {
            storage.getPlugin().getLogger().error("Failed to load game instance " + resultSet.getString("id") + " because the template " + template_id + " doesn't exist on this server.");
            return null;
        }

        return new CoreRemoteGameInstance(
                storage.getPlugin(),
                UUID.fromString(resultSet.getString("id")), template, resultSet.getString("server_id"),
                GameState.valueOf(resultSet.getString("state"))
        );
    }

    @Override
    public List<RemoteGameInstance> getGameInstancesByTemplate(GameTemplate template) {
        List<RemoteGameInstance> instances = new ArrayList<>();
        try (Connection conn = storage.getConnection(); final PreparedStatement statement = conn.prepareStatement("SELECT * FROM `" + table + "` WHERE `template_id` = ?")) {
            bindPropertyValue(statement, 1, template.getName());
            try (final ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    RemoteGameInstance instance = new CoreRemoteGameInstance(
                            storage.getPlugin(),
                            UUID.fromString(resultSet.getString("id")), template, resultSet.getString("server_id"),
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
    public void updateGameInstance(LocalGameInstance gameInstance) {
        try (Connection conn = storage.getConnection();
             final PreparedStatement statement = conn.prepareStatement("INSERT INTO `" + table + "` (`id`, `template_id`, `server_id`, `playercount`, `state`) VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE template_id=?,server_id=?,playercount=?,state=?")) {

            UUID id = gameInstance.getId();
            String templateName = gameInstance.getTemplate().getName();
            Object serverName = gameInstance instanceof RemoteGameInstance ? ((RemoteGameInstance) gameInstance).getServerProxyName() : null;
            int playerCount = gameInstance.getPlayerCount();
            String state = gameInstance.getState().name();

            bindPropertyValue(statement, 1, id);
            bindPropertyValue(statement, 2, templateName);
            bindPropertyValue(statement, 3, serverName);
            bindPropertyValue(statement, 4, playerCount);
            bindPropertyValue(statement, 5, state);

            bindPropertyValue(statement, 6, templateName);
            bindPropertyValue(statement, 7, serverName);
            bindPropertyValue(statement, 8, playerCount);
            bindPropertyValue(statement, 9, state);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startAutoUpdating() {
        this.updateTask = storage.getPlugin().getScheduler().runAsyncTimer(new CoreSWRunnable() {
            @Override
            public void run() {
                List<RemoteGameInstance> fetchedInstances = fetchGameInstances();
                RemoteGameInstanceManager instanceManager = (RemoteGameInstanceManager) storage.getPlugin().getGameInstanceManager();

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
        try (Connection conn = storage.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM `" + table + "` WHERE `id`=?")) {
            bindPropertyValue(ps, 1, gameInstance.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
