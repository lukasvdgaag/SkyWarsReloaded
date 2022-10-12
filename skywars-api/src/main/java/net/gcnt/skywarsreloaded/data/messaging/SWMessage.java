package net.gcnt.skywarsreloaded.data.messaging;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    String getPayload();

    void setPayload(String payload);

    String getOriginServer();

    String getTargetServer();

    void setTargetServer(String targetServerName);

    @Nullable
    int getReplyToId();

    void setReplyToId(@Nullable int replyToId);

    void send();

}
