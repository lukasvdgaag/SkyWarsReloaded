package net.gcnt.skywarsreloaded.wrapper.event;

import net.gcnt.skywarsreloaded.utils.Item;

public interface SWPlayerFoodLevelChangeEvent extends SWPlayerEvent, SWCancellable {

    int getFoodLevel();

    void setFoodLevel(int foodLevel);

    Item getItem();

}
