package net.gcnt.skywarsreloaded.data.messaging;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface SWMessage {

    int getId();

    /**
     * Gets the time at which the message was sent
     *
     * @return Millis timestamp of time
     */
    long getTimestamp();

    void setTimestamp(long timestamp);

    @NotNull
    String getChannel();

    void setChannel(@NotNull String channel);

    String getPayload();

    void setPayload(String payload);

    String getOriginServer();

    String getTargetServer();

    void setTargetServer(String targetServerName);

    @Nullable
    Integer getReplyToId();

    void setReplyToId(@Nullable Integer replyToId);

    void send();

}
