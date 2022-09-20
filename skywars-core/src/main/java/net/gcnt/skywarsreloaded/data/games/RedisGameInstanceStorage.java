package net.gcnt.skywarsreloaded.data.games;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.data.CoreRedisDB;
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
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description of redis structure:
 *  All skywars instance data is sent to lobbies using redis pub/sub
 *
 */
public class RedisGameInstanceStorage extends CoreRedisDB implements GameInstanceStorage {

    private static final String REDIS_GAME_INSTANCE_CHANNEL = "swr:gameinstance:update";
    private static final int INSTANCE_TIMEOUT_TIME = 5;

    // Runtime data
    private final ConcurrentHashMap<UUID, Long> lastUpdateTimes;
    private JedisPubSub redisPubSubConnection;
    private Thread pubSubThread;
    private SWRunnable swRunnable;

    public RedisGameInstanceStorage(SkyWarsReloaded plugin) {
        super(plugin);
        this.lastUpdateTimes = new ConcurrentHashMap<>();
    }

    @Override
    public void setup(String username, String password, int port) {
        JedisPoolConfig poolConfig = this.buildPoolConfig(5, 2);
        // todo change host, user, password and port (config).

        if (password == null || password.equals("n/a")) this.plugin.getLogger().warn("The redis password is set to n/a, the connection will probably fail.");

        this.setJedisPool(new JedisPool(poolConfig, "host", 37047, 10 * 1000, "user", password));
        this.subscribeToRedisUpdates();
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
    public RemoteGameInstance getGameInstanceById(String uuid) {
        return (RemoteGameInstance) this.plugin.getGameInstanceManager().getGameInstanceById(uuid);
    }

    @Override
    public List<RemoteGameInstance> getGameInstancesByTemplate(GameTemplate template) {
        // noinspection unchecked
        return (List<RemoteGameInstance>) this.plugin.getGameInstanceManager().getGameInstancesByTemplate(template);
    }

    @Override
    public void updateGameInstance(LocalGameInstance gameInstance) {
        this.getJedisPool().getResource().publish(REDIS_GAME_INSTANCE_CHANNEL,
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
        this.getJedisPool().getResource().publish(REDIS_GAME_INSTANCE_CHANNEL, uuid + ":remove");
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
        redisPubSubConnection = new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                if (channel.equals(REDIS_GAME_INSTANCE_CHANNEL)) {
                    String[] parts = message.split(":");

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
        };

        pubSubThread = new Thread(() -> {
            boolean brokenConnection = false;
            // As long as the thread hasn't been manually stopped or damaged, keep re-trying
            while (!Thread.interrupted() && !this.getJedisPool().isClosed()) {
                try (Jedis jedis = this.getJedisPool().getResource()) {
                    // Log info to console if connection failed
                    if (brokenConnection) {
                        this.plugin.getLogger().warn("Reconnecting after connection fail...");
                        brokenConnection = false;
                    }

                    // Will lock the thread on this call
                    jedis.subscribe(redisPubSubConnection, REDIS_GAME_INSTANCE_CHANNEL);

                    // This will fire only if subscribe() unlocks - aka fails
                    this.plugin.getLogger().warn("Connection closed!");
                } catch (Exception ex) {
                    // Set the broken flag to true in order for the reconnection warning to be logged
                    brokenConnection = true;
                    this.plugin.getLogger().warn("Connection to redis was interrupted! Attempting reconnection in 5 seconds...");

                    // Try unsubscribing anyway to clean up connections
                    try {
                        redisPubSubConnection.unsubscribe();
                    } catch (Exception ignored) {
                    }

                    // Wait 5 seconds before reconnection attempt
                    try {
                        // No we are not busy waiting here, this is just to prevent spamming reconnection
                        // attempts faster than once every 5 seconds
                        // noinspection BusyWait
                        Thread.sleep(5 * 1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
        });
        pubSubThread.start();
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
