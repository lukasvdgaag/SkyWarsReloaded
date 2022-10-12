package net.gcnt.skywarsreloaded.event;

import net.gcnt.skywarsreloaded.data.messaging.SWMessage;

public interface SWMessageReceivedEvent extends SWEvent  {

    SWMessage getMessage();

}
