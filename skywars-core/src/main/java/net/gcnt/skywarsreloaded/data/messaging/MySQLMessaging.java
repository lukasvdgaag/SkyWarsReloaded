package net.gcnt.skywarsreloaded.data.messaging;

import net.gcnt.skywarsreloaded.data.sql.CoreMySQLStorage;
import net.gcnt.skywarsreloaded.data.sql.CoreSQLTable;
import net.gcnt.skywarsreloaded.utils.properties.ConfigProperties;
import net.gcnt.skywarsreloaded.wrapper.scheduler.CoreSWRunnable;
import net.gcnt.skywarsreloaded.wrapper.scheduler.SWRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class MySQLMessaging extends CoreSQLTable<SWMessage> implements SWMessaging {

    private final HashMap<Integer, SWMessage> messages;
    private SWRunnable updateTask;

    public MySQLMessaging(CoreMySQLStorage storage) {
        super(storage, "sw_messaging");

        this.messages = new HashMap<>();
        this.updateTask = null;
    }

    @Override
    public void setup() {
    }

    @Override
    public void removeMessage(SWMessage message, boolean withReplies) {
        String query = "DELETE FROM `" + table + "` WHERE `id`=?" + (withReplies ? " OR `reply_to_id`=?" : "");
        try (Connection connection = storage.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, message.getId());
            if (withReplies) {
                statement.setInt(2, message.getId());
            }

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessage(SWMessage message) {
        String query = "INSERT INTO `" + table + "` (`payload`, `channel`, `origin_server`, `target_server`, `reply_to_id` VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = storage.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            bindPropertyValue(statement, 1, message.getPayload());
            bindPropertyValue(statement, 2, message.getChannel());
            bindPropertyValue(statement, 3, message.getOriginServer());
            bindPropertyValue(statement, 4, message.getTargetServer());
            bindPropertyValue(statement, 5, message.getReplyToId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void replyMessage(SWMessage message, SWMessage replyTo) {
        message.setReplyToId(replyTo.getId());
        message.setChannel(replyTo.getChannel());
        sendMessage(message);
    }

    @Override
    public SWMessage createMessage(String channel, String payload) {
        return new CoreSWMessage(storage.getPlugin(), channel, payload);
    }

    @Override
    public void createTable(Connection connection) throws SQLException {
        connection.createStatement().executeUpdate("CREATE TABLE `" + table + "` ( " +
                "`id` INT UNSIGNED NOT NULL AUTO_INCREMENT , " +
                "`payload` TEXT NOT NULL , " +
                "`channel` VARCHAR(255) NOT NULL , " +
                "`origin_server` VARCHAR(255) NOT NULL , " +
                "`target_server` VARCHAR(255) NULL DEFAULT NULL , " +
                "`reply_to_id` INT UNSIGNED NULL DEFAULT NULL , " +
                "`timestamp` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP , " +
                "PRIMARY KEY (`id`) ," +
                "FOREIGN KEY (`reply_to_id`) REFERENCES `" + table + "`(`id`) ON DELETE CASCADE ON UPDATE CASCADE) " +
                "ENGINE = InnoDB; ");
    }

    @Override
    public void startFetching() {
        updateTask = storage.getPlugin().getScheduler().runAsyncTimer(new CoreSWRunnable() {
            @Override
            public void run() {
                // todo add deleting messages that are older than 10 seconds, only for the main proxy lobby server.
                String query = "SELECT * FROM `" + table + "` WHERE (`target_server` IS NULL OR `target_server`=?) AND (`timestamp` <= NOW() - INTERVAL 2 SECOND)";
                try (Connection connection = storage.getConnection();
                     PreparedStatement statement = connection.prepareStatement(query)) {

                    bindPropertyValue(statement, 1, storage.getPlugin().getConfig().getString(ConfigProperties.SERVER_NAME.toString()));

                    try (final ResultSet resultSet = statement.executeQuery()) {
                        while (resultSet.next()) {
                            int id = resultSet.getInt("id");
                            if (!isCached(id)) continue;

                            String payload = resultSet.getString("payload");
                            String channel = resultSet.getString("channel");
                            String originServer = resultSet.getString("origin_server");
                            String targetServer = resultSet.getString("target_server");
                            int replyToId = resultSet.getInt("reply_to_id");
                            long timestamp = resultSet.getTimestamp("timestamp").getTime();

                            SWMessage message = new CoreSWMessage(storage.getPlugin(), id, channel, payload, originServer, targetServer, replyToId, timestamp);
                            cacheMessage(message);
                            // todo throw message received event here.
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 20);
    }

    @Override
    public void stopFetching() {
        if (updateTask != null) {
            updateTask.cancel();
        }
    }

    public void cacheMessage(SWMessage message) {
        if (!isCached(message.getId())) {
            this.messages.put(message.getId(), message);
        }
    }

    public boolean isCached(int id) {
        return this.messages.containsKey(id);
    }

    public void removeCachedMessage(SWMessage message) {
        this.messages.remove(message.getId());
    }

    public SWMessage getCachedMessage(SWMessage message) {
        return this.messages.get(message.getId());
    }

}
