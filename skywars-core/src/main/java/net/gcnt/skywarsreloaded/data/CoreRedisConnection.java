package net.gcnt.skywarsreloaded.data;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

public abstract class CoreRedisConnection {

    protected final SkyWarsReloaded plugin;
    private JedisPool jedisPool;

    public CoreRedisConnection(SkyWarsReloaded plugin) {
        this.plugin = plugin;
    }

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

    protected void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    protected JedisPool getJedisPool() {
        return this.jedisPool;
    }
}
