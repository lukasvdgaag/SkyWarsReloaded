package net.gcnt.skywarsreloaded.wrapper.event;

public interface SWPlayerJoinEvent extends SWPlayerEvent {

    String getJoinMessage();

    void setJoinMessage(String joinMessage);

}
