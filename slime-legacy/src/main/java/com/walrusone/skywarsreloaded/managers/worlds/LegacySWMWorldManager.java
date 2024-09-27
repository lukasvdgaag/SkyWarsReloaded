package com.walrusone.skywarsreloaded.managers.worlds;

import com.grinderwolf.swm.api.world.SlimeWorld;
import com.walrusone.skywarsreloaded.SkyWarsReloaded;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class LegacySWMWorldManager extends CommonSWMWorldManager {

    private final Method serializeMethod;

    public LegacySWMWorldManager() {
        super();

        Method serializeMethodTmp;

        // Reflection because this is legacy support
        try {
            Class<?> craftSlimeWorldClass = Class.forName("com.grinderwolf.swm.nms.CraftSlimeWorld");
            serializeMethodTmp = craftSlimeWorldClass.getDeclaredMethod("serialize");
        }
        catch (ClassNotFoundException | NoSuchMethodException e) {
            SkyWarsReloaded.get().getLogger().severe("Could not find serialize method in CraftSlimeWorld class");
            e.printStackTrace();
            serializeMethodTmp = null;
        }

        serializeMethod = serializeMethodTmp;
    }

    @Override
    protected byte[] serializeSlimeWorld(SlimeWorld slimeWorld) throws IllegalStateException {
        try {
            if (serializeMethod == null) {
                throw new IllegalStateException("Couldn't find serialize method in CraftSlimeWorld class at startup! World won't save!");
            }
            return (byte[]) serializeMethod.invoke(slimeWorld);
        }
        catch (IndexOutOfBoundsException e) {
            throw new IllegalStateException(slimeWorld.getName() + " is too big!", e);
        }
        catch (InvocationTargetException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public WorldManagerType getType() {
        return WorldManagerType.LEGACY_SWM;
    }

}
