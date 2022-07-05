package net.gcnt.skywarsreloaded.wrapper.event;

import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;

public class CoreSWPlayerMoveEvent extends CoreSWPlayerEvent implements SWPlayerMoveEvent {

    private boolean cancelled;
    private final SWCoord from;
    private final SWCoord to;

    public CoreSWPlayerMoveEvent(SWPlayer playerIn, SWCoord fromIn, SWCoord toIn) {
        super(playerIn);
        this.from = fromIn;
        this.to = toIn;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelledIn) {
        this.cancelled = cancelledIn;
    }

    @Override
    public SWCoord getFrom() {
        return this.from;
    }

    @Override
    public SWCoord getTo() {
        return this.to;
    }
}
