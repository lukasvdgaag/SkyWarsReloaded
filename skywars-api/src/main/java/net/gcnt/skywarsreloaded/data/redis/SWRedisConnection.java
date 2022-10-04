package net.gcnt.skywarsreloaded.data.redis;

import java.util.function.BiConsumer;

public interface SWRedisConnection {

    void registerMessenger(SWRedisMessenger messenger);

    // BiConsumer<String channel, String message>
    void registerPubSubListener(String channel, BiConsumer<String, String> consumer);

}
