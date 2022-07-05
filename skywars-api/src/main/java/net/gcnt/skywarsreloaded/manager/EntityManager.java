package net.gcnt.skywarsreloaded.manager;

import net.gcnt.skywarsreloaded.wrapper.entity.SWEntity;

import java.util.UUID;

public interface EntityManager {

    SWEntity getEntityByUUID(UUID uuid);

}
