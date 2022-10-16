package net.gcnt.skywarsreloaded.manager.gameinstance;

import com.google.gson.JsonObject;
import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.gameinstance.CoreRemoteGameInstance;
import net.gcnt.skywarsreloaded.game.gameinstance.RemoteGameInstance;
import net.gcnt.skywarsreloaded.game.types.GameState;
import net.gcnt.skywarsreloaded.remote.CoreSWRemoteServerInfo;
import net.gcnt.skywarsreloaded.remote.SWRemoteServerInfo;
import net.gcnt.skywarsreloaded.utils.properties.ConfigProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class CoreRemoteGameInstanceManager extends CoreGameInstanceManager<RemoteGameInstance> implements RemoteGameInstanceManager {

    private final HashMap<String, SWRemoteServerInfo> remoteServers;

    public CoreRemoteGameInstanceManager(SkyWarsReloaded plugin) {
        super(plugin);
        this.remoteServers = new HashMap<>();
    }

    @Override
    public void loadRemoteServers() {
        Set<String> servers = plugin.getConfig().getKeys(ConfigProperties.PROXY_SERVERS.toString());
        for (String server : servers) {
            loadRemoteServer(server);
        }
    }

    protected SWRemoteServerInfo loadRemoteServer(String serverId) {
        int maxGames = plugin.getConfig().getInt(ConfigProperties.PROXY_SERVERS + "." + serverId + ConfigProperties.PROXY_SERVERS_MAX_GAMES);
        final CoreSWRemoteServerInfo info = new CoreSWRemoteServerInfo(serverId, maxGames);
        remoteServers.put(serverId, info);
        return info;
    }

    @Override
    public void updateRemoteServer(String id, int activeGames, int playerCount) {
        SWRemoteServerInfo info = remoteServers.get(id);
        if (!remoteServers.containsKey(id)) {
            info = loadRemoteServer(id);
        }

        info.update(activeGames, playerCount);
    }

    @Override
    public CompletableFuture<Boolean> saveInstanceToTemplate(RemoteGameInstance instance) {
        final CompletableFuture<Boolean> booleanCompletableFuture = new CompletableFuture<>();
        booleanCompletableFuture.complete(true);
        return booleanCompletableFuture; // todo: request save to messaging handler (redis or sql)
    }

    @Override
    public CompletableFuture<RemoteGameInstance> createGameWorld(GameTemplate data) {
        final List<SWRemoteServerInfo> freeServers = getFreeServers();
        if (freeServers.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }

        return createGameWorld(data, freeServers.get(0).getId());
    }

    public CompletableFuture<RemoteGameInstance> createGameWorld(GameTemplate data, String targetServer) {
        final CompletableFuture<RemoteGameInstance> future = new CompletableFuture<>();

        plugin.getMessaging().sendMessage(plugin.getMessageCreator().createGameCreationRequest(data, targetServer))
                .thenAccept(message -> {
                            if (message == null) {
                                // no server has responded...
                                future.complete(null);
                                return;
                            }

                            JsonObject payload = message.getPayload();
                            JsonObject createdGameData = payload.getAsJsonObject("created_game");
                            if (createdGameData == null) {
                                createGameWorld(data).thenAccept(future::complete);
                                return;
                            }

                            RemoteGameInstance instance = new CoreRemoteGameInstance(plugin,
                                    UUID.fromString(createdGameData.get("id").getAsString()),
                                    plugin.getGameInstanceManager().getGameTemplateByName(createdGameData.get("template").getAsString()),
                                    message.getOriginServer(),
                                    GameState.valueOf(createdGameData.get("state").getAsString())
                            );
                            this.addCachedGameInstance(instance);
                            future.complete(instance);
                        }
                );
        return future;
    }

    public List<SWRemoteServerInfo> getFreeServers() {
        return this.remoteServers.values().stream()
                .filter(info -> info.isConnectionAlive() && info.getFreeGames() > 0)
                .collect(Collectors.toList());
    }

    @Override
    public void addCachedGameInstance(RemoteGameInstance instance) {
        this.getGameInstances().put(instance.getId(), instance);
    }

    @Override
    public void removeCachedGameInstance(UUID instanceId) {
        this.getGameInstances().remove(instanceId);
    }

    @Override
    public void removeCachedGameInstance(RemoteGameInstance instance) {
        this.removeCachedGameInstance(instance.getId());
    }

    @Override
    public boolean isGameInstanceCached(UUID instanceId) {
        return this.getGameInstances().containsKey(instanceId);
    }

    @Override
    public RemoteGameInstance getCachedGameInstance(UUID instanceId) {
        return this.getGameInstances().get(instanceId);
    }

    @Override
    public void updateCachedGameInstance(RemoteGameInstance instance) {
        this.getGameInstances().put(instance.getId(), instance);
    }

    @Override
    public boolean isManagerRemote() {
        return true;
    }
}
