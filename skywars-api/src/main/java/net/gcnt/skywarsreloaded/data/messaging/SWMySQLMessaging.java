package net.gcnt.skywarsreloaded.data.messaging;

public interface SWMySQLMessaging extends SWMessaging {

    /**
     * Delete a message from the database.
     *
     * @param message     The message to delete.
     * @param withReplies Whether to delete any replies to the message.
     */
    void removeMessage(SWMessage message, boolean withReplies);

}
