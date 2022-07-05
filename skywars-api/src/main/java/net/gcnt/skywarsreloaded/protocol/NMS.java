package net.gcnt.skywarsreloaded.protocol;

import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;
import net.gcnt.skywarsreloaded.wrapper.server.SWGameRule;
import net.gcnt.skywarsreloaded.wrapper.world.SWWorld;

public interface NMS {

    void setBlock(SWCoord loc, Item item);

    void setGameRule(SWWorld world, SWGameRule rule, Object value);

    void sendActionbar(SWPlayer player, String message);

    void sendJSONMessage(SWPlayer player, String message);

    void setChestOpen(SWCoord loc, boolean open);

}
