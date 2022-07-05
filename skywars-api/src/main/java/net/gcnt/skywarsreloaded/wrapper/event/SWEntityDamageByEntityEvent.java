package net.gcnt.skywarsreloaded.wrapper.event;

import net.gcnt.skywarsreloaded.wrapper.entity.SWEntity;

public interface SWEntityDamageByEntityEvent extends SWEntityDamageEvent, SWCancellable {

    SWEntity getEntity();

    SWEntity getDamager();

}
