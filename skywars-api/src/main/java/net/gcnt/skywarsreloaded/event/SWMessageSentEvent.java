package net.gcnt.skywarsreloaded.event;

import net.gcnt.skywarsreloaded.data.messaging.SWMessage;
import net.gcnt.skywarsreloaded.wrapper.event.SWCancellable;

public interface SWMessageSentEvent extends SWCancellable {

    SWMessage getMessage();

}
