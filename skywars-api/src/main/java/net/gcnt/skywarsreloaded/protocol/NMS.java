package net.gcnt.skywarsreloaded.protocol;

import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;
import net.gcnt.skywarsreloaded.wrapper.world.SWWorld;

public interface NMS {

    void setGameRule(SWWorld world, String rule, String value);

    void sendActionbar(SWPlayer player, String message);

    void sendJSONMessage(SWPlayer player, String message);

}
