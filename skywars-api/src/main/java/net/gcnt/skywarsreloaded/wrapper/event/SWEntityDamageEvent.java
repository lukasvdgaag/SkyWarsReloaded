package net.gcnt.skywarsreloaded.wrapper.event;

import net.gcnt.skywarsreloaded.enums.DeathReason;
import net.gcnt.skywarsreloaded.event.SWEvent;
import net.gcnt.skywarsreloaded.wrapper.entity.SWEntity;

public interface SWEntityDamageEvent extends SWEvent, SWCancellable {

    SWEntity getEntity();

    void setDamage(double damage);

    double getDamage();

    double getFinalDamage();

    DeathReason getCause();

}
