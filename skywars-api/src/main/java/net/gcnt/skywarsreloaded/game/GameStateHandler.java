package net.gcnt.skywarsreloaded.game;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.types.GameStatus;

public interface GameStateHandler {

    void tick();

    void tickSecond();

    GameWorld getGameWorld();

    SkyWarsReloaded getPlugin();

}
