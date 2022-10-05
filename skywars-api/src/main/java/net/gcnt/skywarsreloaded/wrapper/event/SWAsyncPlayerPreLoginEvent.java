package net.gcnt.skywarsreloaded.wrapper.event;

import net.gcnt.skywarsreloaded.event.SWEvent;

import java.net.InetAddress;
import java.util.UUID;

public interface SWAsyncPlayerPreLoginEvent extends SWEvent {

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
