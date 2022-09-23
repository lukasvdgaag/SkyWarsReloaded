package net.gcnt.skywarsreloaded.data.messaging;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CoreSWMessage implements SWMessage {

    private final SkyWarsReloaded plugin;
    private final String origin;
    private String channel;
    private String payload;
    private String target;
    private Integer replyToId;
    private Long timestamp;
    private Integer id;

    public CoreSWMessage(SkyWarsReloaded plugin, int id, long timestamp, String channel, String payload, String originServer, String target, int replyToId) {
        this.plugin = plugin;
        this.id = id;
        this.timestamp = timestamp;
        this.channel = channel;
        this.payload = payload;
        this.origin = originServer;
        this.target = target;
        this.replyToId = replyToId;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public long getTimestamp() {
        return this.timestamp;
    }

    @Override
    public @NotNull String getChannel() {
        return this.channel;
    }

    @Override
    public void setChannel(@NotNull String channel) {
        this.channel = channel;
    }

    @Override
    public String getPayload() {
        return this.payload;
    }

    @Override
    public void setPayload(String payload) {
        this.payload = payload;
    }

    @Override
    public String getOriginServer() {
        return this.origin;
    }

    @Override
    public String getTargetServer() {
        return this.target;
    }

    @Override
    public void setTargetServer(String targetServerName) {
        this.target = targetServerName;
    }

    @Override
    public @Nullable Integer getReplyToId() {
        return this.replyToId;
    }

    @Override
    public void setReplyToId(@Nullable Integer replyToId) {
        this.replyToId = replyToId;
    }

    @Override
    public void send() {
        this.timestamp = System.currentTimeMillis();
        // todo: plugin.getMessaging().
    }
}
