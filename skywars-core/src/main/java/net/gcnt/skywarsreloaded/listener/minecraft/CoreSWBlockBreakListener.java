package net.gcnt.skywarsreloaded.listener.minecraft;

import net.gcnt.skywarsreloaded.listener.CoreSWEventListener;
import net.gcnt.skywarsreloaded.listener.SWListenerPriority;
import net.gcnt.skywarsreloaded.wrapper.event.CoreSWBlockBreakEvent;

public class CoreSWBlockBreakListener extends CoreSWEventListener<CoreSWBlockBreakEvent> {

    public CoreSWBlockBreakListener() {
        super(CoreSWBlockBreakEvent.class, SWListenerPriority.NORMAL, null);
    }

    @Override
    public void onEvent(CoreSWBlockBreakEvent event) {

    }

}
