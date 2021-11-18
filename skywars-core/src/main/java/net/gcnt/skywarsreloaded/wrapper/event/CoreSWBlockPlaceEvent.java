package net.gcnt.skywarsreloaded.wrapper.event;

import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;

public class CoreSWBlockPlaceEvent extends CoreSWPlayerEvent implements SWBlockPlaceEvent {

    private final String worldName;
    private final SWCoord coord;
    private final String blockType;

    public CoreSWBlockPlaceEvent(SWPlayer player, String worldNameIn, SWCoord coordIn, String blockTypeIn) {
        super(player);
        this.worldName = worldNameIn;
        this.coord = coordIn;
        this.blockType = blockTypeIn;
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

}
