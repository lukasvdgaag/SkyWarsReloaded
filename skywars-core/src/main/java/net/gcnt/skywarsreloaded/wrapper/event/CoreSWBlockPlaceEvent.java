package net.gcnt.skywarsreloaded.wrapper.event;

import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;

public class CoreSWBlockPlaceEvent extends CoreSWPlayerEvent implements SWBlockPlaceEvent {

    private final SWCoord coord;
    private final String blockType;

    private boolean cancelled;

    public CoreSWBlockPlaceEvent(SWPlayer player, SWCoord coordIn, String blockTypeIn) {
        super(player);
        this.coord = coordIn;
        this.blockType = blockTypeIn;

        this.cancelled = false;
    }

    public SWCoord getCoord() {
        return this.coord;
    }

    public String getBlockTypeName() {
        return this.blockType;
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
