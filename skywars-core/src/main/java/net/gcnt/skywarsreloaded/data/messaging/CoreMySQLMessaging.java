package net.gcnt.skywarsreloaded.data.messaging;

import net.gcnt.skywarsreloaded.data.sql.CoreMySQLStorage;
import net.gcnt.skywarsreloaded.data.sql.CoreSQLTable;
import net.gcnt.skywarsreloaded.event.CoreSWMessageReceivedEvent;
import net.gcnt.skywarsreloaded.event.CoreSWMessageSentEvent;
import net.gcnt.skywarsreloaded.utils.properties.ConfigProperties;
import net.gcnt.skywarsreloaded.wrapper.scheduler.CoreSWRunnable;
import net.gcnt.skywarsreloaded.wrapper.scheduler.SWRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class CoreMySQLMessaging extends CoreSQLTable<SWMessage> implements SWMySQLMessaging {

    private final long CLEANUP_TIMEOUT = 30 * 1000; // 30s
    private final ConcurrentHashMap<SWMessage, CompletableFuture<SWMessage>> cachedOutgoing;
    private int lastFetchedId;
    private SWRunnable updateTask;
    private SWRunnable cleanupTask;

    public CoreMySQLMessaging(CoreMySQLStorage storage) {
        super(storage, storage.getPlugin().getConfig().getString(ConfigProperties.STORAGE_MYSQL_TABLE_PREFIX.toString()) + "messaging");

        this.cachedOutgoing = new ConcurrentHashMap<>();
        this.updateTask = null;
        this.cleanupTask = null;
    }

    @Override
    public void setup() {
        this.startListening();
        this.startCleaning(); // lukas getting busy
    }

    @Override
    public void removeMessage(SWMessage message, boolean withReplies) {
        String query = "DELETE FROM `" + table + "` WHERE `id`=?" + (withReplies ? " OR `reply_to_id`=?" : "");
        try (Connection connection = storage.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            bindPropertyValue(statement, 1, message.getId());
            if (withReplies) {
                bindPropertyValue(statement, 2, message.getId());
            }

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public CompletableFuture<SWMessage> sendMessage(SWMessage message) {
        CompletableFuture<SWMessage> callback = new CompletableFuture<>();

        String query = "INSERT INTO `" + table + "` (`payload`, `channel`, `origin_server`, `target_server`, `reply_to_id` VALUES (?, ?, ?, ?, ?);" +
                "SELECT `id` FROM `" + table + "` ORDER BY timestamp DESC LIMIT 1";
        try (Connection connection = storage.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            bindPropertyValue(statement, 1, message.getPayload());
            bindPropertyValue(statement, 2, message.getChannel());
            bindPropertyValue(statement, 3, message.getOriginServer());
            bindPropertyValue(statement, 4, message.getTargetServer());
            bindPropertyValue(statement, 5, message.getReplyToId());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                message.setId(resultSet.getInt("id"));
                this.cachedOutgoing.put(message, callback);
            }

            CoreSWMessageSentEvent event = new CoreSWMessageSentEvent(message);
            storage.getPlugin().getEventManager().callEvent(event);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return callback;
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
    public void startListening() {
        updateTask = storage.getPlugin().getScheduler().runAsyncTimer(new CoreSWRunnable() {
            @Override
            public void run() {
                // todo add deleting messages that are older than 10 seconds, only for the main proxy lobby server.
                String query = "SELECT * FROM `" + table + "` " +
                        "WHERE (`target_server` IS NULL OR `target_server` = ?) " +
                        "   AND (`origin_server` != ) " +
                        "   AND `id` > ? " +
                        "   AND (`timestamp` >= NOW() - INTERVAL 2 SECOND)";

                try (Connection connection = storage.getConnection();
                     PreparedStatement statement = connection.prepareStatement(query)) {

                    bindPropertyValue(statement, 1, storage.getPlugin().getConfig().getString(ConfigProperties.PROXY_SERVER_NAME.toString()));
                    bindPropertyValue(statement, 2, lastFetchedId);

                    try (final ResultSet resultSet = statement.executeQuery()) {
                        while (resultSet.next()) {
                            int id = resultSet.getInt("id");

                            String payload = resultSet.getString("payload");
                            String channel = resultSet.getString("channel");
                            String originServer = resultSet.getString("origin_server");
                            String targetServer = resultSet.getString("target_server");
                            int replyToId = resultSet.getInt("reply_to_id");
                            long timestamp = resultSet.getTimestamp("timestamp").getTime();

                            SWMessage message = new CoreSWMessage(storage.getPlugin(), id, channel, payload, originServer, targetServer, replyToId, timestamp);
                            lastFetchedId = id;

                            if (message.getReplyToId() != -1) {
                                cachedOutgoing.entrySet().stream()
                                        .filter(entry -> entry.getKey().getId() == message.getReplyToId())
                                        .map(Map.Entry::getValue)
                                        .findAny()
                                        .ifPresent(foundFuture -> foundFuture.complete(message));
                            }

                            CoreSWMessageReceivedEvent event = new CoreSWMessageReceivedEvent(message);
                            storage.getPlugin().getEventManager().callEvent(event);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 20);
    }

    @Override
    public void startCleaning() {
        cleanupTask = storage.getPlugin().getScheduler().runAsyncTimer(new CoreSWRunnable() {
            @Override
            public void run() {

                List<SWMessage> toRemove = new ArrayList<>();
                cachedOutgoing.forEach((msg, future) -> {
                    if (System.currentTimeMillis() - msg.getTimestamp() > CLEANUP_TIMEOUT) {
                        toRemove.add(msg);
                    }
                });

                for (SWMessage msg : toRemove) cachedOutgoing.remove(msg);
            }
        }, 0, 200);
    }

    @Override
    public void stopListening() {
        if (updateTask != null) {
            updateTask.cancel();
        }
    }

}
