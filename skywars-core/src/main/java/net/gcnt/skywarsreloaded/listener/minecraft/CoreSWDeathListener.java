package net.gcnt.skywarsreloaded.listener.minecraft;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.gameinstance.GameInstance;
import net.gcnt.skywarsreloaded.listener.CoreSWEventListener;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;
import net.gcnt.skywarsreloaded.wrapper.event.CoreSWPlayerDeathEvent;

public class CoreSWDeathListener {

    private final SkyWarsReloaded plugin;

    public CoreSWDeathListener(SkyWarsReloaded plugin) {
        this.plugin = plugin;
        plugin.getEventManager().registerListener(new CoreSWEventListener<>(CoreSWPlayerDeathEvent.class, this::onDeath));
    }

    public void onDeath(CoreSWPlayerDeathEvent event) {
        SWPlayer player = event.getPlayer();
        final GameInstance gameWorld = player.getGameWorld();

        if (gameWorld == null) return;

        player.setHealth(20);
        event.setDeathMessage(null);
        event.setKeepInventory(false);
    }

}
