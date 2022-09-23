package net.gcnt.skywarsreloaded.data.messaging;

public interface SWMessaging {

    /**
     * Set up the messaging environment.
     */
    void setup();

    /**
     * @param payload The payload of the message.
     * @return {@link SWMessage} an SWMessage
     */
    SWMessage createMessage(String payload);

    /**
     * Send a message over the messaging channel.
     *
     * @param message The message to send.
     */
    void sendMessage(SWMessage message);

    /**
     * Reply to a received message.
     *
     * @param message The message to send.
     * @param replyTo The message to reply to.
     */
    void replyMessage(SWMessage message, SWMessage replyTo);

}
