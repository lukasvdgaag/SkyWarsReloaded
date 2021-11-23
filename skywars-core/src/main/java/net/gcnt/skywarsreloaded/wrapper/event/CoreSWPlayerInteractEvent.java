package net.gcnt.skywarsreloaded.wrapper.event;

import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;

public class CoreSWPlayerInteractEvent extends CoreSWPlayerEvent implements SWPlayerInteractEvent {

    private final SWCoord coord;
    private final String blockType;
    private final SWPlayerInteractEvent.Action action;

    private boolean cancelled;

    public CoreSWPlayerInteractEvent(SWPlayer player, SWCoord coordIn, String blockTypeIn, SWPlayerInteractEvent.Action actionIn) {
        super(player);
        this.coord = coordIn;
        this.blockType = blockTypeIn;
        this.action = actionIn;
        this.cancelled = false;
    }

    public SWCoord getClickedBlockLocation() {
        return coord;
    }

    public String getClickedBlockType() {
        return blockType;
    }

    @Override
    public Action getAction() {
        return action;
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
