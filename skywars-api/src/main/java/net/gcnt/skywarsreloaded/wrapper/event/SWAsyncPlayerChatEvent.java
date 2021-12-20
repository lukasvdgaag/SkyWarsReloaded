package net.gcnt.skywarsreloaded.wrapper.event;

public interface SWAsyncPlayerChatEvent extends SWPlayerEvent, SWCancellable {

    String getMessage();

    void setMessage(String message);

}
