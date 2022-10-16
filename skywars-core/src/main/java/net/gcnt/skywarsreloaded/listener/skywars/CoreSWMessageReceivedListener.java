package net.gcnt.skywarsreloaded.listener.skywars;

import com.google.gson.JsonObject;
import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.data.messaging.SWMessage;
import net.gcnt.skywarsreloaded.data.messaging.SWMessageCreator;
import net.gcnt.skywarsreloaded.event.CoreSWMessageReceivedEvent;
import net.gcnt.skywarsreloaded.game.gameinstance.CoreRemoteGameInstance;
import net.gcnt.skywarsreloaded.game.types.GameState;
import net.gcnt.skywarsreloaded.listener.CoreSWEventListener;
import net.gcnt.skywarsreloaded.manager.gameinstance.RemoteGameInstanceManager;

import java.util.UUID;

public class CoreSWMessageReceivedListener {

    private final SkyWarsReloaded plugin;

    // TODO add a scheduler that sends an update ping to the proxy every 1/2 seconds.
    // TODO add a scheduler that sends game update pings every second.
    public CoreSWMessageReceivedListener(SkyWarsReloaded plugin) {
        this.plugin = plugin;
        plugin.getEventManager().registerListener(new CoreSWEventListener<>(CoreSWMessageReceivedEvent.class, this::onMessageReceived));
    }

    public void onMessageReceived(CoreSWMessageReceivedEvent event) {
        final String channel = event.getMessage().getChannel();

        if (channel.equals(SWMessageCreator.MessageChannel.SERVER_UPDATE_PING.toString())) {
            this.onServerUpdatePingReceived(event.getMessage());
        } else if (channel.equals(SWMessageCreator.MessageChannel.GAME_UPDATE_PING.toString())) {
            this.onGameUpdatePingReceived(event.getMessage());
        } else if (channel.equals(SWMessageCreator.MessageChannel.GAME_CREATION_REQUEST.toString())) {
            this.onGameCreationRequestReceived(event.getMessage());
        }
    }

    protected void onServerUpdatePingReceived(SWMessage message) {
        if (plugin.getGameInstanceManager() instanceof RemoteGameInstanceManager) {
            // updating the remote server when a server update ping is received
            final JsonObject payload = message.getPayload();
            ((RemoteGameInstanceManager) plugin.getGameInstanceManager()).updateRemoteServer(
                    message.getOriginServer(),
                    payload.get("game_count").getAsInt(),
                    payload.get("players").getAsInt()
            );
        }
    }

    protected void onGameUpdatePingReceived(SWMessage message) {
        if (plugin.getGameInstanceManager() instanceof RemoteGameInstanceManager) {
            // updating the remote game when a game update ping is received
            final JsonObject payload = message.getPayload();

            final CoreRemoteGameInstance instance = new CoreRemoteGameInstance(plugin,
                    UUID.fromString(payload.get("id").getAsString()),
                    plugin.getGameInstanceManager().getGameTemplateByName(payload.get("template").getAsString()),
                    message.getOriginServer(),
                    GameState.valueOf(payload.get("state").getAsString()));

            ((RemoteGameInstanceManager) plugin.getGameInstanceManager()).updateCachedGameInstance(instance);
        }
    }

    protected void onGameCreationRequestReceived(SWMessage message) {
        // todo check if this is the a game server
        // if game server, check if the maximum number of active games is not yet reached
        // then create the game and send a game creation response with the created game.
    }

}
