package net.gcnt.skywarsreloaded.data.messaging;

import org.jetbrains.annotations.Nullable;

public interface SWMessage {

    int getId();

    String getChannel();

    String setChannel(String channel);

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
