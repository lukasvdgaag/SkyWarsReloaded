package net.gcnt.skywarsreloaded.data.redis;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.utils.properties.ConfigProperties;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiConsumer;

public class CoreSWRedisConnection implements SWRedisConnection {
    public static final String REDIS_BASE_CHANNEL = "swr";

    protected final SkyWarsReloaded plugin;

    // Runtime data
    private JedisPool jedisPool;
    private JedisPubSub redisPubSubConnection;
    private Thread pubSubThread;
    private List<SWRedisMessenger> messengers;
    private HashMap<String, ConcurrentLinkedQueue<BiConsumer<String, String>>> registeredListeners;

    public CoreSWRedisConnection(SkyWarsReloaded plugin) {
        this.plugin = plugin;
        this.messengers = new ArrayList<>();
        this.registeredListeners = new HashMap<>();
    }

    @Override
    public Jedis getConnection() {
        return jedisPool.getResource();
    }

    protected JedisPoolConfig buildPoolConfig(int maxConnections, int minIdle) {
        final JedisPoolConfig poolConfig = new JedisPoolConfig();
        // Max of total connections
        poolConfig.setMaxTotal(maxConnections);
        // Meet in the middle for max idle
        poolConfig.setMaxIdle((maxConnections - minIdle) / 2 + minIdle);
        // Min idle connections in the pool
        poolConfig.setMinIdle(minIdle);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setMinEvictableIdleTime(Duration.ofSeconds(60));
        poolConfig.setTimeBetweenEvictionRuns(Duration.ofSeconds(30));
        poolConfig.setNumTestsPerEvictionRun(3);
        poolConfig.setBlockWhenExhausted(true);
        return poolConfig;
    }

    public void setup() {
        String hostname = plugin.getConfig().getString(ConfigProperties.MESSAGING_REDIS_HOSTNAME.toString());
        String username = plugin.getConfig().getString(ConfigProperties.MESSAGING_REDIS_USERNAME.toString());
        String password = plugin.getConfig().getString(ConfigProperties.MESSAGING_REDIS_PASSWORD.toString());
        int port = plugin.getConfig().getInt(ConfigProperties.MESSAGING_REDIS_PORT.toString());

        JedisPoolConfig poolConfig = this.buildPoolConfig(5, 2);

        if (password == null || password.equals("n/a")) this.plugin.getLogger().warn("The redis password is set to n/a, the connection will probably fail.");

        this.setJedisPool(new JedisPool(poolConfig, hostname, port, 10 * 1000, username, password));
        this.subscribeToRedisUpdates();
    }

    private void subscribeToRedisUpdates() {
        redisPubSubConnection = new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                String partialChannel = "";
                for (String channelPart : channel.split("\\.")) {
                    partialChannel += "." + channelPart;
                    registeredListeners.get(partialChannel).forEach(listener -> {
                        listener.accept(channel, message);
                    });
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
                    jedis.subscribe(redisPubSubConnection, REDIS_BASE_CHANNEL + ".*");

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

    @Override
    public void registerMessenger(SWRedisMessenger messenger) {
        this.messengers.add(messenger);
    }

    @Override
    public void registerPubSubListener(String channel, BiConsumer<String, String> consumer) {
        ConcurrentLinkedQueue<BiConsumer<String, String>> listeners = this.registeredListeners.get(channel);
        if (listeners == null) {
            listeners = new ConcurrentLinkedQueue<>();
            this.registeredListeners.put(channel, listeners);
        }
        listeners.add(consumer);
    }

    private void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    private JedisPool getJedisPool() {
        return this.jedisPool;
    }
}
