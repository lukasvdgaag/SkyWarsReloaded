package net.gcnt.skywarsreloaded.event;

import net.gcnt.skywarsreloaded.data.messaging.SWMessage;

public class CoreSWMessageSentEvent implements SWMessageSentEvent {

    private final SWMessage message;

    public CoreSWMessageSentEvent(SWMessage message) {
        this.message = message;
    }

    @Override
    public SWMessage getMessage() {
        return message;
    }
}
