package net.gcnt.skywarsreloaded.wrapper.event;

import net.gcnt.skywarsreloaded.wrapper.world.SWWorld;

public class CoreSWChunkLoadEvent implements SWChunkLoadEvent {

    private final SWWorld world;
    private final int x;
    private final int z;
    private final boolean generatingChunk;

    public CoreSWChunkLoadEvent(SWWorld worldIn, int chunkXIn, int chunkZIn, boolean generatingChunkIn) {
        this.world = worldIn;
        this.x = chunkXIn;
        this.z = chunkZIn;
        this.generatingChunk = generatingChunkIn;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getZ() {
        return this.z;
    }

    @Override
    public boolean isGeneratingChunk() {
        return this.generatingChunk;
    }

    @Override
    public SWWorld getWorld() {
        return this.world;
    }
}
