package net.gcnt.skywarsreloaded.wrapper.event;

import java.net.InetAddress;
import java.util.UUID;

public class CoreSWAsyncPlayerPreLoginEvent implements SWAsyncPlayerPreLoginEvent {

    private UUID uuid;
    private String name;
    private InetAddress address;
    private Result result;
    private String message;

    public CoreSWAsyncPlayerPreLoginEvent(UUID uuid, String name, InetAddress address) {
        this.uuid = uuid;
        this.name = name;
        this.address = address;
        this.result = Result.ALLOWED;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public InetAddress getIP() {
        return null;
    }

    @Override
    public UUID getUUID() {
        return null;
    }

    @Override
    public void allow() {

    }

    @Override
    public void disallow(Result result, String reason) {

    }

    @Override
    public void setResult(Result result) {

    }

    @Override
    public Result getResult() {
        return null;
    }

    @Override
    public void setKickMessage(String message) {

    }

    @Override
    public String getKickMessage() {
        return null;
    }
}
