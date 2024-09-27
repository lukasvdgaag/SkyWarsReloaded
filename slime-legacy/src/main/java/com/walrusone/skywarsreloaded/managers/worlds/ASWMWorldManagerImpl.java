package com.walrusone.skywarsreloaded.managers.worlds;

import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.nms.world.SlimeLoadedWorld;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class ASWMWorldManagerImpl extends CommonSWMWorldManager {

    public ASWMWorldManagerImpl() {
        super();
    }

    @Override
    protected byte[] serializeSlimeWorld(SlimeWorld slimeWorld) throws IllegalStateException {
        try {
            return ((SlimeLoadedWorld) slimeWorld).serialize().get();
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalStateException(slimeWorld.getName() + " is too big!", e);
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new IllegalStateException("Couldn't serialize world " + slimeWorld.getName(), e);
        }
    }

    @Override
    public WorldManagerType getType() {
        return WorldManagerType.ASWM;
    }
}
