package net.gcnt.skywarsreloaded.wrapper.event;

import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;

public class CoreSWPlayerQuitEvent extends CoreSWPlayerEvent implements SWPlayerQuitEvent {

    private String quitMessage;

    public CoreSWPlayerQuitEvent(SWPlayer player, String quitMessageIn) {
        super(player);
        this.quitMessage = quitMessageIn;
    }

    @Override
    public String getQuitMessage() {
        return this.quitMessage;
    }

    @Override
    public void setQuitMessage(String quitMessageIn) {
        this.quitMessage = quitMessageIn;
    }
}
