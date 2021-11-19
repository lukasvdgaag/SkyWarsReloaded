package net.gcnt.skywarsreloaded.wrapper.event;

public interface SWPlayerQuitEvent extends SWPlayerEvent {

    String getQuitMessage();

    void setQuitMessage(String quitMessage);

}
