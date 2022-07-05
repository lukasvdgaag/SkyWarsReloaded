package net.gcnt.skywarsreloaded.wrapper.entity;

import java.util.UUID;

public abstract class AbstractSWEntity implements SWEntity {

    private final UUID uuid;

    public AbstractSWEntity(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }
}
