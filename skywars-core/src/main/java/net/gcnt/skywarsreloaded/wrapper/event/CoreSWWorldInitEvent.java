package net.gcnt.skywarsreloaded.wrapper.event;

import net.gcnt.skywarsreloaded.wrapper.world.SWWorld;

public class CoreSWWorldInitEvent implements SWWorldInitEvent {

    private final SWWorld world;

    public CoreSWWorldInitEvent(SWWorld worldIn) {
        this.world = worldIn;
    }

    @Override
    public SWWorld getWorld() {
        return this.world;
    }
}
