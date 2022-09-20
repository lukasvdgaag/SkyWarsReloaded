package net.gcnt.skywarsreloaded.data.games;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.data.CoreRedisDB;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.gameinstance.GameInstance;
import net.gcnt.skywarsreloaded.game.gameinstance.RemoteGameInstance;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

import java.util.ArrayList;
import java.util.List;

/**
 * Description of redis structure:
 *  All skywars instance data is sent to lobbies using redis pub/sub
 *
 */
public class RedisGameInstanceStorage extends CoreRedisDB implements GameInstanceStorage {

    private static final String REDIS_GAME_INSTANCE_CHANNEL = "swr:gameinstance:update";
    private JedisPubSub redisPubSubConnection;
    private Thread pubSubThread;

    public RedisGameInstanceStorage(SkyWarsReloaded plugin) {
        super(plugin);
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
    public List<RemoteGameInstance> getGameInstances() {
        //noinspection unchecked
        return new ArrayList<RemoteGameInstance>(this.plugin.getGameInstanceManager().getGameInstancesList());
    }

    @Override
    public GameInstance getGameInstanceById(String uuid) {
        return null;
    }

    @Override
    public List<GameInstance> getGameInstancesByTemplate(GameTemplate template) {
        return null;
    }

    @Override
    public void addGameInstance(GameInstance gameInstance) {

    }

    @Override
    public void removeOldInstances() {

    }

    @Override
    public void removeGameInstance(GameInstance gameInstance) {

    }

    @Override
    public void removeGameInstance(String uuid) {

    }

    @Override
    public void updateGameInstance(GameInstance gameInstance) {

    }

    @Override
    public void startAutoUpdating() {

    }

    @Override
    public void stopAutoUpdating() {

    }

    public void subscribeToRedisUpdates() {
        redisPubSubConnection = new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                if (channel.equals(REDIS_GAME_INSTANCE_CHANNEL)) {
                    String[] parts = message.split(":");

                    // FORMAT
                    // instanceId:templateId:serverId:playerCount:state

                    if (parts.length < 5) return;
                    // todo: parse data
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
}
