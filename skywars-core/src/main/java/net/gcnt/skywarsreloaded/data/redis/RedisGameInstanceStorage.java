package net.gcnt.skywarsreloaded.data.redis;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.data.config.YAMLConfig;
import net.gcnt.skywarsreloaded.data.games.GameInstanceStorage;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.gameinstance.CoreRemoteGameInstance;
import net.gcnt.skywarsreloaded.game.gameinstance.GameInstance;
import net.gcnt.skywarsreloaded.game.gameinstance.LocalGameInstance;
import net.gcnt.skywarsreloaded.game.gameinstance.RemoteGameInstance;
import net.gcnt.skywarsreloaded.game.types.GameState;
import net.gcnt.skywarsreloaded.manager.gameinstance.GameInstanceManager;
import net.gcnt.skywarsreloaded.manager.gameinstance.RemoteGameInstanceManager;
import net.gcnt.skywarsreloaded.utils.properties.ConfigProperties;
import net.gcnt.skywarsreloaded.wrapper.scheduler.CoreSWRunnable;
import net.gcnt.skywarsreloaded.wrapper.scheduler.SWRunnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description of redis structure:
 * All skywars instance data is sent to lobbies using redis pub/sub
 */
public class RedisGameInstanceStorage implements GameInstanceStorage, SWRedisMessenger {

    // Static
    private static final int INSTANCE_TIMEOUT_TIME = 5;

    // Finals
    private final String REDIS_GAME_INSTANCE_CHANNEL;
    private final String REDIS_GAME_INSTANCE_UPDATE_CHANNEL;
    private final SkyWarsReloaded plugin;
    private final CoreSWRedisConnection redisConnection;

    // Runtime data
    private final ConcurrentHashMap<UUID, Long> lastUpdateTimes;
    private SWRunnable swRunnable;

    public RedisGameInstanceStorage(SkyWarsReloaded plugin, CoreSWRedisConnection redisConnection) {
        this.plugin = plugin;
        this.redisConnection = redisConnection;
        this.lastUpdateTimes = new ConcurrentHashMap<>();

        this.REDIS_GAME_INSTANCE_CHANNEL = redisConnection.REDIS_BASE_CHANNEL + ".gameinstance";
        this.REDIS_GAME_INSTANCE_UPDATE_CHANNEL = this.REDIS_GAME_INSTANCE_CHANNEL + ".update";
    }

    @Override
    public void setup(YAMLConfig config) {
        subscribeToRedisUpdates();
    }

    @Override
    public List<RemoteGameInstance> fetchGameInstances() {
        Collection<? extends GameInstance> gameInstancesList = this.plugin.getGameInstanceManager().getGameInstancesList();
        ArrayList<RemoteGameInstance> instancesToReturn = new ArrayList<>(gameInstancesList.size());
        for (GameInstance inst : gameInstancesList) {
            // If this cast fails, the plugin is messed up anyway, so it may as well fail
            instancesToReturn.add((RemoteGameInstance) inst);
        }
        return instancesToReturn;
    }

    @Override
    public RemoteGameInstance getGameInstanceById(UUID uuid) {
        return (RemoteGameInstance) this.plugin.getGameInstanceManager().getGameInstanceById(uuid);
    }

    @Override
    public List<RemoteGameInstance> getGameInstancesByTemplate(GameTemplate template) {
        // noinspection unchecked
        return (List<RemoteGameInstance>) this.plugin.getGameInstanceManager().getGameInstancesByTemplate(template);
    }

    @Override
    public void updateGameInstance(LocalGameInstance gameInstance) {
        this.redisConnection.getConnection().publish(REDIS_GAME_INSTANCE_CHANNEL,
                GameInstanceUpdateSerializer.serialize(
                        gameInstance.getId(),
                        gameInstance.getTemplate().getName(),
                        this.plugin.getConfig().getString(ConfigProperties.SERVER_NAME.toString()),
                        gameInstance.getPlayerCount(),
                        gameInstance.getState()
                ));
    }

    @Override
    public void removeOldInstances() {
        RemoteGameInstanceManager remoteInstManager = (RemoteGameInstanceManager) this.plugin.getGameInstanceManager();
        this.lastUpdateTimes.forEach((uuid, timestamp) -> {
            if (timestamp + INSTANCE_TIMEOUT_TIME < System.currentTimeMillis()) {
                remoteInstManager.removeCachedGameInstance(uuid);
                this.lastUpdateTimes.remove(uuid);
            }
        });
    }

