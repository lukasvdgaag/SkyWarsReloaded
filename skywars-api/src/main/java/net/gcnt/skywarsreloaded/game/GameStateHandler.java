package net.gcnt.skywarsreloaded.game;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.gameinstance.GameInstance;

public interface GameStateHandler {

    void tick();

    void tickSecond();

    GameInstance getGameInstance();

    SkyWarsReloaded getPlugin();

}
