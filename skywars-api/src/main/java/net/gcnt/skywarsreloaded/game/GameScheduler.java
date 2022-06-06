package net.gcnt.skywarsreloaded.game;

import net.gcnt.skywarsreloaded.wrapper.scheduler.SWRunnable;

import java.util.function.Consumer;

public interface GameScheduler {

    void start();

    void end();

    GameStateHandler getGameStateHandler();

    void setGameStateHandler(GameStateHandler handler);

    void addDelayedTask(SWRunnable runnable, double delay);

    void addTimer(Consumer<SWRunnable> consumer, int timer);

    int getTicksSinceGameStart();

    int getTicksSinceStart();

    int getSecondsSinceGameStart();

    void updateScoreboards();

}
