package net.gcnt.skywarsreloaded.bukkit.wrapper.player;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.wrapper.entity.SWOwnedEntity;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;
import org.bukkit.entity.Entity;

public class BukkitSWOwnedEntity extends BukkitSWEntity implements SWOwnedEntity {

    private final String type;
    private SWPlayer owner;

    public BukkitSWOwnedEntity(SkyWarsReloaded plugin, Entity entity, SWPlayer owner) {
        super(plugin, entity);
        this.type = entity.getType().name();
        this.owner = owner;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public SWPlayer getOwner() {
        return owner;
    }

    @Override
    public void setOwner(SWPlayer player) {
        this.owner = player;
    }
}
