package com.walrusone.skywarsreloaded.managers.worlds;

import com.grinderwolf.swm.api.exceptions.WorldTooBigException;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.nms.world.SlimeLoadedWorld;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class ASWMWorldManager extends CommonSWMWorldManager {

    public ASWMWorldManager() {
        super();
    }

    @Override
    protected byte[] serializeSlimeWorld(SlimeWorld slimeWorld) throws WorldTooBigException, IllegalStateException {
        try {
            return ((SlimeLoadedWorld) slimeWorld).serialize().get();
        } catch (IndexOutOfBoundsException e) {
            throw new WorldTooBigException(slimeWorld.getName());
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new IllegalStateException("Couldn't serialize world " + slimeWorld.getName(), e);
        }
    }

    @Override
    public WorldManagerType getType() {
        return WorldManagerType.ASWM;
    }
}
