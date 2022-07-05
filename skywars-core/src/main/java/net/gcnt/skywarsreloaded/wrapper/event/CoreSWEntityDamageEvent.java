package net.gcnt.skywarsreloaded.wrapper.event;

import net.gcnt.skywarsreloaded.enums.DeathReason;
import net.gcnt.skywarsreloaded.wrapper.entity.SWEntity;

public class CoreSWEntityDamageEvent implements SWEntityDamageEvent, SWCancellable {

    private final SWEntity entity;
    private final DeathReason cause;
    private double damage;
    private double finalDamage;

    private boolean cancelled;

    public CoreSWEntityDamageEvent(SWEntity entity, double damage, double finalDamage, DeathReason cause) {
        this.entity = entity;
        this.damage = damage;
        this.finalDamage = finalDamage;
        this.cause = cause;
        this.cancelled = false;
    }

    @Override
    public DeathReason getCause() {
        return cause;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public SWEntity getEntity() {
        return entity;
    }

    @Override
    public double getDamage() {
        return damage;
    }

    @Override
    public double getFinalDamage() {
        return finalDamage;
    }

    @Override
    public void setDamage(double damage) {
        this.damage = damage;
    }

}
