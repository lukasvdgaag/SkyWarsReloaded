package net.gcnt.skywarsreloaded.wrapper.event;

import java.net.InetAddress;
import java.util.UUID;

public interface SWAsyncPlayerPreLoginEvent {

    String getName();

    InetAddress getIP();

    UUID getUUID();

    void allow();

    void disallow(Result result, String reason);

    void setResult(Result result);

    Result getResult();

    void setKickMessage(String message);

    String getKickMessage();

    enum Result {
        ALLOWED,
        KICK_FULL,
        KICK_BANNED,
        KICK_WHITELIST,
        KICK_OTHER;
    }

}
