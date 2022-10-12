package net.gcnt.skywarsreloaded.data.messaging;

import java.util.concurrent.CompletableFuture;

public interface SWMessaging {

    /**
     * Set up the messaging environment.
     */
    void setup();

    /**
     * @param payload The payload of the message.
     * @return {@link SWMessage} an SWMessage
     */
    SWMessage createMessage(String channel, String payload);

    /**
     * Send a message over the messaging channel.
     *
     * @param message The message to send.
     * @return
     */
    CompletableFuture<SWMessage> sendMessage(SWMessage message);

    /**
     * Reply to a received message.
     *
     * @param message The message to send.
     * @param replyTo The message to reply to.
     */
    void replyMessage(SWMessage message, SWMessage replyTo);

    /**
     * Start listening for incoming messages.
     */
    void startListening();

    /**
     * Stop listening for new messages.
     */
    void stopListening();

    /**
     * Start the cleaning of cached messages.
     */
    void startCleaning();

}
