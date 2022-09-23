package net.gcnt.skywarsreloaded.data.messaging;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.data.CoreMySQLStorage;
import net.gcnt.skywarsreloaded.utils.properties.ConfigProperties;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQLMessaging extends CoreMySQLStorage<SWMessage> implements SWMessaging {

    public MySQLMessaging(SkyWarsReloaded plugin) {
        super(plugin, "sw_messaging");
    }

    @Override
    public void setup() {
        String username = plugin.getConfig().getString(ConfigProperties.STORAGE_USERNAME.toString());
        String password = plugin.getConfig().getString(ConfigProperties.STORAGE_PASSWORD.toString());
        int port = plugin.getConfig().getInt(ConfigProperties.STORAGE_PORT.toString());

        this.setup(username, password, port);
    }

    @Override
    public void removeMessage(SWMessage message, boolean withReplies) {
        String query = "DELETE FROM `" + table + "` WHERE `id`=?" + (withReplies ? " OR `reply_to_id`=?" : "");
        try (Connection connection = getConnection();
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
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
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
        // todo implement create message.
        return null;
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
}
