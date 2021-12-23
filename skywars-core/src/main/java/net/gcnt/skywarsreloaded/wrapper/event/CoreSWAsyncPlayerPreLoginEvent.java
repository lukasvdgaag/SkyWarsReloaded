package net.gcnt.skywarsreloaded.wrapper.event;

import java.net.InetAddress;
import java.util.UUID;

public class CoreSWAsyncPlayerPreLoginEvent implements SWAsyncPlayerPreLoginEvent {

    private final UUID uuid;
    private final String name;
    private final InetAddress address;
    private Result result;
    private String message;

    public CoreSWAsyncPlayerPreLoginEvent(UUID uuid, String name, InetAddress address, Result resultIn, String kickMessageIn) {
        this.uuid = uuid;
        this.name = name;
        this.address = address;
        this.result = resultIn == null ? Result.ALLOWED : resultIn;
        this.message = kickMessageIn == null ? "" : kickMessageIn;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public InetAddress getIP() {
        return this.address;
    }

    @Override
    public UUID getUUID() {
        return this.uuid;
    }

    @Override
    public void allow() {
        this.result = Result.ALLOWED;
        this.message = "";
    }

    @Override
    public void disallow(Result resultIn, String reason) {
        this.result = resultIn;
        this.message = reason;
    }

    @Override
    public Result getResult() {
        return this.result;
    }

    @Override
    public void setResult(Result resultIn) {
        this.result = resultIn;
    }

    @Override
    public String getKickMessage() {
        return this.message;
    }

    @Override
    public void setKickMessage(String messageIn) {
        this.message = messageIn;
    }
}
