package net.gcnt.skywarsreloaded.event;

import net.gcnt.skywarsreloaded.data.messaging.SWMessage;

public class CoreSWMessageReceivedEvent implements SWMessageReceivedEvent {

    private final SWMessage message;

    public CoreSWMessageReceivedEvent(SWMessage message) {
        this.message = message;
    }

    @Override
    public SWMessage getMessage() {
        return message;
    }
}
