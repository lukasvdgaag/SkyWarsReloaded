package net.gcnt.skywarsreloaded.event;

import net.gcnt.skywarsreloaded.data.messaging.SWMessage;

public interface SWMessageSentEvent extends SWEvent {

    SWMessage getMessage();

}
