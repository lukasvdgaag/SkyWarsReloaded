package net.gcnt.skywarsreloaded.wrapper.event;

import net.gcnt.skywarsreloaded.enums.DeathReason;
import net.gcnt.skywarsreloaded.wrapper.entity.SWEntity;

public class CoreSWEntityDamageByEntityEvent extends CoreSWEntityDamageEvent implements SWEntityDamageByEntityEvent {

    private final SWEntity damager;

    public CoreSWEntityDamageByEntityEvent(SWEntity entity, SWEntity damager, double damage, double finalDamage, DeathReason cause) {
        super(entity, damage, finalDamage, cause);
        this.damager = damager;
    }

    @Override
    public SWEntity getDamager() {
        return damager;
    }

}