    @Override
    public void removeGameInstance(GameInstance gameInstance) {
        this.removeGameInstance(gameInstance.getId());
    }

    @Override
    public void removeGameInstance(UUID uuid) {
        this.redisConnection.getConnection().publish(REDIS_GAME_INSTANCE_CHANNEL, uuid + ":remove");
    }

    @Override
    public void startAutoUpdating() {
        swRunnable = this.plugin.getScheduler().runAsyncTimer(new CoreSWRunnable() {
            @Override
            public void run() {
                GameInstanceManager<? extends GameInstance> instanceManager = plugin.getGameInstanceManager();
                if (instanceManager.isManagerRemote()) {
                    removeOldInstances();
                } else if (true /* todo: check if in network/proxy mode but we don't have a remote manager (aka we are a game server)*/) {
                    instanceManager.getGameInstancesList().forEach(instance -> {
                        updateGameInstance((LocalGameInstance) instance);
                    });
                }
            }
        }, 2 * 20, 2 * 20);
    }

    @Override
    public void stopAutoUpdating() {
        if (swRunnable != null) swRunnable.cancel();
    }

    public void subscribeToRedisUpdates() {
        this.redisConnection.registerMessenger(this);
        this.redisConnection.registerPubSubListener(REDIS_GAME_INSTANCE_CHANNEL, this::onPubSubMessage);
    }

    public void onPubSubMessage(String channel, String message) {
        if (channel.equals(REDIS_GAME_INSTANCE_CHANNEL)) {
            String[] parts = message.split(".");

            // Allow removing of instance instantly
            if (parts.length < 2) {
                plugin.getLogger().warn("Received invalid redis message!\nData: " + message);
                return;
            }
            if (parts[1].equalsIgnoreCase("remove")) {
                UUID uuid;
                try {
                    uuid = UUID.fromString(parts[0]);
                } catch (Exception ex) {
                    plugin.getLogger().warn("Received invalid redis message!\nData: " + message);
                    return;
                }
                ((RemoteGameInstanceManager) plugin.getGameInstanceManager()).removeCachedGameInstance(uuid);
                lastUpdateTimes.remove(uuid);
                return;
            }

            // Update or add new instance to cache
            if (parts.length < 5) {
                plugin.getLogger().warn("Received invalid redis message!\nData: " + message);
                return;
            }
            RemoteGameInstance deserializedInstance;
            try {
                deserializedInstance = GameInstanceUpdateSerializer.deserialize(plugin, message);
            } catch (InvalidGameInstanceUpdateFormat ex) {
                ex.printStackTrace();
                return;
            }

            // todo: handle deserializedInstance
        }
    }

    static class GameInstanceUpdateSerializer {

        // FORMAT
        // instanceId:templateId:serverId:playerCount:state

        static String serialize(UUID uuid, String templateId, String serverId, int playerCount, GameState state) {
            return String.format("%s:%s:%s:%d:%s", uuid.toString(), templateId, serverId, playerCount, state.name());
        }

        static RemoteGameInstance deserialize(SkyWarsReloaded plugin, String rawData) throws InvalidGameInstanceUpdateFormat {
            String[] parts = rawData.split(":");

            UUID uuid;
            GameTemplate template;
            String serverId;
            int playerCount;
            GameState state;

            try {
                uuid = UUID.fromString(parts[0]);
                template = plugin.getGameInstanceManager().getGameTemplateByName(parts[1]);
                if (template == null) throw new IllegalArgumentException(String.format("Template %s doesn't exist!", parts[1]));
                serverId = parts[2];
                playerCount = Integer.parseInt(parts[3]);
                state = GameState.valueOf(parts[4]);
            } catch (Exception ex) {
                throw new InvalidGameInstanceUpdateFormat(ex);
            }

            CoreRemoteGameInstance inst = new CoreRemoteGameInstance(plugin, uuid, template, serverId, state);
            inst.setPlayerCount(playerCount);
            return inst;
        }

    }

    static class InvalidGameInstanceUpdateFormat extends Exception {

        public InvalidGameInstanceUpdateFormat() {
        }

        public InvalidGameInstanceUpdateFormat(String message) {
            super(message);
        }

        public InvalidGameInstanceUpdateFormat(String message, Throwable cause) {
            super(message, cause);
        }

        public InvalidGameInstanceUpdateFormat(Throwable cause) {
            super(cause);
        }
    }
}
