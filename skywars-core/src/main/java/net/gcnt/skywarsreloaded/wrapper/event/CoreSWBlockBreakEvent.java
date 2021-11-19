package net.gcnt.skywarsreloaded.wrapper.event;

import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;

public class CoreSWBlockBreakEvent extends CoreSWPlayerEvent implements SWBlockBreakEvent {

    private final String worldName;
    private final SWCoord coord;
    private final String blockType;

    private boolean cancelled;

    public CoreSWBlockBreakEvent(SWPlayer player, String worldNameIn, SWCoord coordIn, String blockTypeIn) {
        super(player);
        this.worldName = worldNameIn;
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
    
    public String getWorldName() {
        return this.worldName;
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
