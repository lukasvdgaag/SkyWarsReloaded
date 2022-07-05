package net.gcnt.skywarsreloaded.wrapper.event;

import net.gcnt.skywarsreloaded.enums.DeathReason;
import net.gcnt.skywarsreloaded.wrapper.player.SWEntity;

public interface SWEntityDamageEvent extends SWCancellable {

    SWEntity getEntity();

    void setDamage(double damage);

    double getDamage();

    void setFinalDamage(double damage);

    double getFinalDamage();

    DeathReason getCause();

}
