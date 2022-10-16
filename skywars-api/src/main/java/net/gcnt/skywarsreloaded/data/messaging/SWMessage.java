package net.gcnt.skywarsreloaded.data.messaging;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

public interface SWMessage {

    int getId();

    void setId(int id);

    /**
     * Gets the time at which the message was sent
     *
     * @return Millis timestamp of time
     */
    long getTimestamp();

    @NotNull
    String getChannel();

    void setChannel(@NotNull String channel);

    JsonObject getPayload();

    void setPayload(JsonObject payload);

    String getOriginServer();

    String getTargetServer();

    void setTargetServer(String targetServerName);

    int getReplyToId();

    void setReplyToId(int replyToId);

    void send();

}
