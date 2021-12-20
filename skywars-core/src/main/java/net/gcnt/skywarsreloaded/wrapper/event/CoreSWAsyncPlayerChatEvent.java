package net.gcnt.skywarsreloaded.wrapper.event;

import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;

public class CoreSWAsyncPlayerChatEvent extends CoreSWPlayerEvent implements SWAsyncPlayerChatEvent {

    private String message;

    private boolean cancelled;

    public CoreSWAsyncPlayerChatEvent(SWPlayer player, String message) {
        super(player);
        this.message = message;

        this.cancelled = false;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelledIn) {
        this.cancelled = cancelledIn;
    }
}
