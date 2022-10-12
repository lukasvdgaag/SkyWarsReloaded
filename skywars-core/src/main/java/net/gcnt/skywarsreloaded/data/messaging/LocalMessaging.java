package net.gcnt.skywarsreloaded.data.messaging;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.event.CoreSWMessageReceivedEvent;
import net.gcnt.skywarsreloaded.event.CoreSWMessageSentEvent;
import net.gcnt.skywarsreloaded.wrapper.scheduler.CoreSWRunnable;
import net.gcnt.skywarsreloaded.wrapper.scheduler.SWRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class LocalMessaging implements SWMessaging {

    private final long CLEANUP_TIMEOUT = 1000; // 1s
    private final SkyWarsReloaded plugin;
    private final ConcurrentHashMap<SWMessage, CompletableFuture<SWMessage>> cachedOutgoing;
    private SWRunnable cleanupTask;

    public LocalMessaging(SkyWarsReloaded plugin) {
        this.plugin = plugin;
        this.cachedOutgoing = new ConcurrentHashMap<>();
        this.cleanupTask = null;
    }

    @Override
    public void setup() {
        startCleaning();
    }

    @Override
    public SWMessage createMessage(String channel, String payload) {
        return new CoreSWMessage(plugin, channel, payload);
    }

    @Override
    public CompletableFuture<SWMessage> sendMessage(SWMessage message) {
        CompletableFuture<SWMessage> callback = new CompletableFuture<>();
        this.cachedOutgoing.put(message, callback);

        if (message.getReplyToId() != -1) {
            cachedOutgoing.entrySet().stream()
                    .filter(entry -> entry.getKey().getId() == message.getReplyToId())
                    .map(Map.Entry::getValue)
                    .findAny()
                    .ifPresent(foundFuture -> foundFuture.complete(message));
        }

        // Still trigger the sent event for consistency
        CoreSWMessageSentEvent sentEvent = new CoreSWMessageSentEvent(message);
        plugin.getEventManager().callEvent(sentEvent);

        // Trigger instant received message since we are talking to local games
        CoreSWMessageReceivedEvent receivedEvent = new CoreSWMessageReceivedEvent(message);
        plugin.getEventManager().callEvent(receivedEvent);

        return callback;
    }

    @Override
    public void replyMessage(SWMessage message, SWMessage replyTo) {
        message.setReplyToId(replyTo.getId());
        message.setChannel(replyTo.getChannel());
        sendMessage(message);
    }

    @Override
    public void startListening() {
        // pass
    }

    @Override
    public void stopListening() {
        // pass
    }

    @Override
    public void startCleaning() {
        cleanupTask = plugin.getScheduler().runAsyncTimer(new CoreSWRunnable() {
            @Override
            public void run() {

                List<SWMessage> toRemove = new ArrayList<>();
                cachedOutgoing.forEach((msg, future) -> {
                    if (System.currentTimeMillis() - msg.getTimestamp() > CLEANUP_TIMEOUT) {
                        toRemove.add(msg);
                    }
                });

                for (SWMessage msg : toRemove) cachedOutgoing.remove(msg);
            }
        }, 0, 200);
    }
}
