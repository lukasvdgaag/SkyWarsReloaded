package net.gcnt.skywarsreloaded.listener.minecraft;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.enums.DeathReason;
import net.gcnt.skywarsreloaded.game.GamePlayer;
import net.gcnt.skywarsreloaded.game.GameTeam;
import net.gcnt.skywarsreloaded.game.gameinstance.LocalGameInstance;
import net.gcnt.skywarsreloaded.game.types.GameState;
import net.gcnt.skywarsreloaded.listener.CoreSWEventListener;
import net.gcnt.skywarsreloaded.unlockable.killmessages.KillMessageGroup;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;
import net.gcnt.skywarsreloaded.wrapper.event.CoreSWEntityDamageEvent;

public class CoreSWEntityDamageListener {

    private final SkyWarsReloaded plugin;

    public CoreSWEntityDamageListener(SkyWarsReloaded plugin) {
        this.plugin = plugin;
        plugin.getEventManager().registerListener(new CoreSWEventListener<>(CoreSWEntityDamageEvent.class, this::onDamage));
    }

    public void onDamage(CoreSWEntityDamageEvent event) {
        if (!(event.getEntity() instanceof SWPlayer) || plugin.getGameInstanceManager().isManagerRemote()) return;

        SWPlayer player = (SWPlayer) event.getEntity();
        if (CoreSWEventListener.cancelWhenWaitingInGame(player, event)) return;
        LocalGameInstance gameWorld = (LocalGameInstance) player.getGameWorld();
        if (gameWorld == null) return;

        GamePlayer gamePlayer = gameWorld.getPlayer(player);
        if (gamePlayer.isSpectating() || !gamePlayer.isAlive()) {
            event.setCancelled(true);
            return;
        }

        if (gameWorld.getState() != GameState.PLAYING) return;

        if (player.getHealth() - event.getFinalDamage() > 0) return;

        final GameTeam team = gamePlayer.getTeam();
        if (team == null) return;

        event.setDamage(0);

        DeathReason reason = event.getCause();
        if (reason == null) reason = DeathReason.DEFAULT;

        SWPlayer tagged = gamePlayer.getLastTaggedBy();

        // sending the death message.
        // todo check for assists.
        String message;
        if (tagged != null) {
            if (!reason.isKill()) {
                reason = DeathReason.fromString(reason.name() + "_KILL");
                if (reason == null) reason = DeathReason.DEFAULT_KILL;
            }

            final GamePlayer taggedGamePlayer = gameWorld.getPlayer(tagged);
            if (taggedGamePlayer != null) taggedGamePlayer.addKill();

            // selecting the kill messages of the killer.
            final KillMessageGroup killMessageGroup = plugin.getUnlockablesManager().getKillMessageGroup(tagged.getPlayerData().getKillMessagesTheme());
            message = killMessageGroup.getRandomMessage(reason, tagged);
            message = message.replace("%killer%", tagged.getName());
        } else {
            final KillMessageGroup killMessageGroup = plugin.getUnlockablesManager().getKillMessageGroup(player.getPlayerData().getKillMessagesTheme());
            message = killMessageGroup.getRandomMessage(reason, null);
        }
        message = message.replace("%player%", player.getName());

        gameWorld.announce(plugin.getUtils().colorize(message));
        player.sendTitle("§c§lYOU DIED!", "§7You are now a spectator!", 5, 30, 5);
        // todo customize this message.

        team.eliminatePlayer(gamePlayer);
        gameWorld.preparePlayer(player);
    }

}
