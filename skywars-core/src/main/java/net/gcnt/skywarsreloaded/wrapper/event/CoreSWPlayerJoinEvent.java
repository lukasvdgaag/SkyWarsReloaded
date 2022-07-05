package net.gcnt.skywarsreloaded.wrapper.event;

import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;

public class CoreSWPlayerJoinEvent extends CoreSWPlayerEvent implements SWPlayerJoinEvent {

    private String joinMessage;

    public CoreSWPlayerJoinEvent(SWPlayer player, String joinMessage) {
        super(player);
        this.joinMessage = joinMessage;
    }

    @Override
    public String getJoinMessage() {
        return this.joinMessage;
    }

    @Override
    public void setJoinMessage(String joinMessageIn) {
        this.joinMessage = joinMessageIn;
    }
}
