package net.gcnt.skywarsreloaded.wrapper.event;

import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;

public class CoreSWPlayerEvent implements SWPlayerEvent {

    private SWPlayer player;

    public CoreSWPlayerEvent(SWPlayer playerIn) {
        this.player = playerIn;
    }

    @Override
    public SWPlayer getPlayer() {
        return this.player;
    }

}
