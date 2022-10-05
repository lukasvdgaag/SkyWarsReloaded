package net.gcnt.skywarsreloaded.listener.minecraft;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.GamePlayer;
import net.gcnt.skywarsreloaded.game.gameinstance.LocalGameInstance;
import net.gcnt.skywarsreloaded.listener.CoreSWEventListener;
import net.gcnt.skywarsreloaded.wrapper.entity.SWEntity;
import net.gcnt.skywarsreloaded.wrapper.entity.SWOwnedEntity;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;
import net.gcnt.skywarsreloaded.wrapper.event.CoreSWEntityDamageByEntityEvent;

public class CoreSWEntityDamageByEntityListener {

    private final SkyWarsReloaded plugin;

    public CoreSWEntityDamageByEntityListener(SkyWarsReloaded plugin) {
        this.plugin = plugin;
        plugin.getEventManager().registerListener(new CoreSWEventListener<>(CoreSWEntityDamageByEntityEvent.class, this::onDamage));
    }

    public void onDamage(CoreSWEntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof SWPlayer) || plugin.getGameInstanceManager().isManagerRemote()) return;

        SWPlayer player = (SWPlayer) event.getEntity();
        if (CoreSWEventListener.cancelWhenWaitingInGame(player, event)) return;

        SWEntity damager = event.getDamager();

        SWPlayer tagger = null;
        if (damager instanceof SWPlayer) {
            tagger = (SWPlayer) damager;
        } else if (damager instanceof SWOwnedEntity) {
            tagger = ((SWOwnedEntity) damager).getOwner();
        }

        if (tagger != null && !tagger.equals(player)) {
            final GamePlayer gamePlayer = ((LocalGameInstance) player.getGameWorld()).getPlayer(player);
            gamePlayer.setLastTaggedBy(tagger);
        }
    }

}
