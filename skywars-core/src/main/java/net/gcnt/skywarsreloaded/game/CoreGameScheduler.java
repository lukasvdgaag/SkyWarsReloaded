package net.gcnt.skywarsreloaded.game;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.types.GameState;
import net.gcnt.skywarsreloaded.wrapper.scheduler.SWRunnable;

import java.util.function.Consumer;

public class CoreGameScheduler implements GameScheduler {

    protected final SkyWarsReloaded plugin;
    protected final GameWorld gameWorld;

    private SWRunnable runnable;
    private int ticksRun;
    private int ticksSinceGameStart;
    private GameStateHandler gameStateHandler;

    public CoreGameScheduler(SkyWarsReloaded plugin, GameWorld world) {
        this.plugin = plugin;
        this.gameWorld = world;
    }

    @Override
    public void start() {
        runnable = plugin.getScheduler().createRunnable(() -> {
            ticksRun++;
            if (gameWorld.getState() == GameState.PLAYING) ticksSinceGameStart++;
            gameStateHandler.tick();

            if (ticksRun % 20 == 0) {
                gameStateHandler.tickSecond();
            }
        });
        plugin.getScheduler().runSyncTimer(runnable, 0, 1);
    }

    @Override
    public void end() {
        if (runnable != null) {
            runnable.cancel();
            runnable = null;
        }
    }

    @Override
    public GameStateHandler getGameStateHandler() {
        return gameStateHandler;
    }

    @Override
    public void setGameStateHandler(GameStateHandler handler) {
        this.gameStateHandler = handler;
    }

    @Override
    public void addDelayedTask(SWRunnable runnable, double delay) {

    }

    @Override
    public void addTimer(Consumer<SWRunnable> consumer, int timer) {

    }

    @Override
    public void updateScoreboards() {

    }

    @Override
    public int getTicksSinceStart() {
        return this.ticksRun;
    }

    @Override
    public int getTicksSinceGameStart() {
        return this.ticksSinceGameStart;
    }

    @Override
    public int getSecondsSinceGameStart() {
        return this.ticksSinceGameStart / 20;
    }


}
