package net.gcnt.skywarsreloaded.wrapper.event;

import net.gcnt.skywarsreloaded.utils.SWCoord;

public interface SWPlayerInteractEvent extends SWPlayerEvent, SWCancellable {

    Action getAction();

    SWCoord getClickedBlockLocation();

    String getClickedBlockWorld();

    String getClickedBlockType();

    enum Action {
        LEFT_CLICK_BLOCK,
        RIGHT_CLICK_BLOCK,
        LEFT_CLICK_AIR,
        RIGHT_CLICK_AIR,
        PHYSICAL
    }

}
