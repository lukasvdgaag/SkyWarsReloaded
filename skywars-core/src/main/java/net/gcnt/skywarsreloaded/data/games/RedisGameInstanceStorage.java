package net.gcnt.skywarsreloaded.data.games;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.gameinstance.GameInstance;
import net.gcnt.skywarsreloaded.game.gameinstance.RemoteGameInstance;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;
import java.util.List;

public class RedisGameInstanceStorage implements GameInstanceStorage {

    private final SkyWarsReloaded plugin;
    private JedisPool jedisPool;

    public RedisGameInstanceStorage(SkyWarsReloaded plugin) {
        this.plugin = plugin;
    }

    private JedisPoolConfig buildPoolConfig() {
        final JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(5);
        poolConfig.setMaxIdle(3);
        poolConfig.setMinIdle(2);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setMinEvictableIdleTime(Duration.ofSeconds(60));
        poolConfig.setTimeBetweenEvictionRuns(Duration.ofSeconds(30));
        poolConfig.setNumTestsPerEvictionRun(3);
        poolConfig.setBlockWhenExhausted(true);
        return poolConfig;
    }

    public Jedis getConnection() {
        return jedisPool.getResource();
    }

    @Override
    public void setup() {
        JedisPoolConfig poolConfig = buildPoolConfig();
        // todo change host, user, password and port (config).

        String password = this.plugin.getConfig().getString("password", "n/a");
        if (password.equals("n/a")) this.plugin.getLogger().warn("The redis password is set to n/a, the connection will probably fail.");

        this.jedisPool = new JedisPool(poolConfig, "host", 37047, 10 * 1000, "user", password);
    }

    @Override
    public List<RemoteGameInstance> getGameInstances() {
        return null;
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
}
