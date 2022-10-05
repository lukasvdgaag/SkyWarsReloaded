package net.gcnt.skywarsreloaded.listener;

import net.gcnt.skywarsreloaded.event.SWEvent;
import net.gcnt.skywarsreloaded.game.gameinstance.GameInstance;
import net.gcnt.skywarsreloaded.game.types.GameState;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;
import net.gcnt.skywarsreloaded.wrapper.event.SWCancellable;
import net.gcnt.skywarsreloaded.wrapper.event.SWPlayerEvent;

import java.util.function.Consumer;

public class CoreSWEventListener<T extends SWEvent> implements SWEventListener<T> {

    private final Class<T> typeClass;
    private final SWListenerPriority priority;
    private final Consumer<T> handler;

    public CoreSWEventListener(Class<T> typeClass, Consumer<T> handler) {
        this(typeClass, SWListenerPriority.NORMAL, handler);
    }

    public CoreSWEventListener(Class<T> typeClass, SWListenerPriority priority, Consumer<T> handler) {
        this.typeClass = typeClass;
        this.priority = priority;
        this.handler = handler;
    }

    public static boolean cancelWhenWaitingInGame(SWPlayer player, SWCancellable cancellable) {
        if (player == null) return false;
        GameInstance gameWorld = player.getGameWorld();
        if (gameWorld == null) return false;

        if (gameWorld.getState().isWaiting() || gameWorld.getState() == GameState.ENDING) {
            cancellable.setCancelled(true);
            return true;
        }
        return false;
    }

    public static boolean cancelWhenWaitingInGame(SWPlayerEvent event) {
        if (!(event instanceof SWCancellable)) return false;

        return cancelWhenWaitingInGame(event.getPlayer(), (SWCancellable) event);
    }

    @Override
    public SWListenerPriority getPriority() {
        return this.priority;
    }

    @Override
    public Class<T> getEventClass() {
        return this.typeClass;
    }

    @Override
    public void onEvent(T event) {
        this.handler.accept(event);
    }
}
